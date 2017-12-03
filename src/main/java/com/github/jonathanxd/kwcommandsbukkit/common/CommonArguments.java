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

import com.github.jonathanxd.iutils.localization.Locale;
import com.github.jonathanxd.iutils.localization.LocaleManager;
import com.github.jonathanxd.iutils.text.TextComponent;
import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.kwcommands.argument.ArgumentType;
import com.github.jonathanxd.kwcommands.argument.SingleArgumentType;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.parser.Input;
import com.github.jonathanxd.kwcommands.parser.Possibilities;
import com.github.jonathanxd.kwcommands.parser.SingleInput;
import com.github.jonathanxd.kwcommands.parser.Transformer;
import com.github.jonathanxd.kwcommands.parser.Validation;
import com.github.jonathanxd.kwcommands.parser.ValidationKt;
import com.github.jonathanxd.kwcommands.parser.Validator;
import com.github.jonathanxd.kwcommands.reflect.env.ConcreteProvider;
import com.github.jonathanxd.kwcommands.reflect.env.ReflectionEnvironment;
import com.github.jonathanxd.kwcommandsbukkit.Texts;

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

import kotlin.collections.ArraysKt;
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
                CommonArguments.getPlayerConverter(server),
                CommonArguments.getPlayerValidator(server),
                CommonArguments.getPlayerPossibilities(server),
                null,
                TypeInfo.of(Player.class)
        );
    }

    public static Possibilities getPlayerPossibilities(Server server) {
        return new PlayerPossibilitiesFunc(server);
    }

    public static Validator<SingleInput> getPlayerValidator(Server server) {
        return new PlayerValidator(server);
    }

    public static Transformer<SingleInput, Player> getPlayerConverter(Server server) {
        return new PlayerTransformer(server);
    }

    // OfflinePlayer
    public static ArgumentType<SingleInput, OfflinePlayer> getOfflinePlayerArgumentType(Server server) {
        return new SingleArgumentType<>(
                CommonArguments.getOfflinePlayerConverter(server),
                CommonArguments.getOfflinePlayerValidator(server),
                CommonArguments.getOfflinePlayerPossibilities(server),
                null,
                TypeInfo.of(OfflinePlayer.class)
        );
    }

    public static Possibilities getOfflinePlayerPossibilities(Server server) {
        return new OfflinePlayerPossibilitiesFunc(server);
    }

    public static Validator<SingleInput> getOfflinePlayerValidator(Server server) {
        return new OfflinePlayerValidator(server);
    }

    public static Transformer<SingleInput, OfflinePlayer> getOfflinePlayerConverter(Server server) {
        return new OfflinePlayerTransformer(server);
    }

    public static ArgumentType<SingleInput, Plugin> getPluginArgumentType(Server server) {
        return new SingleArgumentType<>(
                CommonArguments.getPluginConverter(server),
                CommonArguments.getPluginValidator(server),
                CommonArguments.getPluginPossibilities(server),
                null,
                TypeInfo.of(Plugin.class)
        );
    }

    public static Possibilities getPluginPossibilities(Server server) {
        return new PluginPossibilitiesFunc(server);
    }

    public static Validator<SingleInput> getPluginValidator(Server server) {
        return new PluginValidator(server);
    }

    public static Transformer<SingleInput, Plugin> getPluginConverter(Server server) {
        return new PluginTransformer(server);
    }

    public static ArgumentType<SingleInput, Command> getCommandArgumentType(CommandManager commandManager) {
        return new SingleArgumentType<>(
                CommonArguments.getCommandConverter(commandManager),
                CommonArguments.getCommandValidator(commandManager),
                CommonArguments.getCommandPossibilities(commandManager),
                null,
                TypeInfo.of(Command.class)
        );
    }

    public static Possibilities getCommandPossibilities(CommandManager commandManager) {
        return new CommandPossibilitiesFunc(commandManager);
    }

    public static Validator<SingleInput> getCommandValidator(CommandManager commandManager) {
        return new CommandValidator(commandManager);
    }

    public static Transformer<SingleInput, Command> getCommandConverter(CommandManager commandManager) {
        return new CommandTransformer(commandManager);
    }

    // Locale
    public static ArgumentType<SingleInput, Locale> getLocaleArgumentType(LocaleManager localeManager) {
        return new SingleArgumentType<>(
                CommonArguments.getLocaleConverter(localeManager),
                CommonArguments.getLocaleValidator(localeManager),
                CommonArguments.getLocalePossibilities(localeManager),
                null,
                TypeInfo.of(Locale.class)
        );
    }

    public static Possibilities getLocalePossibilities(LocaleManager localeManager) {
        return new LocalePossibilitiesFunc(localeManager);
    }

    public static Validator<SingleInput> getLocaleValidator(LocaleManager localeManager) {
        return new LocaleValidator(localeManager);
    }

    public static Transformer<SingleInput, Locale> getLocaleConverter(LocaleManager localeManager) {
        return new LocaleTransformer(localeManager);
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

    private static class PlayerValidator implements Validator<SingleInput> {
        private final Server server;

        public PlayerValidator(Server server) {
            this.server = server;
        }

        @NotNull
        @Override
        public TextComponent getName() {
            return Texts.I.getPlayerValidatorText();
        }

        @NotNull
        @Override
        public Validation invoke(ArgumentType<SingleInput, ?> argumentType, SingleInput singleInput) {
            String inputString = singleInput.getInput();
            boolean any = CollectionsKt.any(server.getOnlinePlayers(), o -> o.getName().equals(inputString));
            return any ? ValidationKt.valid()
                    : ValidationKt.invalid(singleInput, argumentType, this, Texts.I.getInvalidPlayerText());
        }
    }

    private static class PlayerTransformer implements Transformer<SingleInput, Player> {
        private final Server server;

        public PlayerTransformer(Server server) {
            this.server = server;
        }

        @Override
        public Player invoke(SingleInput singleInput) {
            return CollectionsKt.first(server.getOnlinePlayers(),
                    o -> o.getName().equals(singleInput.getInput()));
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

    private static class OfflinePlayerValidator implements Validator<SingleInput> {
        private final Server server;

        public OfflinePlayerValidator(Server server) {
            this.server = server;
        }

        @NotNull
        @Override
        public TextComponent getName() {
            return Texts.I.getAnyPlayerValidatorText();
        }

        @NotNull
        @Override
        public Validation invoke(ArgumentType<SingleInput, ?> argumentType, SingleInput singleInput) {
            String inputString = singleInput.getInput();

            boolean any = CollectionsKt.any(server.getOnlinePlayers(), o -> o.getName().equals(inputString));

            if (any)
                return ValidationKt.valid();

            try {
                any = server.getOfflinePlayer(UUID.fromString(inputString)) != null;
            } catch (IllegalArgumentException ignored) {
                any = false;
            }

            return any
                    ? ValidationKt.valid()
                    : ValidationKt.invalid(singleInput, argumentType, this, Texts.I.getInvalidAnyPlayerText());
        }

    }

    private static class OfflinePlayerTransformer implements Transformer<SingleInput, OfflinePlayer> {
        private final Server server;

        public OfflinePlayerTransformer(Server server) {
            this.server = server;
        }

        @Override
        public OfflinePlayer invoke(SingleInput singleInput) {
            String inputString = singleInput.getInput();

            OfflinePlayer p = CollectionsKt.firstOrNull(server.getOnlinePlayers(), o -> o.getName().equals(inputString));

            if (p != null)
                return p;

            try {
                p = server.getOfflinePlayer(UUID.fromString(inputString));
            } catch (IllegalArgumentException ignored) {
            }

            return Objects.requireNonNull(p);
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

    private static class PluginValidator implements Validator<SingleInput> {
        private final Server server;

        public PluginValidator(Server server) {
            this.server = server;

        }

        @NotNull
        @Override
        public TextComponent getName() {
            return Texts.I.getPluginValidatorText();
        }

        @NotNull
        @Override
        public Validation invoke(ArgumentType<SingleInput, ?> argumentType, SingleInput singleInput) {
            String inputString = singleInput.getInput();

            boolean any = ArraysKt.any(server.getPluginManager().getPlugins(),
                    o -> o.getName().equals(inputString));

            return any
                    ? ValidationKt.valid()
                    : ValidationKt.invalid(singleInput, argumentType, this, Texts.I.getInvalidPluginText());
        }
    }

    private static class PluginTransformer implements Transformer<SingleInput, Plugin> {
        private final Server server;

        public PluginTransformer(Server server) {
            this.server = server;
        }

        @Override
        public Plugin invoke(SingleInput singleInput) {
            String inputString = singleInput.getInput();

            return ArraysKt.first(server.getPluginManager().getPlugins(),
                    o -> o.getName().equals(inputString));
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

    private static class CommandValidator implements Validator<SingleInput> {
        private final CommandManager commandManager;

        public CommandValidator(CommandManager commandManager) {
            this.commandManager = commandManager;
        }

        @NotNull
        @Override
        public TextComponent getName() {
            return Texts.I.getCommandValidatorText();
        }

        @NotNull
        @Override
        public Validation invoke(ArgumentType<SingleInput, ?> argumentType, SingleInput singleInput) {
            String inputString = singleInput.getInput();

            boolean any = CollectionsKt.any(this.commandManager.createListWithCommands(),
                    c -> c.getName().compareTo(inputString) == 0);

            return any
                    ? ValidationKt.valid()
                    : ValidationKt.invalid(singleInput, argumentType, this, Texts.I.getInvalidCommandText());
        }

    }

    private static class CommandTransformer implements Transformer<SingleInput, Command> {
        private final CommandManager commandManager;

        public CommandTransformer(CommandManager commandManager) {
            this.commandManager = commandManager;
        }

        @Override
        public Command invoke(SingleInput singleInput) {
            String inputString = singleInput.getInput();

            return CollectionsKt.first(this.commandManager.createListWithCommands(),
                    c -> c.getName().compareTo(inputString) == 0);
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

    private static class LocaleValidator implements Validator<SingleInput> {
        private final LocaleManager localeManager;

        public LocaleValidator(LocaleManager localeManager) {
            this.localeManager = localeManager;
        }

        @NotNull
        @Override
        public TextComponent getName() {
            return Texts.I.getLocaleValidatorText();
        }

        @NotNull
        @Override
        public Validation invoke(ArgumentType<SingleInput, ?> argumentType, SingleInput singleInput) {
            String inputString = singleInput.getInput();

            return this.localeManager.getLocale(inputString) != null
                    ? ValidationKt.valid()
                    : ValidationKt.invalid(singleInput, argumentType, this, Texts.I.getInvalidLocaleText());
        }

    }

    private static class LocaleTransformer implements Transformer<SingleInput, Locale> {
        private final LocaleManager localeManager;

        public LocaleTransformer(LocaleManager localeManager) {
            this.localeManager = localeManager;
        }

        @Override
        public Locale invoke(SingleInput singleInput) {
            String inputString = singleInput.getInput();

            return this.localeManager.getLocale(inputString);
        }
    }
}
