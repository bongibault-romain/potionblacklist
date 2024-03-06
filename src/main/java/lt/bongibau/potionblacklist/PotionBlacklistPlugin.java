package lt.bongibau.potionblacklist;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class PotionBlacklistPlugin extends JavaPlugin {

    public static NamespacedKey BLACKLISTED_KEY;

    @Override
    public void onEnable() {
        BLACKLISTED_KEY = new NamespacedKey(this, "blacklisted");

        /*
         * Register the potion command.
         */
        PluginCommand command = this.getCommand("potion");

        if (command == null) {
            this.getLogger().severe("Unable to register potion command.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        PotionBlacklistCommand potionCommand = new PotionBlacklistCommand();

        command.setExecutor(potionCommand);
        command.setTabCompleter(potionCommand);

        /*
         * Register the potion blacklist listener.
         */
        PotionBlacklistListener listener = new PotionBlacklistListener();
        Bukkit.getPluginManager().registerEvents(listener, this);

        /*
         * Register the potion blacklist command permission.
         */
        Permission permission = new Permission(
                "potion.blacklist",
                "Allows a player to blacklist other players from using potions.",
                PermissionDefault.OP
        );
        Bukkit.getPluginManager().addPermission(permission);
    }

    @Override
    public void onDisable() {
        BLACKLISTED_KEY = null;
    }
}
