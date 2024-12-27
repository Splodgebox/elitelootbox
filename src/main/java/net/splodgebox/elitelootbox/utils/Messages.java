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

    @Message(path = "general.player_not_found", defaultMessage = "&3&lElite&b&lLootbox&f: &7Unable to find this player!")
    public static String playerNotFound;

    @Message(path = "general.item_null", defaultMessage = "&3&lElite&b&lLootbox&f: &7Specified item is null!")
    public static String itemNull;

    @Message(path = "general.cannot_send_from_console", defaultMessage = "&3&lElite&b&lLootbox&f: &7You cannot send this command from the console!")
    public static String cannotSendFromConsole;

    @Message(path = "general.reward_added", defaultMessage = "&3&lElite&b&lLootbox&f: &aReward successfully added to lootbox &b{LOOTBOX}&a with a chance of &b{CHANCE}&a!")
    public static String rewardAdded;

    @Message(path = "general.lootbox_not_found", defaultMessage = "&3&lElite&b&lLootbox&f: &7Lootbox &b{LOOTBOX}&7 does not exist!")
    public static String lootboxNotFound;

}
