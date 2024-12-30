package net.splodgebox.elitelootbox.commands;

import net.splodgebox.eliteapi.acf.annotation.*;
import net.splodgebox.eliteapi.chat.Chat;
import net.splodgebox.eliteapi.item.ItemUtils;
import net.splodgebox.eliteapi.message.Message;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import net.splodgebox.elitelootbox.managers.LootboxRewardManager;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("elitelootbox|lootbox|el")
public class LootboxAddRewardCommand extends DefaultCommand {

    @Message(path = "commands.addreward.reward_added", defaultMessage = "&3&lElite&b&lLootbox&f: &aReward successfully added to lootbox &b{LOOTBOX}&a with a chance of &b{CHANCE}&a!")
    private String rewardAdded;

    @Dependency
    private LootboxManager lootboxManager;

    @Dependency
    private LootboxRewardManager rewardManager;

    @Subcommand("addreward")
    @CommandPermission("elitelootbox.addreward")
    @CommandCompletion("@lootboxes")
    public void addReward(CommandSender sender, String lootbox, double chance, @Optional Boolean giveItem, @Optional String command) {
        if (!(sender instanceof Player player)) {
            Chat.send(sender, Messages.cannotSendFromConsole);
            return;
        }

        Lootbox box = validateLootbox(sender, lootbox);
        if (box == null) {
            return;
        }

        ItemStack item = validatePlayerItem(sender, player);
        if (item == null) {
            return;
        }

        rewardManager.addReward(lootbox, item, chance, command, Boolean.TRUE.equals(giveItem), false);
        Chat.send(sender, rewardAdded, "{LOOTBOX}", lootbox, "{CHANCE}", String.valueOf(chance));
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
