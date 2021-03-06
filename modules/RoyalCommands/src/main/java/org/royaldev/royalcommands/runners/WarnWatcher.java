/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.runners;

import org.bukkit.OfflinePlayer;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

import java.util.ArrayList;
import java.util.List;

// TODO: Check when warns updated/queried, not in a runnable

public class WarnWatcher implements Runnable {

    private final RoyalCommands plugin;

    public WarnWatcher(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public void run() {
        if (Config.warnExpireTime < 1L) return;
        final OfflinePlayer[] players = this.plugin.getServer().getOfflinePlayers();
        for (final OfflinePlayer p : players) {
            final PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(p);
            if (!pcm.exists()) continue;
            if (pcm.get("warns") == null) continue;
            final List<String> warns = pcm.getStringList("warns");
            final List<String> warnsToRemove = new ArrayList<>();
            if (warns == null) return;
            for (final String s : warns) {
                final String[] reason = s.split("\\u00b5");
                if (reason.length < 2) continue;
                final long timeSet;
                try {
                    timeSet = Long.valueOf(reason[1]);
                } catch (final NumberFormatException e) {
                    continue;
                }
                final long currentTime = System.currentTimeMillis();
                final long timeExpires = timeSet + (Config.warnExpireTime * 1000);
                if (timeExpires <= currentTime) warnsToRemove.add(s);
            }
            warns.removeAll(warnsToRemove);
            pcm.set("warns", warns);
        }
    }
}
