package org.royaldev.royalcommands.rcommands.kick;

import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

import java.util.UUID;

public class Kick {

    private long timestamp;
    private String reason;
    private UUID kicker;
    private UUID kicked;

    public Kick(final long timestamp, final String reason, final UUID kicker, final UUID kicked) {
        this.timestamp = timestamp;
        this.reason = reason;
        this.kicker = kicker;
        this.kicked = kicked;
    }

    public UUID getKicked() {
        return this.kicked;
    }

    public void setKicked(final UUID kicked) {
        this.kicked = kicked;
    }

    public UUID getKicker() {
        return this.kicker;
    }

    public void setKicker(final UUID kicker) {
        this.kicker = kicker;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public void save() {
        final KickHistory kh = new KickHistory(this.getKicked());
        final PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(this.getKicked());
        final String path = "kick_history." + kh.getTotalKicks() + ".";
        pcm.set(path + "kicker", this.getKicker());
        pcm.set(path + "reason", this.getReason());
        pcm.set(path + "timestamp", this.getTimestamp());
        pcm.forceSave();
    }
}
