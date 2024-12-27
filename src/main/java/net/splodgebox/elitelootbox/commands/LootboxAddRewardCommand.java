package net.splodgebox.elitelootbox.commands;

import net.splodgebox.eliteapi.acf.annotation.*;
import net.splodgebox.eliteapi.chat.Chat;
import net.splodgebox.eliteapi.item.ItemUtils;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import net.splodgebox.elitelootbox.managers.LootboxRewardManager;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("elitelootbox|lootbox|el")
public class LootboxAddRewardCommand extends DefaultCommand {

    @Dependency
    private LootboxManager lootboxManager;

    @Dependency
    private LootboxRewardManager rewardManager;

    @Subcommand("addreward")
    @CommandPermission("elitelootbox.addreward")
    @CommandCompletion("@lootboxes")
    public void addReward(CommandSender sender, String lootbox, double chance, @Optional Boolean giveItem, @Optional String command) {
        if (!(sender instanceof Player)) {
            Chat.send(sender, Messages.cannotSendFromConsole);
            return;
        }

        Player player = (Player) sender;

        Lootbox box = validateLootbox(sender, lootbox);
        if (box == null) {
            return;
        }

        ItemStack item = validatePlayerItem(sender, player);
        if (item == null) {
            return;
        }

        rewardManager.addReward(lootbox, item, chance, command, Boolean.TRUE.equals(giveItem));
        Chat.send(sender, Messages.rewardAdded,"{LOOTBOX}", lootbox, "{CHANCE}", String.valueOf(chance));
    }

    private Lootbox validateLootbox(CommandSender sender, String lootboxName) {
        Lootbox box = lootboxManager.getLootbox(lootboxName);
        if (box == null) {
            Chat.send(sender, Messages.lootboxNotFound, "{LOOTBOX}", lootboxName);
        }
        return box;
    }

    private ItemStack validatePlayerItem(CommandSender sender, Player player) {
        ItemStack item = player.getItemInHand();
        if (!ItemUtils.isValid(item)) {
            Chat.send(sender, Messages.itemNull);
            return null;
        }
        return item;
    }
}