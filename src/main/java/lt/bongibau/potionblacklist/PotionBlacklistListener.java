package lt.bongibau.potionblacklist;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PotionBlacklistListener implements Listener {

    /**
     * Checks if a player is blacklisted from using potions.
     * @param player The player.
     * @return True if the player is not blacklisted, false otherwise.
     */
    private boolean canUsePotion(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        byte blacklisted = container.getOrDefault(PotionBlacklistPlugin.BLACKLISTED_KEY, PersistentDataType.BYTE, (byte) 0);
        return blacklisted == 0;
    }

    /**
     * Prevents blacklisted players from consuming potions.
     * @param e The event.
     */
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (!e.getItem().getType().equals(Material.POTION)) return;
        if (this.canUsePotion(e.getPlayer())) return;

        e.getPlayer().sendMessage(ChatColor.RED + "You are blacklisted from using potions.");
        e.setCancelled(true);
    }

    /**
     * Prevents blacklisted players from throwing potions.
     * @param e The event.
     */
    @EventHandler
    public void onThrow(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null) return;
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;

        Material type = item.getType();

        if (!type.equals(Material.SPLASH_POTION)
                && !type.equals(Material.LINGERING_POTION)) return;
        if (this.canUsePotion(e.getPlayer())) return;

        e.getPlayer().sendMessage(ChatColor.RED + "You are blacklisted from using potions.");
        e.setCancelled(true);
    }

}
