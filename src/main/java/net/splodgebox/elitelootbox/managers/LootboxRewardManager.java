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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.models.LootboxReward;
import net.splodgebox.elitelootbox.utils.RandomCollection;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LootboxRewardManager {

    private final LootboxManager lootboxManager;

    public void addReward(String lootboxName, ItemStack reward, double chance, String command, boolean giveItem, boolean isBonus) {
        Lootbox lootbox = validateLootbox(lootboxName);
        if (lootbox == null) {
            return;
        }

        LootboxReward lootboxReward = new LootboxReward(reward, chance, command, giveItem);
        if (isBonus) {
            lootbox.getBonusRewards().add(lootboxReward);
        } else {
            lootbox.getRewards().add(lootboxReward);
        }

        lootboxManager.saveLootbox(lootbox);
    }

    public void editReward(String lootboxName, int index, LootboxReward reward, boolean isBonus) {
        Lootbox lootbox = validateLootbox(lootboxName);
        if (lootbox == null) {
            return;
        }

        List<LootboxReward> rewardList = isBonus ? lootbox.getBonusRewards() : lootbox.getRewards();

        if (index < 0 || index >= rewardList.size()) {
            throw new IndexOutOfBoundsException("Invalid reward index: " + index);
        }

        rewardList.set(index, reward);
        lootboxManager.saveLootbox(lootbox);
    }

    public void removeReward(String lootboxName, int index, boolean isBonus) {
        Lootbox lootbox = validateLootbox(lootboxName);
        if (lootbox == null) {
            return;
        }

        List<LootboxReward> rewardList = isBonus ? lootbox.getBonusRewards() : lootbox.getRewards();

        if (index < 0 || index >= rewardList.size()) {
            throw new IndexOutOfBoundsException("Invalid reward index: " + index);
        }

        rewardList.remove(index);
        lootboxManager.saveLootbox(lootbox);
    }

    private Lootbox validateLootbox(String lootboxName) {
        Lootbox lootbox = lootboxManager.getLootbox(lootboxName);
        if (lootbox == null) {
            log.error("Invalid lootbox name: {}", lootboxName);
        }
        return lootbox;
    }
}