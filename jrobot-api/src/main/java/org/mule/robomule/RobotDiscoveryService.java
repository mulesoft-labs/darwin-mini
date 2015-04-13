/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.robomule;

import javax.bluetooth.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class RobotDiscoveryService {

    public static String ROBOT_ADDRESS = "0FA5";

    public static void main(String[] args) throws IOException {
        final List<Robot> robots = new RobotDiscoveryService().findRobots(new AddressRobotPredicate(ROBOT_ADDRESS));
        final RobotSession robotSession = robots.get(0).connect();
        
        String commandName = "Push_Up";
        for(int indexEnum = 0; indexEnum < RobotMotion.values().length; indexEnum++)
        {
            RobotMotion robotMotion = RobotMotion.values()[indexEnum];
            if (commandName == robotMotion.toString())
            {
                try {
                    robotSession.send(robotMotion);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                    
        }
        


        robotSession.disconnect();
        System.out.println("Done");
    }


    public List<Robot> findRobots(final RobotPredicate predicate) throws BluetoothStateException {
        final List<Robot> result = new ArrayList<Robot>();
        final CountDownLatch latch = new CountDownLatch(1);
        final DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                try {
                    String friendlyName = btDevice.getFriendlyName(false);
                    String bluetoothAddress = btDevice.getBluetoothAddress();
                    if (predicate.accept(friendlyName, bluetoothAddress)) {
                        System.out.println("Robot found : " + friendlyName + " " + bluetoothAddress);
                        result.add(new Robot(btDevice));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void inquiryCompleted(int discType) {
                latch.countDown();
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };


        final LocalDevice localDevice = LocalDevice.getLocalDevice();
        final DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
        final boolean started = discoveryAgent.startInquiry(DiscoveryAgent.GIAC, listener);
        if (started) {
            try {
                latch.await();
            } catch (InterruptedException e) {

            }
        }

        return result;
    }

    public static interface RobotPredicate {
        boolean accept(String name, String address);
    }

    public static class NameRobotPredicate implements RobotPredicate {

        private String namePattern;

        public NameRobotPredicate(String namePattern) {
            this.namePattern = namePattern;
        }

        public boolean accept(String name, String address) {
            return name.contains(this.namePattern);
        }
    }

    public static class AddressRobotPredicate implements RobotPredicate {

        private String address;

        public AddressRobotPredicate(String address) {
            this.address = address;
        }

        public boolean accept(String name, String address) {
            return address.contains(this.address);
        }
    }


}
