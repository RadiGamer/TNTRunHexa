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

        // Check if the player is in a game and the game has started
        GameManager gameManager = plugin.getGameManager();
        if (!gameManager.isPlayerInGame(player)) {
            return;
        }

        Game game = gameManager.getGameByPlayer(player);
        if (game == null || !game.isInProgress()) {
            return;
        }

        // Schedule block removal with a delay of 10 ticks
        Block blockBelow = player.getLocation().subtract(0, 1, 0).getBlock();
        Block adjacentBlock = player.getLocation().subtract(0, 1, 0).add(0.3, 0, 0.3).getBlock(); // Slight offset for adjacent block check

        if (blockBelow.getType() == Material.WHITE_WOOL || blockBelow.getType() == Material.LIME_WOOL) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> blockBelow.setType(Material.AIR), 10L);
        }

        if ((adjacentBlock.getType() == Material.WHITE_WOOL || adjacentBlock.getType() == Material.LIME_WOOL) && !adjacentBlock.equals(blockBelow)) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> adjacentBlock.setType(Material.AIR), 10L);
        }

        // Eliminate the player if they fall below Y=45
        if (player.getLocation().getY() < 45) {
            game.playerLost(player);
        }
    }

}
