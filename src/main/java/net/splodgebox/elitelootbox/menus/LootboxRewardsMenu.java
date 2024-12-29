package net.splodgebox.elitelootbox.menus;

import net.splodgebox.eliteapi.gui.menu.Button;
import net.splodgebox.eliteapi.gui.menu.types.PagedMenu;
import net.splodgebox.eliteapi.xseries.XSound;
import net.splodgebox.elitelootbox.managers.LootboxRewardManager;
import net.splodgebox.elitelootbox.models.Lootbox;
import net.splodgebox.elitelootbox.models.LootboxReward;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LootboxRewardsMenu extends PagedMenu {

    private final Lootbox lootbox;
    private final LootboxRewardManager rewardManager;

    public LootboxRewardsMenu(Lootbox lootbox, LootboxRewardManager rewardManager, String title) {
        super(title, 6, new MenuTemplate());
        this.lootbox = lootbox;
        this.rewardManager = rewardManager;

        populateRewards();
    }

    private void populateRewards() {
        for (int i = 0; i < lootbox.getRewards().size(); i++) {
            LootboxReward reward = lootbox.getRewards().get(i);
            addRewardButton(reward, i);
        }
    }

    private void addRewardButton(LootboxReward reward, int index) {
        appendButton(new Button(reward.createDisplayItem(), (player, event) -> handleRewardClick(event, index)));
    }

    private void handleRewardClick(InventoryClickEvent event, int index) {
        event.setCancelled(true);

        // TODO: Add ability to change command, chance etc.
        if (event.getClick() == ClickType.SHIFT_LEFT) {
            rewardManager.removeReward(lootbox.getId(), index);
            event.getWhoClicked().closeInventory();
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
