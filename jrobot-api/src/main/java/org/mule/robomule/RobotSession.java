/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.robomule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

public class RobotSession {

    private final StreamConnection connection;
    private OutputStream outStream;
    private InputStream inStream;
    private MotionDefinition motionDefinition;

    public RobotSession(StreamConnection connection, MotionDefinition motionDefinition) throws IOException {
        this.connection = connection;
        this.motionDefinition = motionDefinition;
        this.outStream = connection.openOutputStream();
        this.inStream = connection.openInputStream();

    }

    public boolean send(RobotMotion motionName) throws IOException {
        System.out.println("Motion Name -> " + motionName);
        Integer number = motionDefinition.getActionCode(motionName);

        System.out.println("Command Number -> " + number);
        String message = RobotUtils.toRoboCommand(number);
        System.out.println("Message -> " + message);
        if (sendMessage(message)) {
            waitRobotToExecute(motionDefinition.getWaitTime(motionName));
            return true;
        } else {
            return false;
        }
    }

    private void waitRobotToExecute(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {

        }
    }

    public void disconnect() throws IOException {
        connection.close();
    }

    public boolean sendMessage(String message) {
        try {
            outStream.write(RobotUtils.toByteArray(message));
            outStream.flush();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

}
