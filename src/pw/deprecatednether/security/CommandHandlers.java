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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class CommandHandlers implements CommandExecutor {
    private AccountSecurity plugin;

    public CommandHandlers(AccountSecurity main) {
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!sender.hasPermission("accountsecurity.command")) {
            sender.sendMessage("No permission");
            return false;
        }
        if (args.length == 0) { // Show a nice help menu
            sender.sendMessage(ChatColor.DARK_AQUA + "AccountSecurity: " + ChatColor.RED + "Commands list");
            boolean hasAccess = false;
            if (plugin.getConfig().getBoolean("checks.hostname") && sender.hasPermission("accountsecurity.hostname")) {
                sender.sendMessage(ChatColor.DARK_AQUA + "/" + lbl + " hostname" + ChatColor.GREEN + " toggles hostname security.");
                hasAccess = true;
            }
            if (plugin.getConfig().getBoolean("checks.ip") && sender.hasPermission("accountsecurity.ip")) {
                sender.sendMessage(ChatColor.DARK_AQUA + "/" + lbl + " ip" + ChatColor.GREEN + " toggles IP security.");
                hasAccess = true;
            }
            if (hasAccess) {
                sender.sendMessage(ChatColor.DARK_AQUA + "/" + lbl + " help <feature>" + ChatColor.GREEN + " gives you an explanation of what each feature does.");
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have access to any AccountSecurity commands.");
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
            if (args[0].equalsIgnoreCase("ip")) {
                sender.sendMessage(ChatColor.GREEN + "IP security requires you to always connect with the same IP address.");
                sender.sendMessage(ChatColor.GREEN + "While this makes it impossible for anybody except you (or somebody using your internet connection) to connect to the server, a lot of internet server providers change their users' IPs periodically, so you may find yourself locked out of the account if you don't have a static IP. Use with caution!");
            }
            else if (args[0].equalsIgnoreCase("hostname")) {
                sender.sendMessage(ChatColor.GREEN + "This generates you a random hostname (like abcd1234.mc.example.com) to use to connect to the server.");
                sender.sendMessage(ChatColor.GREEN + "You'll be able to connect from any computer on any network as long as you use the random hostname every time to log in.");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Invalid feature - use either 'hostname' or 'help'.");
            }
            sender.sendMessage(ChatColor.GREEN + "If you ever get locked out of your account, you may contact an administrator to disable the check.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player!");
            return true;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("ip")) {
            AccountManager manager = new AccountManager(plugin, player.getUniqueId());
            boolean enable = manager.getClientIP() == null ? true : false;
            String ip = enable ? player.getAddress().getAddress().getHostAddress() : null;
            manager.setClientIP(ip);
            player.sendMessage(ChatColor.DARK_AQUA + "AccountSecurity: " + ChatColor.GREEN + (enable ? "Your account can now only connect from this IP address (" + ip + "). To be able to connect from other IP addresses, issue the command (/" + lbl + " ip) again." : "You can now connect from any IP address again."));
        }
        if (args[0].equalsIgnoreCase("hostname")) {
            AccountManager manager = new AccountManager(plugin, player.getUniqueId());
            boolean enable = manager.getConnectHostname() == null ? true : false;
            String sub = null;
            if (enable) {
                sub = "";
                String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
                Random random = new Random();
                for (int i = 0; i < 8; i++) {
                    sub = sub + chars.charAt(random.nextInt(7));
                }
                sub = sub + "." + plugin.getConfig().getString("base-hostname");
            }
            manager.setConnectHostname(sub);
            player.sendMessage(ChatColor.DARK_AQUA + "AccountSecurity: " + ChatColor.GREEN + (enable ? "You should now use " + sub + " to connect to the server." : "You can now connect with any hostname again."));
        }
        return false;
    }
}
