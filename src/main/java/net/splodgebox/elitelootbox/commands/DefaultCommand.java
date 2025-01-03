package net.splodgebox.elitelootbox.commands;

import net.splodgebox.eliteapi.acf.BaseCommand;
import net.splodgebox.eliteapi.acf.annotation.CommandAlias;
import net.splodgebox.eliteapi.acf.annotation.Default;
import net.splodgebox.eliteapi.chat.Chat;
import net.splodgebox.eliteapi.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("elitelootbox|lootbox|el")
public class DefaultCommand extends BaseCommand {

    @Message(path = "commands.help", defaultMessage = "&3&lElite&b&lLootbox\n" +
            "&3Commands:\n" +
            "&b/elitelootbox addreward <lootbox>\n" +
            "\n" +
            "&3Command Aliases: &7elitelootbox, lootbox, el")
    private static String helpMessage;

    @Default
    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(Chat.color(helpMessage));
    }

    /**
     * Method to fetch player from playerName as ACF has an issue with collecting
     * bedrock players names as the validation checks don't allow '.' at the start of the name
     *
     * @param playerName name of the player
     * @return Player object of requested player
     */
    public Player getPlayer(String playerName) {
        return Bukkit.getPlayer(playerName);
    }

}
