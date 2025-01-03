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

package net.splodgebox.elitelootbox.listeners;

import lombok.RequiredArgsConstructor;
import net.splodgebox.eliteapi.item.ItemUtils;
import net.splodgebox.elitelootbox.animations.SchemaAnimation;
import net.splodgebox.elitelootbox.managers.AnimationSchemaManager;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import net.splodgebox.elitelootbox.models.AnimationSchema;
import net.splodgebox.elitelootbox.models.Lootbox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class LootboxPlayerListeners implements Listener {

    private final Plugin plugin;
    private final LootboxManager manager;
    private final AnimationSchemaManager animationSchemaManager;

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!ItemUtils.isValid(itemStack)) return;
        if (!ItemUtils.hasTag(itemStack, "Lootbox")) return;
        event.setCancelled(true);

        Lootbox lootbox = manager.getLootbox(ItemUtils.getString(itemStack, "Lootbox"));
        SchemaAnimation schemaAnimation = new SchemaAnimation(plugin, lootbox, player, animationSchemaManager.getSchema("default"));
        schemaAnimation.start();
    }

}
