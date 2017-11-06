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
package com.github.jonathanxd.kwcommandsbukkit;

import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.command.CommandName;
import com.github.jonathanxd.kwcommands.exception.CommandException;
import com.github.jonathanxd.kwcommands.exception.CommandNotFoundException;
import com.github.jonathanxd.kwcommands.help.HelpInfoHandler;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.manager.InformationManager;
import com.github.jonathanxd.kwcommands.manager.InformationManagerImpl;
import com.github.jonathanxd.kwcommands.processor.CommandProcessor;
import com.github.jonathanxd.kwcommands.processor.CommandResult;
import com.github.jonathanxd.kwcommands.processor.MissingInformationResult;
import com.github.jonathanxd.kwcommands.processor.UnsatisfiedRequirementsResult;
import com.github.jonathanxd.kwcommandsbukkit.completion.CommandSuggestionHelper;
import com.github.jonathanxd.kwcommandsbukkit.info.BukkitHelpInfoHandler;
import com.github.jonathanxd.kwcommandsbukkit.info.BukkitInfo;
import com.github.jonathanxd.kwcommandsbukkit.info.BukkitInformationProvider;
import com.github.jonathanxd.kwcommandsbukkit.service.KWCommandsBukkitService;
import com.github.jonathanxd.kwcommandsbukkit.util.CommandStringUtil;
import com.github.jonathanxd.kwcommandsbukkit.util.PrinterUtil;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import kotlin.collections.CollectionsKt;

public final class KWBukkitCommand extends org.bukkit.command.Command implements org.bukkit.command.PluginIdentifiableCommand {
    private final Plugin plugin;
    private final Command command;
    private final Dispatcher dispatcher;

    KWBukkitCommand(Plugin plugin, Command command, Dispatcher dispatcher) {
        super(command.getName().toString(), command.getDescription(), command.getDescription(),
                CollectionsKt.map(command.getAlias(), CommandName::toString));
        this.plugin = plugin;
        this.command = command;
        this.dispatcher = dispatcher;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String append = args.length == 0 ? "" : " ";
        String collect = command.getName().toString() +
                append +
                Arrays.stream(args).collect(Collectors.joining(" "));

        if (!this.dispatcher.getCommandManager().isRegistered(this.command, null)) {
            this.dispatcher.getService().unregisterCommand(this.command);
            return false;
        }

        List<String> commandString = CommandStringUtil.getCommandStringListFromMessage(collect);

        this.dispatcher.dispatch(commandString, sender, manager -> {
            manager.registerInformation(BukkitInfo.SENDER_ID, sender, "Dispatcher of command.");

            if (sender instanceof Player) {
                manager.registerInformation(BukkitInfo.PLAYER_ID, sender, "Dispatcher of command.");
            }
        });

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        CommandSuggestionHelper suggestionHelper = this.dispatcher.getService().getSuggestionHelper();

        List<String> suggestions = suggestionHelper.getSuggestionsFor(sender, this.dispatcher.getCommandManager(),
                this.command.getName().toString(),
                args,
                true);

        if (!suggestions.isEmpty()) {
            return suggestions;
        }

        return super.tabComplete(sender, alias, args);
    }

    static final class Dispatcher {
        private static final HelpInfoHandler handler = new BukkitHelpInfoHandler();
        private final Server server;
        private final InformationManager informationManager = new InformationManagerImpl();
        private final KWCommandsBukkitService service;

        Dispatcher(Server server, KWCommandsBukkitService service) {
            this.server = server;
            this.service = service;
            PrinterUtil.getGreenPrinter(server.getConsoleSender());
            PrinterUtil.getRedPrinter(server.getConsoleSender());
            this.informationManager.registerInformationProvider(new BukkitInformationProvider(this.server));
        }

        @NotNull
        @Contract(pure = true)
        private static Boolean isError(CommandResult it) {
            return it instanceof UnsatisfiedRequirementsResult || it instanceof MissingInformationResult;
        }

        private boolean dispatch(List<String> commandString,
                                 CommandSender sender,
                                 Consumer<InformationManager> managerConsumer) {
            try {
                InformationManager manager = this.informationManager.copy();

                managerConsumer.accept(manager);

                List<CommandResult> commandResults = this.getCommandProcessor().processAndHandleWithOwnerFunc(commandString, name -> {
                    String ownerPlugin = CommandStringUtil.getCommandOwnerPlugin(name);

                    if (ownerPlugin != null)
                        return this.server.getPluginManager().getPlugin(ownerPlugin);

                    return null;
                }, manager);

                if (CollectionsKt.any(commandResults, Dispatcher::isError)) {
                    this.getHandler().handleResults(commandResults, PrinterUtil.getRedPrinter(sender));
                    return false;
                }

                return true;
            } catch (CommandNotFoundException e) {
                sender.sendMessage(ChatColor.RED + "Unexpected fail. Command not found: " + e.getCommandStr());
                e.printStackTrace();
                return false;
            } catch (CommandException ex) {
                this.getHandler().handleCommandException(ex, PrinterUtil.getRedPrinter(sender));
            }
            return false;
        }

        private HelpInfoHandler getHandler() {
            return Dispatcher.handler;
        }

        private Server getServer() {
            return this.server;
        }

        public KWCommandsBukkitService getService() {
            return this.service;
        }

        private CommandManager getCommandManager() {
            return this.service.getCommandManager();
        }

        private CommandProcessor getCommandProcessor() {
            return this.service.getCommandProcessor();
        }

    }
}
