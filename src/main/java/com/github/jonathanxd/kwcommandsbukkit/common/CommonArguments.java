/*
 *      KWCommandsBukkit - Register KWCommands in Bukkit. <https://github.com/JonathanxD/KWCommandsBukkit>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2018 JonathanxD <jonathan.scripter@programmer.net>
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

import com.github.jonathanxd.iutils.localization.Locale;
import com.github.jonathanxd.iutils.localization.LocaleManager;
import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.kwcommands.argument.ArgumentType;
import com.github.jonathanxd.kwcommands.argument.SingleArgumentType;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.parser.ArgumentParser;
import com.github.jonathanxd.kwcommands.parser.Input;
import com.github.jonathanxd.kwcommands.parser.Possibilities;
import com.github.jonathanxd.kwcommands.parser.SingleInput;
import com.github.jonathanxd.kwcommands.parser.ValueOrValidation;
import com.github.jonathanxd.kwcommands.parser.ValueOrValidationFactory;
import com.github.jonathanxd.kwcommands.reflect.env.ConcreteProvider;
import com.github.jonathanxd.kwcommands.reflect.env.ReflectionEnvironment;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import kotlin.collections.CollectionsKt;

public final class CommonArguments {

    private CommonArguments() {
        throw new IllegalArgumentException();
    }

    public static void register(Server server,
                                ReflectionEnvironment reflectionEnvironment,
                                LocaleManager localeManager) {
        final ArgumentType<SingleInput, Player> playerArgumentType =
                CommonArguments.getPlayerArgumentType(server);

        final ArgumentType<SingleInput, OfflinePlayer> offlinePlayerArgumentType =
                CommonArguments.getOfflinePlayerArgumentType(server);

        final ArgumentType<SingleInput, Plugin> pluginArgumentType =
                CommonArguments.getPluginArgumentType(server);

        final ArgumentType<SingleInput, Command> commandArgumentType =
                CommonArguments.getCommandArgumentType(reflectionEnvironment.getManager());

        final ArgumentType<SingleInput, Locale> localeArgumentType =
                CommonArguments.getLocaleArgumentType(localeManager);

        reflectionEnvironment.registerProvider(new ConcreteProvider(playerArgumentType));
        reflectionEnvironment.registerProvider(new ConcreteProvider(offlinePlayerArgumentType));
        reflectionEnvironment.registerProvider(new ConcreteProvider(pluginArgumentType));
        reflectionEnvironment.registerProvider(new ConcreteProvider(commandArgumentType));
        reflectionEnvironment.registerProvider(new ConcreteProvider(localeArgumentType));
    }

    // Player
    public static ArgumentType<SingleInput, Player> getPlayerArgumentType(Server server) {
        return new SingleArgumentType<>(
                CommonArguments.getPlayerParser(server),
                CommonArguments.getPlayerPossibilities(server),
                null,
                TypeInfo.of(Player.class)
        );
    }

    public static Possibilities getPlayerPossibilities(Server server) {
        return new PlayerPossibilitiesFunc(server);
    }

    public static ArgumentParser<SingleInput, Player> getPlayerParser(Server server) {
        return new PlayerParser(server);
    }

    // OfflinePlayer
    public static ArgumentType<SingleInput, OfflinePlayer> getOfflinePlayerArgumentType(Server server) {
        return new SingleArgumentType<>(
                CommonArguments.getOfflinePlayerParser(server),
                CommonArguments.getOfflinePlayerPossibilities(server),
                null,
                TypeInfo.of(OfflinePlayer.class)
        );
    }

    public static Possibilities getOfflinePlayerPossibilities(Server server) {
        return new OfflinePlayerPossibilitiesFunc(server);
    }

    public static ArgumentParser<SingleInput, OfflinePlayer> getOfflinePlayerParser(Server server) {
        return new OfflinePlayerParser(server);
    }

    public static ArgumentType<SingleInput, Plugin> getPluginArgumentType(Server server) {
        return new SingleArgumentType<>(
                CommonArguments.getPluginParser(server),
                CommonArguments.getPluginPossibilities(server),
                null,
                TypeInfo.of(Plugin.class)
        );
    }

    public static Possibilities getPluginPossibilities(Server server) {
        return new PluginPossibilitiesFunc(server);
    }

    public static ArgumentParser<SingleInput, Plugin> getPluginParser(Server server) {
        return new PluginParser(server);
    }

    public static ArgumentType<SingleInput, Command> getCommandArgumentType(CommandManager commandManager) {
        return new SingleArgumentType<>(
                CommonArguments.getCommandParser(commandManager),
                CommonArguments.getCommandPossibilities(commandManager),
                null,
                TypeInfo.of(Command.class)
        );
    }

    public static Possibilities getCommandPossibilities(CommandManager commandManager) {
        return new CommandPossibilitiesFunc(commandManager);
    }

    public static ArgumentParser<SingleInput, Command> getCommandParser(CommandManager commandManager) {
        return new CommandParser(commandManager);
    }

    // Locale
    public static ArgumentType<SingleInput, Locale> getLocaleArgumentType(LocaleManager localeManager) {
        return new SingleArgumentType<>(
                CommonArguments.getLocaleParser(localeManager),
                CommonArguments.getLocalePossibilities(localeManager),
                null,
                TypeInfo.of(Locale.class)
        );
    }

    public static Possibilities getLocalePossibilities(LocaleManager localeManager) {
        return new LocalePossibilitiesFunc(localeManager);
    }

    public static ArgumentParser<SingleInput, Locale> getLocaleParser(LocaleManager localeManager) {
        return new LocaleParser(localeManager);
    }


    // Player
    private static class PlayerPossibilitiesFunc implements Possibilities {
        private final Server server;

        public PlayerPossibilitiesFunc(Server server) {
            this.server = server;
        }

        @NotNull
        @Override
        public List<Input> invoke() {
            return server.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .map(SingleInput::new)
                    .collect(Collectors.toList());
        }

    }

    private static class PlayerParser implements ArgumentParser<SingleInput, Player> {

        private final Server server;

        public PlayerParser(Server server) {
            this.server = server;
        }

        @NotNull
        @Override
        public ValueOrValidation<Player> parse(SingleInput singleInput,
                                               ValueOrValidationFactory valueOrValidationFactory) {
            String input = singleInput.getInput();


            Player player = CollectionsKt.firstOrNull(this.server.getOnlinePlayers(), o -> Objects.equals(o.getName(), input));

            return player == null
                    ? valueOrValidationFactory.invalid()
                    : valueOrValidationFactory.value(player);
        }
    }

    // OfflinePlayer
    private static class OfflinePlayerPossibilitiesFunc implements Possibilities {
        private final Server server;

        public OfflinePlayerPossibilitiesFunc(Server server) {
            this.server = server;
        }

        @NotNull
        @Override
        public List<Input> invoke() {
            //server.getOfflinePlayers() --> Not a good idea
            return Collections.emptyList();
        }
    }

    private static class OfflinePlayerParser implements ArgumentParser<SingleInput, OfflinePlayer> {
        private final Server server;

        public OfflinePlayerParser(Server server) {
            this.server = server;
        }

        @NotNull
        @Override
        public ValueOrValidation<OfflinePlayer> parse(SingleInput singleInput, ValueOrValidationFactory valueOrValidationFactory) {
            String inputString = singleInput.getInput();

            OfflinePlayer p = CollectionsKt.firstOrNull(server.getOnlinePlayers(), o -> o.getName().equals(inputString));

            if (p != null)
                return valueOrValidationFactory.value(p);

            try {
                p = server.getOfflinePlayer(UUID.fromString(inputString));
            } catch (IllegalArgumentException ignored) {
            }

            return p != null
                    ? valueOrValidationFactory.value(p)
                    : valueOrValidationFactory.invalid();
        }
    }

    // Plugin

    private static class PluginPossibilitiesFunc implements Possibilities {
        private final Server server;

        public PluginPossibilitiesFunc(Server server) {
            this.server = server;
        }

        @NotNull
        @Override
        public List<Input> invoke() {
            return Arrays.stream(server.getPluginManager().getPlugins())
                    .map(Plugin::getName)
                    .map(SingleInput::new)
                    .collect(Collectors.toList());
        }
    }

    private static class PluginParser implements ArgumentParser<SingleInput, Plugin> {

        private final Server server;

        public PluginParser(Server server) {
            this.server = server;
        }

        @NotNull
        @Override
        public ValueOrValidation<Plugin> parse(SingleInput singleInput,
                                               ValueOrValidationFactory valueOrValidationFactory) {
            String input = singleInput.getInput();

            Plugin plugin = this.server.getPluginManager().getPlugin(input);

            return plugin == null
                    ? valueOrValidationFactory.invalid()
                    : valueOrValidationFactory.value(plugin);
        }
    }

    // Command

    private static class CommandPossibilitiesFunc implements Possibilities {
        private final CommandManager commandManager;

        public CommandPossibilitiesFunc(CommandManager commandManager) {
            this.commandManager = commandManager;
        }

        @NotNull
        @Override
        public List<Input> invoke() {
            return CollectionsKt.map(this.commandManager.createListWithCommands(),
                    c -> new SingleInput(c.getName()));
        }

    }

    private static class CommandParser implements ArgumentParser<SingleInput, Command> {
        private final CommandManager commandManager;

        public CommandParser(CommandManager commandManager) {
            this.commandManager = commandManager;
        }

        @NotNull
        @Override
        public ValueOrValidation<Command> parse(SingleInput singleInput, ValueOrValidationFactory valueOrValidationFactory) {
            String inputString = singleInput.getInput();

            Command command = CollectionsKt.firstOrNull(this.commandManager.createListWithAllCommands(),
                    c -> c.getFullname().compareTo(inputString) == 0);

            return command != null
                    ? valueOrValidationFactory.value(command)
                    : valueOrValidationFactory.invalid();
        }

    }

    // Locale

    private static class LocalePossibilitiesFunc implements Possibilities {
        private final LocaleManager localeManager;

        public LocalePossibilitiesFunc(LocaleManager localeManager) {
            this.localeManager = localeManager;
        }

        @NotNull
        @Override
        public List<Input> invoke() {
            return CollectionsKt.map(this.localeManager.getLocales(),
                    c -> new SingleInput(c.getName()));
        }

    }

    private static class LocaleParser implements ArgumentParser<SingleInput, Locale> {
        private final LocaleManager localeManager;

        public LocaleParser(LocaleManager localeManager) {
            this.localeManager = localeManager;
        }

        @NotNull
        @Override
        public ValueOrValidation<Locale> parse(SingleInput singleInput, ValueOrValidationFactory valueOrValidationFactory) {
            String inputString = singleInput.getInput();

            Locale locale = this.localeManager.getLocale(inputString);

            return locale != null
                    ? valueOrValidationFactory.value(locale)
                    : valueOrValidationFactory.invalid();
        }

    }
}
