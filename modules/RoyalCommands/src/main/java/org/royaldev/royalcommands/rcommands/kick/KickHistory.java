package org.royaldev.royalcommands.rcommands.kick;

import org.bukkit.configuration.ConfigurationSection;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

import java.util.UUID;

public class KickHistory {

    private final int totalKicks;

    public KickHistory(final ConfigurationSection cs) {
        this.totalKicks = cs.getParent().getKeys(false).size();
    }

    public KickHistory(final UUID uuid) {
        final PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(uuid);
        this.totalKicks = pcm.isSet("kick_history") ? pcm.getConfigurationSection("kick_history").getKeys(false).size() : 0;
    }

    public int getTotalKicks() {
        return this.totalKicks;
    }
}
