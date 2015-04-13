/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.robo;

public class CommandAndTime {
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	private String command;
	private int time;

	CommandAndTime(int time, String command) {
		setTime(time);
		setCommand(command);
	}

	@Override public boolean equals(Object other) {
	    boolean result = false;
	    if (other instanceof CommandAndTime) {
	    	CommandAndTime that = (CommandAndTime) other;
	        result = (this.getCommand().equals(that.getCommand()) && this.getTime() == that.getTime());
	    }
	    return result;
	}
	
	@Override public int hashCode(){
		int aux = 17;
		int result = 0;
		result += 31* aux +command.hashCode();
		result += 31* aux +time;
		return result;
	}
}