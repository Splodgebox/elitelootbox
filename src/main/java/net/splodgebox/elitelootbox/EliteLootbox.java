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