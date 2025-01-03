package net.splodgebox.elitelootbox.menus;

import net.splodgebox.eliteapi.chat.Chat;
import net.splodgebox.eliteapi.gui.menu.Button;
import net.splodgebox.eliteapi.gui.menu.types.PagedMenu;
import net.splodgebox.eliteapi.message.Message;
import net.splodgebox.eliteapi.xseries.XSound;
import net.splodgebox.elitelootbox.managers.LootboxRewardManager;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.models.LootboxReward;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class LootboxRewardsMenu extends PagedMenu {

    @Message(path = "menus.list_lootbox_rewards.reward_removed", defaultMessage = "&3&lElite&b&lLootbox&f: &7You have successfully removed a reward from {LOOTBOX}!")
    private static String rewardRemovedMessage;

    private final Lootbox lootbox;
    private final LootboxRewardManager rewardManager;
    private final boolean bonusRewards;

    public LootboxRewardsMenu(Lootbox lootbox, LootboxRewardManager rewardManager, String title, boolean bonusRewards) {
        super(title, 6, new MenuTemplate());
        this.lootbox = lootbox;
        this.rewardManager = rewardManager;
        this.bonusRewards = bonusRewards;

        populateRewards();
    }

    private void populateRewards() {
        List<LootboxReward> rewards = getRewards();
        for (int i = 0; i < rewards.size(); i++) {
            addRewardButton(rewards.get(i), i);
        }
    }

    private List<LootboxReward> getRewards() {
        return bonusRewards ? lootbox.getBonusRewards() : lootbox.getRewards();
    }

    private void addRewardButton(LootboxReward reward, int index) {
        appendButton(new Button(reward.createDisplayItem(), (player, event) -> handleRewardClick(player, event, index)));
    }

    private void handleRewardClick(Player player, InventoryClickEvent event, int index) {
        event.setCancelled(true);

        // TODO: Add ability to change command, chance etc.
        if (event.getClick() == ClickType.SHIFT_LEFT) {
            rewardManager.removeReward(lootbox.getId(), index, bonusRewards);
            Chat.send(player, rewardRemovedMessage, "{LOOTBOX}", lootbox.getName());
            player.closeInventory();
        }
    }

    @Override
    public void onFirstOpen(Player player) {
        playOpenSound(player);
    }

    private void playOpenSound(Player player) {
        XSound.ENTITY_BAT_TAKEOFF.play(player, 1, 10);
    }
}
