package net.splodgebox.elitelootbox.animations;

import lombok.RequiredArgsConstructor;
import net.splodgebox.eliteapi.gui.menu.actions.CloseAction;
import net.splodgebox.elitelootbox.models.Lootbox;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public abstract class Animation {

    private final Plugin plugin;
    private final Lootbox lootbox;
    private final Player player;

    public abstract void setup();
    public abstract BukkitTask start();
    public abstract void stop();
    public abstract CloseAction onClose();

}
