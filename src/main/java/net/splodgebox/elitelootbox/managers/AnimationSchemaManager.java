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

import lombok.extern.slf4j.Slf4j;
import net.splodgebox.eliteapi.util.FileManager;
import net.splodgebox.elitelootbox.EliteLootbox;
import net.splodgebox.elitelootbox.animations.SchemaAnimation;
import net.splodgebox.elitelootbox.exceptions.AnimationConfigurationException;
import net.splodgebox.elitelootbox.models.AnimationSchema;
import net.splodgebox.elitelootbox.models.enums.StaticRewardType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class AnimationSchemaManager {

    private final EliteLootbox plugin;
    private final Map<String, AnimationSchema> schemas;
    private final Map<String, FileManager> schemaFiles;

    public AnimationSchemaManager(EliteLootbox plugin) throws AnimationConfigurationException {
        this.plugin = plugin;
        this.schemas = new HashMap<>();
        this.schemaFiles = new HashMap<>();
        loadFiles();
    }

    public void loadSchemas() {
        schemaFiles.forEach((id, fileManager) -> {
            try {
                YamlConfiguration config = fileManager.getConfig();
                AnimationSchema schema = buildSchemaFromConfig(id, config);
                schemas.put(id, schema);
            } catch (Exception e) {
                log.error("Failed to load schema for ID: {}", id, e);
            }
        });
    }

    public AnimationSchema getSchema(String id) {
        return schemas.get(id);
    }

    private AnimationSchema buildSchemaFromConfig(String id, YamlConfiguration config) {
        AnimationSchema.AnimationSchemaBuilder builder = AnimationSchema.builder();
        builder.id(id);
        builder.schemaRows(config.getStringList("schema"));
        builder.schemaMap(parseItemMap(config.getStringList("items")));
        builder.totalDuration(config.getInt("settings.total_duration"));
        builder.shufflingInterval(config.getInt("settings.shuffling_reward.interval"));
        builder.rewardType(parseRewardType(config.getString("settings.static_reward.type")));
        return builder.build();
    }

    private Map<Character, String> parseItemMap(List<String> items) {
        Map<Character, String> itemMap = new HashMap<>();
        for (String item : items) {
            String[] parts = item.split(";");
            if (parts.length == 2) {
                itemMap.put(parts[0].charAt(0), parts[1]);
            } else {
                log.warn("Invalid item format: {}", item);
            }
        }
        return itemMap;
    }

    private StaticRewardType parseRewardType(String rewardType) {
        try {
            return StaticRewardType.valueOf(rewardType);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Invalid or missing reward type: {}", rewardType, e);
            return StaticRewardType.DEFAULT;
        }
    }

    public void loadFiles() throws AnimationConfigurationException {
        File animationDirectory = new File(plugin.getDataFolder(), "animations");
        ensureDirectoryAndExampleFile(animationDirectory);
        try (Stream<Path> paths = Files.walk(animationDirectory.toPath())) {
            paths.filter(Files::isRegularFile).forEach(this::processFile);
        } catch (IOException e) {
            throw new AnimationConfigurationException("Error loading animation files: ", e);
        }
    }

    private void ensureDirectoryAndExampleFile(File directory) {
        if (!directory.exists()) {
            plugin.saveResource("animations/default.yml", false);
            log.info("Created default animations directory and example file.");
        }
    }

    private void processFile(Path file) {
        String fileName = file.getFileName().toString();
        String baseName = getBaseName(fileName);

        schemaFiles.put(baseName, new FileManager(plugin, fileName, plugin.getDataFolder() + "/animations/"));
        log.info("Loaded animation file: {}", fileName);
    }

    private String getBaseName(String fileName) {
        return fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
    }
}