/*
 * AccountSecurity
 * Copyright (C) 2014  DeprecatedNether
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pw.deprecatednether.security;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventListeners implements Listener {

    private AccountSecurity plugin;

    public EventListeners(AccountSecurity main) {
        plugin = main;
    }

    @EventHandler
    public void login(PlayerLoginEvent e) {

        AccountManager manager = new AccountManager(plugin, e.getPlayer().getUniqueId());

        // Check hostname
        String hostname = manager.getConnectHostname();
        if (hostname != null && !e.getHostname().equals(hostname)) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(ChatColor.DARK_RED + "[AccountSecurity]\n" + ChatColor.RED + "Invalid hostname.");
        }

        // Check IP
        String ip = manager.getClientIP();
        if (ip != null && !e.getAddress().getHostAddress().equals(ip)) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(ChatColor.DARK_RED + "[AccountSecurity]\n" + ChatColor.RED + "Invalid client IP.");
        }
    }
}
