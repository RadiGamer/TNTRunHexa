package org.imradigamer.tNTRunHexa;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final TNTRunHexa plugin;
    private final List<Game> activeGames = new ArrayList<>();

    public GameManager(TNTRunHexa plugin) {
        this.plugin = plugin;
    }

    public Game getAvailableGame() {
        for (Game game : activeGames) {
            if (!game.isInProgress() && game.getPlayerCount() < Game.MAX_PLAYERS) {
                return game;
            }
        }

        Game newGame = new Game(plugin);
        activeGames.add(newGame);
        return newGame;
    }

    public void removeGame(Game game) {
        activeGames.remove(game);
    }

    public void playerQuit(Player player) {
        for (Game game : activeGames) {
            if (game.containsPlayer(player)) {
                game.playerQuit(player);
                if (game.isEmpty()) {
                    game.endGame();
                    removeGame(game);
                }
                return;
            }
        }
    }

    public boolean isPlayerInGame(Player player) {
        for (Game game : activeGames) {
            if (game.containsPlayer(player)) {
                return true;
            }
        }
        return false;
    }
    public Game getGameByPlayer(Player player) {
        for (Game game : activeGames) {
            if (game.containsPlayer(player)) {
                return game;
            }
        }
        return null;
    }

}
