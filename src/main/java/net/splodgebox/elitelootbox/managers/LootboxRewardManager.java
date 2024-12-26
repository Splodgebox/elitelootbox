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

package net.splodgebox.elitelootbox.managers;

import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.models.LootboxReward;
import org.bukkit.inventory.ItemStack;

public class LootboxRewardManager {

    private final LootboxManager lootboxManager;

    public LootboxRewardManager(LootboxManager lootboxManager) {
        this.lootboxManager = lootboxManager;
    }

    public void addReward(String lootboxName, ItemStack reward, double chance) {
        Lootbox lootbox = lootboxManager.getLootbox(lootboxName);
        lootbox.getRewards().add(new LootboxReward(reward, chance));
        lootboxManager.saveLootbox(lootbox);
    }

    public void removeReward(String lootboxName, int index) {
        Lootbox lootbox = lootboxManager.getLootbox(lootboxName);
        lootbox.getRewards().remove(index);
        lootboxManager.saveLootbox(lootbox);
    }

}
