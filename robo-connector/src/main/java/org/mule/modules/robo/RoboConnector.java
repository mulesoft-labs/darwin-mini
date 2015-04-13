/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.robo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mule.api.annotations.Connector;
import org.mule.modules.robo.processors.SendCommandToParseMessageProcessor;
import org.mule.robomule.*;
import org.mule.robomule.RobotDiscoveryService.AddressRobotPredicate;
import org.mule.robomule.RobotDiscoveryService.RobotPredicate;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectStrategy;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Optional;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Processor;

import org.mule.api.annotations.param.Default;

/**
 * Anypoint Connector
 * 
 * @author MuleSoft, Inc.
 */
@Connector(name = "robo", schemaVersion = "1.0", friendlyName = "Robo")
public class RoboConnector {

	private RobotSession robotSession;
	private String robotName;

	/**
	 * Configurable
	 */

	/**
	 * Connect
	 * 
	 * @param username
	 *            A username
	 * @param password
	 *            A password
	 * @throws ConnectionException
	 */
	@Connect(strategy = ConnectStrategy.SINGLE_INSTANCE)
	public void connect(@ConnectionKey String robotAddr)
			throws ConnectionException {
		if (robotSession != null)
			return;
		try {
			final List<Robot> robots = new RobotDiscoveryService()
					.findRobots(new AddressRobotPredicate(robotAddr));
			robotSession = robots.get(0).connect();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConnectionException(
					ConnectionExceptionCode.INCORRECT_CREDENTIALS, robotAddr,
					robotAddr);
		}

	}

	/**
	 * Disconnect
	 */
	@Disconnect
	public void disconnect() {
		try {
			robotSession.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Are we connected
	 */
	@ValidateConnection
	public boolean isConnected() {
		return robotSession != null;
	}

	/**
	 * Are we connected
	 */
	@ConnectionIdentifier
	public String connectionId() {
		return robotName;
	}

	/**
	 * Custom processor
	 * 
	 * {@sample.xml ../../../doc/robo-connector.xml.sample robo:myProcessor}
	 * 
	 * @param content
	 *            Content to be processed
	 * @return Some string
	 */
	@Processor
	public synchronized boolean sendMessage(RobotMotion roboMotion) {
		try {
			robotSession.send(roboMotion);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Custom processor
	 * 
	 * {@sample.xml ../../../doc/robo-connector.xml.sample robo:myProcessor}
	 * 
	 * @param content
	 *            Content to be processed
	 * @return Some string
	 */
	@Processor
	public synchronized boolean sendCommandName(String roboMotionName) {
		roboMotionName = roboMotionName.trim().toLowerCase();
		for (int indexEnum = 0; indexEnum < RobotMotion.values().length; indexEnum++) {
			RobotMotion robotMotion = RobotMotion.values()[indexEnum];
			if (roboMotionName.equals(robotMotion.toString().toLowerCase())) {
				try {
					robotSession.send(robotMotion);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return true;

	}

	@Processor
	public synchronized boolean sendCommandToParse(String commandToParse) {
		List<CommandAndTime> motionsToSend = parseCommand(commandToParse);
		for (int index = 0; index < motionsToSend.size(); index++) {
			for (int index2 = 0; index2 < motionsToSend.get(index).getTime(); index2++) {
				sendCommandName(motionsToSend.get(index).getCommand());
			}
		}
		return true;

	}

	@Processor
	public  String getHelp() {
		String help = new String();
		help += "Here is the list of available commands \n";
		help += "You can send more than one command using this pattern \n";
		help += "command(n. of repetitions)->command2(n.o.r.)... \n";
		help += "If the command should be written exactly as listed below, otherwise it won't work \n";
		for (int index = 0; index < RobotMotion.values().length; index++)
		{
			help += RobotMotion.values()[index].toString();
			help += "\n";
		}
		System.out.print(help);
		return help;
		
	}
	
	public List<CommandAndTime> parseCommand(String commandToParse) {
		List<CommandAndTime> motionsToSend = new ArrayList<CommandAndTime>();
		String[] splitedCommands = commandToParse.split("->");
		for (int index = 0; index < splitedCommands.length; index++) {
			readTimeAndCommand(motionsToSend, splitedCommands[index]);
		}
		return motionsToSend;
	}

	private void readTimeAndCommand(List<CommandAndTime> motionsToSend,
			String splitedCommand) {
		String[] commandAndTime = splitedCommand.split("\\(");
		String command = commandAndTime[0];
		String times = "1";
		if (commandAndTime.length > 1) {
			int length = commandAndTime[1].length();
			times = commandAndTime[1].substring(0, length - 1);
		}
		motionsToSend.add(new CommandAndTime(Integer.parseInt(times), command));

	}

}
