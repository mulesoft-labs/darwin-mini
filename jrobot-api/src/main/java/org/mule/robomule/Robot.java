/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.robomule;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class Robot {

    public static final UUID SERIAL_UUID = new UUID(0x1101);

    private RemoteDevice btDevice;

    Robot(RemoteDevice btDevice) {
        this.btDevice = btDevice;
    }

    public String getName() {
        try {
            return btDevice.getFriendlyName(false);
        } catch (IOException e) {
            return "";
        }
    }

    public String getAddress() {
        return btDevice.getBluetoothAddress();
    }

    public RobotSession connect() throws IOException {
        ServiceDiscovery discoveryListener = new ServiceDiscovery(btDevice);
        String connectionURL = discoveryListener.getConnectionURL();
        if (connectionURL == null) {
            throw new RuntimeException("Is this device robot address : " + getAddress() + " ?");
        }
        MotionDefinition motionDefinition = new MotionDefinition();
        motionDefinition.load(getClass().getClassLoader().getResourceAsStream("motionFile.xml"));
        return new RobotSession((StreamConnection) Connector.open(connectionURL), motionDefinition);
    }

    private static class ServiceDiscovery implements DiscoveryListener {
        private final CountDownLatch latch;
        private String connectionURL;
        private RemoteDevice btDevice;

        public ServiceDiscovery(RemoteDevice btDevice) {
            this.btDevice = btDevice;
            this.latch = new CountDownLatch(1);
        }

        public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {

        }

        public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            if (servRecord != null && servRecord.length > 0) {
                connectionURL = servRecord[0].getConnectionURL(0, false);
            }
        }

        public void serviceSearchCompleted(int transID, int respCode) {
            latch.countDown();
        }

        public void inquiryCompleted(int discType) {

        }

        public String getConnectionURL() throws BluetoothStateException {
            if (connectionURL == null) {
                final LocalDevice localDevice = LocalDevice.getLocalDevice();
                final DiscoveryAgent agent = localDevice.getDiscoveryAgent();
                final UUID[] uuidSet = new UUID[1];
                uuidSet[0] = SERIAL_UUID;
                agent.searchServices(null, uuidSet, btDevice, this);
                try {
                    latch.await();
                } catch (InterruptedException e) {

                }
            }
            return connectionURL;
        }


    }
}
