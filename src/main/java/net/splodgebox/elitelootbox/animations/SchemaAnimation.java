/*
 * Copyright (c) 2025. ElitePlugins License.
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

package net.splodgebox.elitelootbox.animations;

import net.splodgebox.eliteapi.chat.Chat;
import net.splodgebox.eliteapi.gui.menu.Button;
import net.splodgebox.eliteapi.gui.menu.Menu;
import net.splodgebox.eliteapi.gui.menu.actions.CloseAction;
import net.splodgebox.eliteapi.item.ItemBuilder;
import net.splodgebox.eliteapi.xseries.XMaterial;
import net.splodgebox.elitelootbox.models.AnimationSchema;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.models.LootboxReward;
import net.splodgebox.elitelootbox.utils.RandomCollection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchemaAnimation extends Animation {

    private static final String SHUFFLING_REWARDS = "SHUFFLING_REWARD";
    private static final String STATIC_REWARDS = "STATIC_REWARD";
    private static final String COUNTDOWN_SLOT = "COUNTDOWN_SLOT";

    private final Plugin plugin;
    private final Lootbox lootbox;
    private final Player player;
    private final AnimationSchema schema;

    private final List<Integer> shufflingRewards;
    private final List<Integer> staticRewards;
    private final List<Integer> countdownSlots;

    private Menu inventory;
    private final RandomCollection<LootboxReward> rewards;
    private final RandomCollection<LootboxReward> bonusRewards;

    public SchemaAnimation(Plugin plugin, Lootbox lootbox, Player player, AnimationSchema schema) {
        super(plugin, lootbox, player);
        this.plugin = plugin;
        this.lootbox = lootbox;
        this.player = player;
        this.schema = schema;

        this.rewards = lootbox.getRewardCollection();
        this.bonusRewards = lootbox.getBonusRewardCollection();

        shufflingRewards = new ArrayList<>();
        staticRewards = new ArrayList<>();
        countdownSlots = new ArrayList<>();

        setup();

        inventory.setCloseAction(onClose());
    }

    @Override
    public void setup() {
        inventory = new Menu(Chat.color(lootbox.getName()), schema.getSchemaRows().size());
        setupInventory();
        updateStaticRewards();

        inventory.open(player);
    }

    @Override
    public BukkitTask start() {
        return new BukkitRunnable() {
            int tickCounter = 0;
            int totalTime = schema.getTotalDuration();

            @Override
            public void run() {
                tickCounter += schema.getShufflingInterval();

                if (tickCounter >= 20) {
                    tickCounter = 0;
                    totalTime--;

                    updateCountdownSlots(totalTime);
                }

                if (totalTime <= 0) {
                    // Give rewards
                    cancel();
                    return;
                }

                updateShufflingRewards();
            }
        }.runTaskTimer(plugin, 0L, schema.getShufflingInterval());
    }

    @Override
    public void stop() {
        // Logic to finalize animation (e.g., clean up resources, close inventory)
    }

    @Override
    public CloseAction onClose() {
        return (player, inventoryCloseEvent) -> {
            // Stop them from closing the inventory
        };
    }

    private void updateCountdownSlots(int time) {
        for (Integer slot : countdownSlots) {
            inventory.setButton(slot, new Button(new ItemBuilder(XMaterial.BLACK_STAINED_GLASS_PANE)
                    .amount(time)
                    .name("&e&lCountdown: &6" + time)
                    .build(),
                    (p, event) -> event.setCancelled(true)
            ));
        }
    }

    private void updateShufflingRewards() {
        shufflingRewards.forEach(slot -> {
            inventory.setButton(slot, new Button(rewards.getRandom(true).getItemStack(),
                    (p, event) -> event.setCancelled(true)
            ));
        });
    }

    private void updateStaticRewards() {
        staticRewards.forEach(slot -> {
            inventory.setButton(slot, new Button(bonusRewards.getRandom(false).getItemStack(),
                    (p, event) -> event.setCancelled(true)
            ));
        });
    }

    private void setupInventory() {
        Map<Character, String> itemMap = schema.getSchemaMap();
        int slot = 0;

        for (String row : schema.getSchemaRows()) {
            for (String symbol : row.split(" ")) {
                configureSlot(itemMap, symbol, slot++);
            }
        }
    }

    private void configureSlot(Map<Character, String> itemMap, String symbol, int slot) {
        if (symbol.isEmpty() || !itemMap.containsKey(symbol.charAt(0))) return;

        String item = itemMap.get(symbol.charAt(0));
        if (item == null) return;

        switch (item.toUpperCase()) {
            case SHUFFLING_REWARDS -> shufflingRewards.add(slot);
            case STATIC_REWARDS -> staticRewards.add(slot);
            case COUNTDOWN_SLOT -> countdownSlots.add(slot);
            default -> addButtonToInventory(slot, item);
        }
    }

    private void addButtonToInventory(int slot, String item) {
        String[] parts = item.contains(":") ? item.split(":") : new String[]{item, " "};
        addInventoryButton(slot, parts[0], parts[1]);
    }

    private void addInventoryButton(int slot, String materialName, String displayName) {
        inventory.setButton(slot, new Button(
                new ItemBuilder(XMaterial.matchXMaterial(materialName).orElse(XMaterial.AIR))
                        .name(displayName)
                        .build(),
                (p, event) -> event.setCancelled(true)
        ));
    }
}
