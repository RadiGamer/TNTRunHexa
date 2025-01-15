package org.imradigamer.tNTRunHexa;

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
        if (!(sender instanceof Player)) {
            sender.sendMessage(TNTRunHexa.PREFIX + ChatColor.RED + "¡Solo los jugadores pueden usar este comando!");
            return true;
        }

        Player player = (Player) sender;
        plugin.getGameManager().getAvailableGame().joinGame(player);
        return true;
    }
}
