package net.splodgebox.elitelootbox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.splodgebox.eliteapi.item.ItemBuilder;
import net.splodgebox.eliteapi.xseries.XMaterial;
import net.splodgebox.elitelootbox.models.enums.AnimationType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Lootbox {

    private final String id;

    private final String name;
    private final List<String> lore;
    private final XMaterial material;
    private final int modelData;

    private final AnimationType animationType;
    private final List<LootboxReward> rewards;

    public ItemStack create() {
        return new ItemBuilder(material)
                .name(name)
                .lore(lore)
                .modelData(modelData)
                .setNbtStr("Lootbox", id)
                .build();
    }

}
