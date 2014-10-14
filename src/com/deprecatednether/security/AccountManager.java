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

package com.deprecatednether.security;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AccountManager {

    private UUID player;
    private AccountSecurity plugin;
    FileConfiguration data;

    public AccountManager(AccountSecurity main, UUID uuid) {
        player = uuid;
        plugin = main;
        data = loadConfiguration();
    }

    private FileConfiguration loadConfiguration() {
        File folder = new File(plugin.getDataFolder(), "userdata");
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        File file = new File(folder, player.toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                plugin.getLogger().severe("Couldn't create userdata file for (UUID) " + player.toString());
                ioe.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    private void saveConfiguration() {
        try {
            data.save(new File(new File(plugin.getDataFolder(), "userdata"), player.toString() + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the hostname the player must connect with.
     * @return The hostname. Null if none specified.
     */
    public String getConnectHostname() {
        return data.getString("hostname");
    }

    /**
     * Set a hostname the player has to connect with.
     * @param hostname The hostname. If null, this check is disabled.
     */
    public void setConnectHostname(String hostname) {
        data.set("hostname", hostname);
        saveConfiguration();
    }

    /**
     * Get the IP address the client has to connect from.
     * @return The IP. If null, this check is disabled.
     */
    public String getClientIP() {
        return data.getString("ip-address");
    }

    /**
     * Set the IP the player has to connect with.
     * @param address The IP address.
     */
    public void setClientIP(String address) {
        data.set("ip-address", address);
        saveConfiguration();
    }
}
