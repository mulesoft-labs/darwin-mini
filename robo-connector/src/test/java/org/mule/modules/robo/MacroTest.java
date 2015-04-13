/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.robo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class MacroTest {

    @Test
    public void whenSendingOneCommandItShouldParseItCorrectly() {
        List<CommandAndTime> parseCommand = new RoboConnector().parseCommand("Stand_Up");
        List<CommandAndTime> expectedCommand = Arrays.asList(new CommandAndTime(1, "Stand_Up"));
        Assert.assertTrue(parseCommand.equals(expectedCommand));
    }

    @Test
    public void whenSendingTwoCommandsItShouldParseItCorrectly() {
        List<CommandAndTime> parseCommand = new RoboConnector().parseCommand("Stand_Up->Horse_Dance");
        List<CommandAndTime> expectedCommand = Arrays.asList(new CommandAndTime(1, "Stand_Up"), new CommandAndTime(1, "Horse_Dance"));
        Assert.assertTrue(parseCommand.equals(expectedCommand));
    }

    @Test
    public void whenSendingOneWithRepetitionCommandItShouldParseItCorrectly() {
        List<CommandAndTime> parseCommand = new RoboConnector().parseCommand("Stand_Up(2)");
        List<CommandAndTime> expectedCommand = Arrays.asList(new CommandAndTime(2, "Stand_Up"));
        Assert.assertTrue(parseCommand.equals(expectedCommand));
    }

    @Test
    public void whenSendingTwoWithRepetitionCommandItShouldParseItCorrectly() {
        List<CommandAndTime> parseCommand = new RoboConnector().parseCommand("Stand_Up(2)->Horse_Dance(3)");
        List<CommandAndTime> expectedCommand = Arrays.asList(new CommandAndTime(2, "Stand_Up"), new CommandAndTime(3, "Horse_Dance"));
        Assert.assertEquals(parseCommand, expectedCommand);
    }

    @Test
    public void whenSendingFourCommandsUsingRepetitionsShouldParseItCorrectly() {
        List<CommandAndTime> parseCommand = new RoboConnector().parseCommand("Left_Kick->Right_Kick->Front_Roll->Greet1(3)");
        List<CommandAndTime> expectedCommand = Arrays.asList(new CommandAndTime(1, "Left_Kick"), new CommandAndTime(1, "Right_Kick"),new CommandAndTime(1,"Front_Roll"),new CommandAndTime(3,"Greet1"));
        Assert.assertEquals(parseCommand, expectedCommand);
    }

    @Test
    public void whenCreatingTwoCommandAndTimeObjectsThatHaveSameFieldsShouldHaveSameHashCode() {
        CommandAndTime cmdAndTime = new CommandAndTime(2, "laralala");
        CommandAndTime cmdAndTime2 = new CommandAndTime(2, "laralala");
        Assert.assertEquals(cmdAndTime.hashCode(), cmdAndTime2.hashCode());
    }

}
