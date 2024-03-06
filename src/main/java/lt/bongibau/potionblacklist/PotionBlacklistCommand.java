package lt.bongibau.potionblacklist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class PotionBlacklistCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("blacklist")) {
                String playerName = args[1];

                this.blacklist(sender, playerName);
                return true;
            }
        }

        this.usage(sender);

        return true;
    }

    private void usage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: /potion blacklist <player> - Toggle potion blacklist for a player.");
    }

    private void blacklist(CommandSender sender, String playerName) {
        if (!sender.hasPermission("potion.blacklist")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return;
        }

        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        /*
         * Get the player's persistent data container
         * and toggle the blacklisted key.
         */
        PersistentDataContainer container = player.getPersistentDataContainer();
        byte blacklisted = container.getOrDefault(PotionBlacklistPlugin.BLACKLISTED_KEY, PersistentDataType.BYTE, (byte) 0);
        container.set(PotionBlacklistPlugin.BLACKLISTED_KEY, PersistentDataType.BYTE, blacklisted == 0 ? (byte) 1 : (byte) 0);

        if (blacklisted == 0) {
            sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been blacklisted from using potions.");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been removed from the blacklist.");
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 1) {
            return Stream.of("blacklist")
                    .filter(completion -> completion.startsWith(args[0]))
                    .toList();
        }

        if (args[0].equalsIgnoreCase("blacklist") && args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.startsWith(args[1]))
                    .toList();
        }

        return List.of();
    }
}
