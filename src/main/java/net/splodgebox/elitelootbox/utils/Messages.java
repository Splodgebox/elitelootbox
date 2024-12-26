package net.splodgebox.elitelootbox.utils;

import net.splodgebox.eliteapi.message.Message;

public class Messages {

    @Message(path = "commands.no_permission", defaultMessage = "&4&l(!) &4You do not have permission to do this")
    public String noPermission;

    @Message(path = "general.welcome", defaultMessage = "&aWelcome to the server!")
    public static String WELCOME;

    @Message(path = "general.goodbye", defaultMessage = "&cGoodbye! See you next time.")
    public String GOODBYE;

}
