package net.splodgebox.elitelootbox.commands;

import net.splodgebox.eliteapi.acf.annotation.*;
import net.splodgebox.eliteapi.chat.Chat;
import net.splodgebox.eliteapi.message.Message;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import net.splodgebox.elitelootbox.managers.LootboxRewardManager;
import net.splodgebox.elitelootbox.menus.LootboxRewardsMenu;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("elitelootbox|lootbox|el")
public class LootboxListRewardsCommand extends DefaultCommand {

    @Message(path = "menus.list_lootbox_rewards.menu_title", defaultMessage = "&7Viewing Rewards...")
    private static String menuTitle;

    @Dependency
    private LootboxManager lootboxManager;

    @Dependency
    private LootboxRewardManager rewardManager;

    @Subcommand("rewards")
    @CommandPermission("elitelootbox.rewards")
    public void openRewardsMenu(CommandSender sender, String lootboxName, @Optional boolean bonusRewards) {
        if (!(sender instanceof Player player)) return;

        Lootbox lootbox = lootboxManager.getLootbox(lootboxName);
        if (lootbox == null) {
            Chat.send(sender, Messages.lootboxNotFound);
            return;
        }

        LootboxRewardsMenu lootboxRewardsMenu = new LootboxRewardsMenu(lootbox, rewardManager, menuTitle, bonusRewards);
        lootboxRewardsMenu.open(player);
    }

}
