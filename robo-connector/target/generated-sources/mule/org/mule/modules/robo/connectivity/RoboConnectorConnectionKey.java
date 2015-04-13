
package org.mule.modules.robo.connectivity;

import javax.annotation.Generated;


/**
 * A tuple of connection parameters
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class RoboConnectorConnectionKey {

    /**
     * 
     */
    private String robotAddr;

    public RoboConnectorConnectionKey(String robotAddr) {
        this.robotAddr = robotAddr;
    }

    /**
     * Sets robotAddr
     * 
     * @param value Value to set
     */
    public void setRobotAddr(String value) {
        this.robotAddr = value;
    }

    /**
     * Retrieves robotAddr
     * 
     */
    public String getRobotAddr() {
        return this.robotAddr;
    }

    @Override
    public int hashCode() {
        int result = ((this.robotAddr!= null)?this.robotAddr.hashCode(): 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoboConnectorConnectionKey)) {
            return false;
        }
        RoboConnectorConnectionKey that = ((RoboConnectorConnectionKey) o);
        if (((this.robotAddr!= null)?(!this.robotAddr.equals(that.robotAddr)):(that.robotAddr!= null))) {
            return false;
        }
        return true;
    }

}
