/*
 * Copyright (c) 2024. ElitePlugins License.
 * Permission is hereby granted to use this software (the "Software") for personal purposes or on private servers. Redistribution,
 * modification for distribution, or sale of this software is strictly prohibited.
 *
 * This software is provided "as is," without warranty of any kind, express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose, or non-infringement. In no event shall the author or copyright holder be liable for
 * any claim, damages, or other liability, whether in an action of contract, tort, or otherwise, arising from, out of, or in connection
 * with the Software or its use.
 *
 * See LICENSE.txt for details.
 */

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
