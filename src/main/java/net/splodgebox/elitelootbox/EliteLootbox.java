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
import net.splodgebox.eliteapi.PluginAPI;
import net.splodgebox.eliteapi.acf.PaperCommandManager;
import net.splodgebox.eliteapi.message.MessageManager;
import net.splodgebox.elitelootbox.commands.*;
import net.splodgebox.elitelootbox.exceptions.AnimationConfigurationException;
import net.splodgebox.elitelootbox.exceptions.LootboxConfigurationException;
import net.splodgebox.elitelootbox.listeners.LootboxPlayerListeners;
import net.splodgebox.elitelootbox.managers.AnimationSchemaManager;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import net.splodgebox.elitelootbox.managers.LootboxRewardManager;
import net.splodgebox.elitelootbox.menus.LootboxRewardsMenu;
import net.splodgebox.elitelootbox.models.LootboxReward;
import net.splodgebox.elitelootbox.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

@Slf4j
public class EliteLootbox extends JavaPlugin {

    @Getter
    private static EliteLootbox instance;

    private LootboxManager lootboxManager;
    private LootboxRewardManager lootboxRewardManager;
    private AnimationSchemaManager animationSchemaManager;

    @Override
    public void onEnable() {
        instance = this;

        initializeManagers();

        registerListeners();
        registerMessages();
        initializeCommands();
    }

    @Override
    public void onDisable() {
        instance = null;

        if (lootboxManager != null) {
            lootboxManager.cleanup();
        }
    }

    private void initializeManagers() {
        PluginAPI.implementMenuListeners(this);

        try {
            lootboxManager = new LootboxManager(this);
            lootboxManager.loadLootboxes();
        } catch (LootboxConfigurationException e) {
            log.error("Error loading lootbox configurations: ", e);
        }

        try {
            animationSchemaManager = new AnimationSchemaManager(this);
            animationSchemaManager.loadSchemas();
        } catch (AnimationConfigurationException e) {
            log.error("Error loading custom animation configurations: ", e);
        }

        lootboxRewardManager = new LootboxRewardManager(lootboxManager);
    }

    private void initializeCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerDependency(LootboxManager.class, lootboxManager);
        commandManager.registerDependency(LootboxRewardManager.class, lootboxRewardManager);

        commandManager.registerCommand(new DefaultCommand());
        commandManager.registerCommand(new LootboxGiveCommand());
        commandManager.registerCommand(new LootboxAddRewardCommand());
        commandManager.registerCommand(new LootboxListCommand());
        commandManager.registerCommand(new LootboxListRewardsCommand());

        commandManager.getCommandCompletions().registerStaticCompletion("lootboxes", lootboxManager.getLootboxIds());
    }

    private void registerMessages() {
        MessageManager messageManager = new MessageManager(this);
        messageManager.loadMessages(
                // commands
                DefaultCommand.class,
                LootboxAddRewardCommand.class,
                LootboxGiveCommand.class,
                LootboxListCommand.class,
                LootboxListRewardsCommand.class,

                // menus
                LootboxRewardsMenu.class,

                // models
                LootboxReward.class,

                // utils
                Messages.class
        );
    }

    private void registerListeners() {
        PluginAPI.implementMenuListeners(this);
        getServer().getPluginManager().registerEvents(new LootboxPlayerListeners(this, lootboxManager, animationSchemaManager), this);
    }
}