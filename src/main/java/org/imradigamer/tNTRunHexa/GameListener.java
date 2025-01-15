package org.imradigamer.tNTRunHexa;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameListener implements Listener {
    private final TNTRunHexa plugin;

    public GameListener(TNTRunHexa plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block blockBelow = player.getLocation().subtract(0, 1, 0).getBlock();

        // Remove the block below the player
        if (blockBelow.getType() == Material.WHITE_WOOL || blockBelow.getType() == Material.LIME_WOOL) {
            blockBelow.setType(Material.AIR);
        }

        // Eliminate the player if they fall below Y=45
        if (player.getLocation().getY() < 45) {
            plugin.getGameManager().playerQuit(player);
        }
    }
}
