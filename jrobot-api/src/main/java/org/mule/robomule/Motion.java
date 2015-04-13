/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.robomule;


public class Motion {

    
    public int getRobotId() {
        return RobotId;
    }
    
    private void setRobotId(int robotId) {
        RobotId = robotId;
    }
    
    public long getWaitTime() {
        return WaitTime;
    }
    
    private void setWaitTime(long waitTime) {
        WaitTime = waitTime;
    }
    private int RobotId;
    private long WaitTime;
    private String command;
    
    
    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
    
    

    public Motion(int id, long waitingTime,String command)
    {
       RobotId = id;
       WaitTime = waitingTime;
       setCommand(command);
    }
}
