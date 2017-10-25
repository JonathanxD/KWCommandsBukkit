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
package com.github.jonathanxd.kwcommandsbukkit.common;

import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.reflect.env.ArgumentType;
import com.github.jonathanxd.kwcommands.reflect.env.ConcreteProvider;
import com.github.jonathanxd.kwcommands.reflect.env.ReflectionEnvironment;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public final class CommonArguments {

    private CommonArguments() {
        throw new IllegalArgumentException();
    }

    public static void register(Server server, ReflectionEnvironment reflectionEnvironment) {
        final ArgumentType<Player> playerArgumentType = CommonArguments.getPlayerArgumentType(server);
        final ArgumentType<Plugin> pluginArgumentType = CommonArguments.getPluginArgumentType(server);
        final ArgumentType<Command> commandArgumentType = CommonArguments.getCommandArgumentType(reflectionEnvironment.getManager());

        reflectionEnvironment.registerProvider(new ConcreteProvider(playerArgumentType));
        reflectionEnvironment.registerProvider(new ConcreteProvider(pluginArgumentType));
        reflectionEnvironment.registerProvider(new ConcreteProvider(commandArgumentType));
    }

    public static ArgumentType<Player> getPlayerArgumentType(Server server) {
        return new ArgumentType<>(
                TypeInfo.of(Player.class),
                CommonArguments.getPlayerValidator(server),
                CommonArguments.getPlayerConverter(server),
                CommonArguments.getPlayerPossibilities(server),
                null
        );
    }

    public static Function0<List<String>> getPlayerPossibilities(Server server) {
        return () -> server.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public static Function1<String, Boolean> getPlayerValidator(Server server) {
        return s -> server.getOnlinePlayers().stream()
                .anyMatch(o -> o.getName().equals(s));
    }

    public static Function1<String, Player> getPlayerConverter(Server server) {
        return s -> server.getOnlinePlayers().stream()
                .filter(o -> o.getName().equals(s))
                .findAny()
                .orElse(null);
    }

    public static ArgumentType<Plugin> getPluginArgumentType(Server server) {
        return new ArgumentType<>(
                TypeInfo.of(Plugin.class),
                CommonArguments.getPluginValidator(server),
                CommonArguments.getPluginConverter(server),
                CommonArguments.getPluginPossibilities(server),
                null
        );
    }

    public static Function0<List<String>> getPluginPossibilities(Server server) {
        return () -> Arrays.stream(server.getPluginManager().getPlugins())
                .map(Plugin::getName)
                .collect(Collectors.toList());
    }

    public static Function1<String, Boolean> getPluginValidator(Server server) {
        return s -> Arrays.stream(server.getPluginManager().getPlugins())
                .anyMatch(o -> o.getName().equals(s));
    }

    public static Function1<String, Plugin> getPluginConverter(Server server) {
        return s -> Arrays.stream(server.getPluginManager().getPlugins())
                .filter(o -> o.getName().equals(s))
                .findAny()
                .orElse(null);
    }

    public static ArgumentType<Command> getCommandArgumentType(CommandManager commandManager) {
        return new ArgumentType<>(
                TypeInfo.of(Command.class),
                CommonArguments.getCommandValidator(commandManager),
                CommonArguments.getCommandConverter(commandManager),
                CommonArguments.getCommandPossibilities(commandManager),
                null
        );
    }

    public static Function0<List<String>> getCommandPossibilities(CommandManager commandManager) {
        return () -> commandManager.createListWithCommands().stream()
                .map(c -> c.getName().toString())
                .collect(Collectors.toList());
    }

    public static Function1<String, Boolean> getCommandValidator(CommandManager commandManager) {
        return s -> commandManager.createListWithCommands().stream()
                .anyMatch(c -> c.getName().compareTo(s) == 0);
    }

    public static Function1<String, Command> getCommandConverter(CommandManager commandManager) {
        return s -> commandManager.createListWithCommands().stream()
                .filter(c -> c.getName().compareTo(s) == 0)
                .findAny()
                .orElse(null);
    }
}
