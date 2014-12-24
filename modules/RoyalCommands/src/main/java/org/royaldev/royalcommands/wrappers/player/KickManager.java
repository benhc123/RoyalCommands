package org.royaldev.royalcommands.wrappers.player;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;
import org.royaldev.royalcommands.rcommands.kick.Kick;
import org.royaldev.royalcommands.rcommands.kick.KickHistory;

import java.util.UUID;

public class KickManager {

    private final UUID uuid;

    KickManager(final UUID uuid) {
        this.uuid = uuid;
    }

    private String getDisplayName(final UUID uuid) {
        if (uuid == null) return "CONSOLE";
        final Player p = RoyalCommands.getInstance().getServer().getPlayer(uuid);
        return p != null ? p.getDisplayName() : this.getName(uuid);
    }

    private String getName(final UUID uuid) {
        return uuid == null ? "CONSOLE" : RoyalCommands.getInstance().getServer().getOfflinePlayer(uuid).getName();
    }

    public void displayKickMessage(final String message) {
        RoyalCommands.getInstance().getServer().broadcast(message, "rcmds.see.kick");
    }

    public String formatKickMessage(final String reason, final Player kicked, final UUID kicker) {
        if (reason == null || kicked == null) return null;
        //noinspection ConstantConditions
        return RUtils.colorize(Config.inGameKickFormat)
            .replace("{kdispname}", kicked.getDisplayName())
            .replace("{kname}", kicked.getName())
            .replace("{name}", this.getName(kicker))
            .replace("{dispname}", this.getDisplayName(kicker))
            .replace("{reason}", reason);
    }

    public KickHistory getKickHistory(final int index) {
        final PlayerConfiguration pcm = this.getPConfManager();
        if (!pcm.isSet("kick_history." + index)) {
            return null;
        }
        return new KickHistory(this.getPConfManager().getConfigurationSection("kick_history." + index));
    }

    public PlayerConfiguration getPConfManager() {
        return PlayerConfigurationManager.getConfiguration(this.getUUID());
    }

    public Player getPlayer() {
        return RoyalCommands.getInstance().getServer().getPlayer(this.getUUID());
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void kick(final UUID kicker, final String reason) {
        final Player p = this.getPlayer();
        if (p == null) return; // TODO: Throw?
        final Kick kick = new Kick(System.currentTimeMillis(), reason == null ? Config.kickMessage : reason, kicker, this.getUUID());
        this.displayKickMessage(this.formatKickMessage(kick.getReason(), p, kick.getKicker()));
    }

    public void kick(final UUID kicker) {
        this.kick(kicker, null);
    }
}
