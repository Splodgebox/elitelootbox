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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.splodgebox.eliteapi.util.FileManager;
import net.splodgebox.eliteapi.xseries.XMaterial;
import net.splodgebox.elitelootbox.EliteLootbox;
import net.splodgebox.elitelootbox.exceptions.LootboxConfigurationException;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.models.LootboxReward;
import net.splodgebox.elitelootbox.models.enums.AnimationType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class LootboxManager {

    private final EliteLootbox plugin;
    private final HashMap<String, FileManager> lootboxFiles;

    @Getter
    private final HashMap<String, Lootbox> lootboxes;

    public LootboxManager(EliteLootbox plugin) throws LootboxConfigurationException {
        this.plugin = plugin;
        this.lootboxes = new HashMap<>();
        this.lootboxFiles = new HashMap<>();

        loadFiles();
    }

    public Lootbox getLootbox(String key) {
        return lootboxes.get(key);
    }

    public Set<String> getLootboxIds() {
        return lootboxes.keySet();
    }

    public void loadLootboxes() {
        lootboxFiles.forEach((key, config) -> {
            try {
                Lootbox lootbox = parseLootbox(key, config.getConfig());
                if (lootbox != null) {
                    lootboxes.put(key, lootbox);
                } else {
                    log.warn("Failed to load lootbox: {} - invalid configuration.", key);
                }
            } catch (Exception e) {
                log.error("Error loading lootbox: {} - {}", key, e.getMessage(), e);
            }
        });
    }

    public void saveLootbox(Lootbox lootbox) {
        FileManager fileManager = lootboxFiles.get(lootbox.getId());
        YamlConfiguration config = fileManager.getConfig();
        saveRewards(config, lootbox.getRewards());
        fileManager.save();
    }

    private void saveRewards(YamlConfiguration config, List<LootboxReward> rewards) {
        if (rewards == null || rewards.isEmpty()) {
            return;
        }

        for (int index = 0; index < rewards.size(); index++) {
            LootboxReward reward = rewards.get(index);
            String path = "rewards." + index + ".";
            config.set(path + "item", reward.getItemStack());
            config.set(path + "chance", reward.getChance());
        }
    }

    private Lootbox parseLootbox(String key, YamlConfiguration config) {
        if (config == null) {
            log.warn("Lootbox configuration is null for key: {}", key);
            return null;
        }

        String itemPath = "item.";
        String displayName = config.getString(itemPath + "name");
        if (displayName == null || displayName.isEmpty()) {
            log.warn("Missing display name for lootbox: {}", key);
            return null;
        }

        List<String> lore = config.getStringList(itemPath + "lore");

        String materialName = config.getString(itemPath + "material");
        if (materialName == null || materialName.isEmpty()) {
            log.warn("Missing material for lootbox: {}. Defaulting to CHEST.", key);
            materialName = "CHEST";
        }
        XMaterial material = XMaterial.matchXMaterial(materialName).orElse(XMaterial.CHEST);

        int modelData = config.getInt(itemPath + "model-data", 0);

        String animationPath = "animation.";
        String animationType = config.getString(animationPath + "type", "LOOTBOX");
        AnimationType parsedAnimationType;
        try {
            parsedAnimationType = AnimationType.valueOf(animationType);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid animation type for lootbox: {}. Defaulting to LOOTBOX.", key);
            parsedAnimationType = AnimationType.LOOTBOX;
        }

        return new Lootbox(
                key,
                displayName,
                lore,
                material,
                modelData,
                parsedAnimationType,
                getLootboxRewards(key, config)
        );
    }

    private List<LootboxReward> getLootboxRewards(String key, YamlConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("rewards");

        if (section == null) {
            log.warn("Rewards section is missing/invalid for lootbox: {}", key);
            return null;
        }

        List<LootboxReward> rewards = new ArrayList<>();
        section.getKeys(false).forEach(rewardKey -> {
            LootboxReward reward = parseReward(config, rewardKey);
            if (reward != null) {
                rewards.add(reward);
            }
        });

        return rewards;
    }

    private LootboxReward parseReward(YamlConfiguration config, String rewardKey) {
        String path = "rewards." + rewardKey + ".";
        ItemStack itemStack = config.getItemStack(path + "item");
        double chance = config.getDouble(path + "chance", 100.0);
        String command = config.getString(path + "command", null);
        boolean giveItem = config.getBoolean(path + "give-item", true);
        return itemStack != null ? new LootboxReward(itemStack, chance, command, giveItem) : null;
    }

    public void loadFiles() throws LootboxConfigurationException {
        File lootboxDirectory = new File(plugin.getDataFolder(), "lootbox");
        ensureDirectoryAndExampleFile(lootboxDirectory);

        try (Stream<Path> paths = Files.walk(lootboxDirectory.toPath())) {
            paths.filter(Files::isRegularFile).forEach(this::processFile);
        } catch (IOException e) {
            throw new LootboxConfigurationException("Error loading lootbox files: ", e);
        }
    }

    private void ensureDirectoryAndExampleFile(File directory) {
        if (!directory.exists()) {
            plugin.saveResource("lootbox/example.yml", false);
        }
    }

    private void processFile(Path file) {
        String fileName = file.getFileName().toString();
        String baseName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;

        lootboxFiles.put(baseName, new FileManager(plugin, fileName, plugin.getDataFolder() + "/lootbox/"));
        log.info("Loaded lootbox file: {}", fileName);
    }

    public void cleanup() {
        lootboxes.clear();
        lootboxFiles.clear();
        log.info("LootboxManager cleaned up successfully.");
    }
}
