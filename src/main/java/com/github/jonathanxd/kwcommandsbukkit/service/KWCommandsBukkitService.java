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
package com.github.jonathanxd.kwcommandsbukkit.service;

import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.completion.Completion;
import com.github.jonathanxd.kwcommands.dispatch.CommandDispatcher;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.parser.CommandParser;
import com.github.jonathanxd.kwcommands.processor.CommandProcessor;
import com.github.jonathanxd.kwcommands.reflect.env.ReflectionEnvironment;

import org.bukkit.plugin.Plugin;

/**
 * Service to register KWCommands in Bukkit, registered on enable phase.
 *
 * Almost all features of KWCommands can be used, the only known not-supported feature is Regex
 * command name.
 */
public interface KWCommandsBukkitService {
    /**
     * Registers {@code command} for {@code plugin} in Bukkit and in {@link #getCommandManager()}.
     *
     * @param command Command to register.
     * @param plugin  Owner plugin.
     */
    void registerCommand(Command command, Plugin plugin);

    /**
     * Register all commands defined in annotations {@link com.github.jonathanxd.kwcommands.reflect.annotation.Cmd},
     * {@link com.github.jonathanxd.kwcommands.json.CmdJson} and {@link
     * com.github.jonathanxd.kwcommands.json.CmdJsonSupplied} of class of {@code obj}
     *
     * @param obj    Instance of class with command annotations.
     * @param plugin Owner plugin.
     */
    void registerCommands(Object obj, Plugin plugin);

    /**
     * Unregisters the {@code command} in {@link #getCommandManager()} and in Bukkit.
     *
     * @param command Command to unregister.
     */
    void unregisterCommand(Command command);

    /**
     * Gets the command manager used to manage commands.
     *
     * @return Manager used to manage commands.
     */
    CommandManager getCommandManager();

    /**
     * Gets the command processor used to parse and dispatch commands.
     *
     * @return Command processor used to parse and dispatch commands.
     */
    CommandProcessor getCommandProcessor();

    /**
     * Gets the command parser.
     *
     * @return Command parser.
     */
    CommandParser getCommandParser();

    /**
     * Gets the command dispatcher.
     *
     * @return Command dispatcher.
     */
    CommandDispatcher getCommandDispatcher();

    /**
     * Gets the completion helper.
     *
     * @return Completion helper.
     */
    Completion getCompletion();

    /**
     * Gets the reflection environment used to parse annotations.
     *
     * @return Reflection environment used to parse annotations.
     */
    ReflectionEnvironment getReflectionEnvironment();

}
