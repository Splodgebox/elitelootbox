package net.splodgebox.elitelootbox.menus;

import net.splodgebox.eliteapi.gui.menu.Button;
import net.splodgebox.eliteapi.gui.menu.Template;
import net.splodgebox.eliteapi.gui.menu.types.PagedMenu;
import net.splodgebox.eliteapi.item.ItemBuilder;
import net.splodgebox.eliteapi.xseries.XMaterial;
import net.splodgebox.elitelootbox.utils.MenuUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuTemplate extends Template {

    @Override
    public void set(PagedMenu pagedMenu) {
        setBorder(pagedMenu);
        addNavigationButtons(pagedMenu);
    }

    private void setBorder(PagedMenu pagedMenu) {
        List<Integer> borderSlots = MenuUtils.calculateBorderSlots(pagedMenu.getRows(), 9);

        borderSlots.forEach(slot -> pagedMenu.setButton(slot, createBorderButton()));
    }

    private Button createBorderButton() {
        return new Button(getBorderPane(), (player, inventoryClickEvent) -> {
            inventoryClickEvent.setCancelled(true);
        });
    }

    private ItemStack getBorderPane() {
        return new ItemBuilder(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .build();
    }

    private void addNavigationButtons(PagedMenu pagedMenu) {
        pagedMenu.addNextButton();
        pagedMenu.addPreviousButton();
    }
}
