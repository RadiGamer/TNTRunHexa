package org.imradigamer.tNTRunHexa;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int MAX_PLAYERS = 20;
    private final TNTRunHexa plugin;
    private final List<Player> players = new ArrayList<>();
    private String arenaWorldName;
    private boolean inProgress = false;

    public Game(TNTRunHexa plugin) {
        this.plugin = plugin;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean containsPlayer(Player player) {
        return players.contains(player);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void joinGame(Player player) {
        if (inProgress) {
            player.sendMessage(TNTRunHexa.PREFIX + ChatColor.RED + "El juego ya ha comenzado.");
            return;
        }

        if (players.contains(player)) {
            player.sendMessage(TNTRunHexa.PREFIX + ChatColor.YELLOW + "Ya estás en la partida.");
            return;
        }

        players.add(player);

        // Teleport player to the arena spawn
        if (arenaWorldName != null) {
            World arenaWorld = Bukkit.getWorld(arenaWorldName);
            if (arenaWorld != null) {
                player.teleport(arenaWorld.getSpawnLocation());
                player.sendMessage(TNTRunHexa.PREFIX + ChatColor.GREEN + "¡Te has unido a la partida!");
            }
        }

        if (players.size() == 2) {
            startCountdown();
        }
    }

    public void playerQuit(Player player) {
        players.remove(player);
        if (players.size() == 1 && inProgress) {
            Player winner = players.get(0);
            winner.sendMessage(TNTRunHexa.PREFIX + ChatColor.GOLD + "¡Felicidades! ¡Ganaste la partida!");
            endGame();
        } else if (players.isEmpty()) {
            endGame();
        }
    }

    public void playerLost(Player player) {
        players.remove(player);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        player.sendMessage(TNTRunHexa.PREFIX + ChatColor.RED + "¡Has perdido!");

        if (players.size() <= 1) {
            endGame();
        }
    }

    private void startCountdown() {
        new BukkitRunnable() {
            int countdown = 10;

            @Override
            public void run() {
                if (countdown > 0) {
                    broadcastMessage(ChatColor.GOLD + "El juego comienza en " + countdown + " segundos.");
                    countdown--;
                } else {
                    startGame();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void startGame() {
        inProgress = true;

        // Duplicate world
        arenaWorldName = "TNTRunArena_" + System.currentTimeMillis();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv clone TNTRunArena " + arenaWorldName);

        World arenaWorld = Bukkit.getWorld(arenaWorldName);
        if (arenaWorld == null) {
            broadcastMessage(ChatColor.RED + "Error al cargar la arena.");
            endGame();
            return;
        }

        // Teleport all players to the arena spawn
        Location spawnLocation = arenaWorld.getSpawnLocation();
        for (Player player : players) {
            player.teleport(spawnLocation);
            player.sendMessage(TNTRunHexa.PREFIX + ChatColor.GREEN + "¡El juego ha comenzado!");
        }
    }

    public void endGame() {
        broadcastMessage(ChatColor.GOLD + "El juego ha terminado.");
        if (arenaWorldName != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv delete " + arenaWorldName);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv confirm");
        }

        players.clear();
        inProgress = false;
        plugin.getGameManager().removeGame(this);
    }

    private void broadcastMessage(String message) {
        for (Player player : players) {
            player.sendMessage(TNTRunHexa.PREFIX + message);
        }
    }
}
