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

package net.splodgebox.elitelootbox.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.splodgebox.eliteapi.item.ItemBuilder;
import net.splodgebox.eliteapi.message.Message;
import net.splodgebox.elitelootbox.utils.RandomCollection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class LootboxReward {

    @Message(path = "lootbox_reward.display_chance", defaultMessage = "&6Chance: &e{CHANCE}&6%")
    public static String chanceMessage;

    @Message(path = "lootbox_reward.display_command", defaultMessage = "&6Command: &e{COMMAND}")
    public static String commandMessage;

    @Message(path = "lootbox_reward.display_command_none", defaultMessage = "&enone")
    public static String noCommandMessage;

    @Message(path = "lootbox_reward.display_give_item", defaultMessage = "&6Give-Item?: &e{GIVE-ITEM}")
    public static String giveItemMessage;

    private ItemStack itemStack;
    private double chance;
    @Nullable
    private String command;
    private boolean giveItem;

    public ItemStack createDisplayItem() {
        return new ItemBuilder(itemStack)
                .addLore(chanceMessage, "{CHANCE}", String.valueOf(chance))
                .addLore(commandMessage, "{COMMAND}", command == null ? noCommandMessage : command)
                .addLore(giveItemMessage, "{GIVE-ITEM}", String.valueOf(giveItem))
                .build();
    }

}
