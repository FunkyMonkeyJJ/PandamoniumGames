package General;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Collection;

public abstract class PandamoniumGame {
    protected Collection<GamePlayer> gamePlayers;
    protected Listener gameListener;
    protected boolean teamBased;

    protected PandamoniumGame(Collection<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public abstract void start(
            final Collection<GamePlayer> startingGamePlayers);

    public abstract void end(final Collection<GamePlayer> winningPlayers);

    public abstract boolean checkGamePlayers();

    public final void setGameListener(Listener gameListener) {
        this.gameListener = gameListener;
        Bukkit.getPluginManager().registerEvents(gameListener, Game.plugin);
    }
}
