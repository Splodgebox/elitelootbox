package net.splodgebox.elitelootbox.commands;

import net.splodgebox.eliteapi.acf.annotation.*;
import net.splodgebox.eliteapi.chat.Chat;
import net.splodgebox.eliteapi.message.Message;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("elitelootbox|lootbox|el")
public class LootboxGiveCommand extends DefaultCommand {

    @Message(
            path = "commands.give.lootbox_given",
            defaultMessage = "&3&lElite&b&lLootbox&f: &7You have given &b{PLAYER}&7 {AMOUNT}x {LOOTBOX}!"
    )
    public String lootboxGiven;

    @Dependency
    private LootboxManager lootboxManager;

    @Subcommand("give")
    @CommandCompletion("@players @lootboxes")
    @CommandPermission("elitelootbox.give")
    public void give(CommandSender sender, String player, String lootbox, @Optional Integer amount) {
        Player target = Bukkit.getPlayer(player);

        if (target == null) {
            Chat.send(sender, Messages.playerNotFound);
            return;
        }

        Lootbox box = lootboxManager.getLootbox(lootbox);
        if (box == null) {
            Chat.send(sender, Messages.lootboxNotFound);
            return;
        }

        int lootboxAmount = (amount != null && amount > 0) ? amount : 1;
        giveLootbox(target, box, lootboxAmount);

        Chat.send(sender, lootboxGiven,
                "{PLAYER}", target.getName(),
                "{LOOTBOX}", lootbox,
                "{AMOUNT}", String.valueOf(lootboxAmount));
    }

    private void giveLootbox(Player target, Lootbox lootbox, int amount) {
        for (int i = 0; i < amount; i++) {
            target.getInventory().addItem(lootbox.create());
        }
    }
}
