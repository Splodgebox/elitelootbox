package net.splodgebox.elitelootbox.commands;

import net.splodgebox.eliteapi.acf.annotation.CommandAlias;
import net.splodgebox.eliteapi.acf.annotation.CommandPermission;
import net.splodgebox.eliteapi.acf.annotation.Dependency;
import net.splodgebox.eliteapi.acf.annotation.Subcommand;
import net.splodgebox.eliteapi.message.Message;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import net.splodgebox.elitelootbox.menus.LootboxListMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("elitelootbox|lootbox|el")
public class LootboxListCommand extends DefaultCommand {

    @Message(path = "menus.list_lootbox.menu_title", defaultMessage = "&7Viewing lootboxes...")
    private String menuTitle;

    @Dependency
    private LootboxManager lootboxManager;

    @Subcommand("list")
    @CommandPermission("elitelootbox.list")
    public void openListMenu(CommandSender sender) {
        if (!(sender instanceof Player player)) return;

        LootboxListMenu lootboxListMenu = new LootboxListMenu(lootboxManager, menuTitle);

        lootboxListMenu.open(player);
    }

}
