package org.imradigamer.tNTRunHexa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TNTRunHexa extends JavaPlugin {

    public static final String PREFIX = ChatColor.DARK_AQUA + "[BORREGOS] " + ChatColor.RESET;

    private GameManager gameManager;

    @Override
    public void onEnable() {
        getLogger().info(PREFIX + "El plugin TNTRun se ha activado.");
        this.gameManager = new GameManager(this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getCommand("tntrun").setExecutor(new JoinCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info(PREFIX + "El plugin TNTRun se ha desactivado.");
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}