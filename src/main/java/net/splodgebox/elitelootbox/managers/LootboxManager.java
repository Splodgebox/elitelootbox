package net.splodgebox.elitelootbox.managers;

import lombok.extern.slf4j.Slf4j;
import net.splodgebox.eliteapi.chat.Chat;
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
import java.util.stream.Stream;

@Slf4j
public class LootboxManager {

    private final EliteLootbox plugin;
    private final HashMap<String, Lootbox> lootboxes;
    private final HashMap<String, YamlConfiguration> lootboxFiles;

    public LootboxManager(EliteLootbox plugin) throws LootboxConfigurationException {
        this.plugin = plugin;
        this.lootboxes = new HashMap<>();
        this.lootboxFiles = new HashMap<>();

        loadFiles();
    }

    public void loadLootboxes() {
        lootboxFiles.forEach((key, config) -> {
            try {
                Lootbox lootbox = parseLootbox(key, config);
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

    public List<LootboxReward> getLootboxRewards(String key, YamlConfiguration config) {
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

    public LootboxReward parseReward(YamlConfiguration config, String rewardKey) {
        String path = "rewards." + rewardKey + ".";
        ItemStack itemStack = config.getItemStack(path + "item");
        double chance = config.getDouble(path + "chance", 1.0);
        return itemStack != null && chance > 0.0 ? new LootboxReward(itemStack, chance) : null;
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

        lootboxFiles.put(baseName, YamlConfiguration.loadConfiguration(file.toFile()));
        log.info("Loaded lootbox file: {}", fileName);
    }

    public void cleanup() {
        lootboxes.clear();
        lootboxFiles.clear();
        log.info("LootboxManager cleaned up successfully.");
    }
}
