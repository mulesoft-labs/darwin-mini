/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.robomule.exceptions;

/**
 * Created by estebanwasinger on 4/29/15.
 */
public class RobotOfflineException extends RuntimeException{

    RobotOfflineException(String msg, Throwable e){
        super(msg,e);
    }

    public RobotOfflineException(String msg){
        super(msg);
    }
}
