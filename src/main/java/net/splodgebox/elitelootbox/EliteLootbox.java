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

package net.splodgebox.elitelootbox;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.splodgebox.elitelootbox.exceptions.LootboxConfigurationException;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public class EliteLootbox extends JavaPlugin {

    @Getter
    private static EliteLootbox instance;

    @Getter
    private LootboxManager lootboxManager;

    @Override
    public void onEnable() {
        instance = this;

        try {
            lootboxManager = new LootboxManager(this);
            lootboxManager.loadLootboxes();
        } catch (LootboxConfigurationException e) {
            log.error("Error loading lootbox configurations: ", e);
            log.error("Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        instance = null;

        if (lootboxManager != null) {
            lootboxManager.cleanup();
        }
    }
}