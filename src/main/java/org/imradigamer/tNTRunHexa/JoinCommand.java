package org.imradigamer.tNTRunHexa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    private final TNTRunHexa plugin;

    public JoinCommand(TNTRunHexa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command is executed by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(TNTRunHexa.PREFIX + ChatColor.RED + "¡Este comando solo puede ser ejecutado por jugadores!");
            return true;
        }

        Player player = (Player) sender;

        // Check permissions
        if (!player.hasPermission("tntrun.admin")) {
            player.sendMessage(TNTRunHexa.PREFIX + ChatColor.RED + "¡No tienes permiso para ejecutar este comando!");
            return true;
        }

        // Join the player to a game
        plugin.getGameManager().getAvailableGame().joinGame(player);
        player.sendMessage(TNTRunHexa.PREFIX + ChatColor.AQUA + "¡Te has unido a un juego de TNT Run!");

        return true;
    }
}
