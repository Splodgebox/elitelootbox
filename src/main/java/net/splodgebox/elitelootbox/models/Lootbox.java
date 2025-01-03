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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.splodgebox.eliteapi.item.ItemBuilder;
import net.splodgebox.eliteapi.xseries.XMaterial;
import net.splodgebox.elitelootbox.models.enums.AnimationType;
import net.splodgebox.elitelootbox.utils.RandomCollection;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Lootbox {

    private final String id;

    private final String name;
    private final List<String> lore;
    private final XMaterial material;
    private final int modelData;

    private final AnimationType animationType;

    private final List<LootboxReward> rewards;
    private final List<LootboxReward> bonusRewards;

    public ItemStack create() {
        return new ItemBuilder(material)
                .name(name)
                .lore(lore)
                .modelData(modelData)
                .setNbtStr("Lootbox", id)
                .build();
    }

    public RandomCollection<LootboxReward> getRewardCollection() {
        RandomCollection<LootboxReward> collection = new RandomCollection<>();
        rewards.forEach(reward -> collection.add(reward.getChance(), reward));
        return collection;
    }

    public RandomCollection<LootboxReward> getBonusRewardCollection() {
        RandomCollection<LootboxReward> collection = new RandomCollection<>();
        bonusRewards.forEach(reward -> collection.add(reward.getChance(), reward));
        return collection;
    }

}
