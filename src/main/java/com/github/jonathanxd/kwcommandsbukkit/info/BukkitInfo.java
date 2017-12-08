/*
 *      KWCommandsBukkit - Register KWCommands in Bukkit. <https://github.com/JonathanxD/KWCommandsBukkit>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 JonathanxD <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.jonathanxd.kwcommandsbukkit.info;

import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.kwcommands.information.Information;
import com.github.jonathanxd.kwcommands.manager.CommandManager;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import kotlin.collections.ArraysKt;

public class BukkitInfo {

    public static final TypeInfo<Server> SERVER = TypeInfo.of(Server.class);
    public static final String SERVER_LITER = "org.bukkit.Server";
    public static final Information.Id<Server> SERVER_ID = new Information.Id<>(SERVER, new String[]{"server"});

    public static final TypeInfo<Plugin> PLUGIN = TypeInfo.of(Plugin.class);
    public static final String PLUGIN_LITER = "org.bukkit.plugin.Plugin";
    public static final Information.Id<Plugin> PLUGIN_ID = new Information.Id<>(PLUGIN, new String[0]);

    public static final TypeInfo<Player> PLAYER = TypeInfo.of(Player.class);
    public static final String PLAYER_LITER = "org.bukkit.entity.Player";
    public static final Information.Id<Player> PLAYER_ID = new Information.Id<>(PLAYER, new String[] { "player" });
    public static final Information.Id<Player> PLAYER_SENDER_ID = new Information.Id<>(PLAYER, new String[] { "sender", "player" });

    public static final TypeInfo<CommandSender> SENDER = TypeInfo.of(CommandSender.class);
    public static final String SENDER_LITER = "org.bukkit.command.CommandSender";
    public static final Information.Id<CommandSender> SENDER_ID = new Information.Id<>(SENDER, new String[] { "sender" });

    public static Information<Server> createServerInformation(Server server) {
        return new Information<>(SERVER_ID, server, "Current minecraft server");
    }

    public static Information<Plugin> createPluginInformation(Plugin plugin) {
        return new Information<>(PLUGIN_ID, plugin, "Requested plugin instance");
    }
}
