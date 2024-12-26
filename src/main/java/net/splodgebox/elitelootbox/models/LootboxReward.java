package net.splodgebox.elitelootbox.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.splodgebox.eliteapi.item.ItemBuilder;
import net.splodgebox.eliteapi.message.Message;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
@AllArgsConstructor
public class LootboxReward {

    @Message(path = "lootbox_reward.display_chance", defaultMessage = "&6Chance: &e{CHANCE}&6%")
    public static String chanceMessage;

    private ItemStack itemStack;
    private double chance;

    public ItemStack createDisplayItem() {
        return new ItemBuilder(itemStack)
                .addLore(chanceMessage, "{CHANCE}", String.valueOf(chance))
                .build();
    }

}
