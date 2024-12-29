package net.splodgebox.elitelootbox.menus;

import net.splodgebox.eliteapi.chat.Chat;
import net.splodgebox.eliteapi.gui.menu.Button;
import net.splodgebox.eliteapi.gui.menu.types.PagedMenu;
import net.splodgebox.eliteapi.xseries.XSound;
import net.splodgebox.elitelootbox.managers.LootboxManager;
import net.splodgebox.elitelootbox.models.Lootbox;
import org.bukkit.entity.Player;

public class LootboxListMenu extends PagedMenu {

    private final LootboxManager lootboxManager;

    public LootboxListMenu(LootboxManager lootboxManager, String title) {
        super(Chat.color(title), 6, new MenuTemplate());
        this.lootboxManager = lootboxManager;

        populateMenuWithLootboxes();
    }

    private void populateMenuWithLootboxes() {
        lootboxManager.getLootboxes().values().forEach(this::addLootboxButton);
    }

    private void addLootboxButton(Lootbox lootbox) {
        Button button = new Button(lootbox.create(), (player, event) -> event.setCancelled(true));
        appendButton(button);
    }

    @Override
    public void onFirstOpen(Player player) {
        playOpenSound(player);
    }

    private void playOpenSound(Player player) {
        XSound.ENTITY_BAT_TAKEOFF.play(player, 1, 10);
    }
}
