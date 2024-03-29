/*
 *      KWCommandsBukkit - Register KWCommands in Bukkit. <https://github.com/JonathanxD/KWCommandsBukkit>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2021 JonathanxD <jonathan.scripter@programmer.net>
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

import com.github.jonathanxd.iutils.object.Either;
import com.github.jonathanxd.iutils.text.Colors;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.fail.ParseFail;
import com.github.jonathanxd.kwcommands.help.HelpInfoHandler;
import com.github.jonathanxd.kwcommands.information.InformationProviders;
import com.github.jonathanxd.kwcommands.information.InformationProvidersImpl;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.printer.Printer;
import com.github.jonathanxd.kwcommands.processor.CommandProcessor;
import com.github.jonathanxd.kwcommands.processor.CommandResult;
import com.github.jonathanxd.kwcommands.processor.MissingInformationResult;
import com.github.jonathanxd.kwcommands.processor.UnsatisfiedRequirementsResult;
import com.github.jonathanxd.kwcommandsbukkit.info.BukkitHelpInfoHandler;
import com.github.jonathanxd.kwcommandsbukkit.info.BukkitInfo;
import com.github.jonathanxd.kwcommandsbukkit.info.BukkitInformationProvider;
import com.github.jonathanxd.kwcommandsbukkit.service.KWCommandsBukkitService;
import com.github.jonathanxd.kwcommandsbukkit.text.LocalizedSender;
import com.github.jonathanxd.kwcommandsbukkit.util.PrinterUtil;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import kotlin.collections.CollectionsKt;

public final class KWBukkitCommand extends org.bukkit.command.Command implements org.bukkit.command.PluginIdentifiableCommand {
    private final Plugin plugin;
    private final Command command;
    private final Dispatcher dispatcher;

    KWBukkitCommand(Plugin plugin, Command command, Dispatcher dispatcher) {
        super(command.getName(),
                KWCommandsBukkit.LOCALIZER.localize(command.getDescription()),
                KWCommandsBukkit.LOCALIZER.localize(command.getDescription()),
                command.getAlias());
        this.plugin = plugin;
        this.command = command;
        this.dispatcher = dispatcher;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    private String getCommandsString(String[] args) {
        String append = args.length == 0 ? "" : " " + Arrays.stream(args)
                                                            .collect(Collectors.joining(" "));
        return command.getName() + append;
    }

    private Consumer<InformationProviders> registerSender(CommandSender sender) {
        return manager -> {
            manager.registerInformation(BukkitInfo.SENDER_ID, sender, "Dispatcher of command.");
            manager.registerInformation(BukkitInfo.LOCALIZED_SENDER_ID,
                    this.dispatcher.getService().getLocalizedSender(sender),
                    "Localized dispatcher of command.");

            if (sender instanceof Player) {
                manager.registerInformation(BukkitInfo.PLAYER_ID, sender, "Dispatcher of command.");
            }
        };
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String append = args.length == 0 ? "" : " ";
        String commandString = command.getName() +
                append +
                Arrays.stream(args).collect(Collectors.joining(" "));

        if (!this.dispatcher.getCommandManager().isRegistered(this.command, null)) {
            this.dispatcher.getService().unregisterCommand(this.command);
            return false;
        }

        this.dispatcher
                .dispatch(commandString, this.getPlugin(), sender, this.registerSender(sender));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias,
                                    String[] args) throws IllegalArgumentException {
        String commandString = this.getCommandsString(args);
        List<String> complete = this.dispatcher
                .complete(commandString, this.getPlugin(), sender, this.registerSender(sender));

        if (complete.size() == 1 && complete.get(0).equals(" "))
            return super.tabComplete(sender, alias, args);

        if (!complete.isEmpty()) {
            return complete;
        }

        return super.tabComplete(sender, alias, args);
    }

    static final class Dispatcher {
        private static final HelpInfoHandler handler = new BukkitHelpInfoHandler();
        private final Server server;
        private final InformationProviders informationManager = new InformationProvidersImpl();
        private final KWCommandsBukkitService service;


        Dispatcher(Server server, KWCommandsBukkitService service) {
            this.server = server;
            this.service = service;
            this.informationManager
                    .registerInformationProvider(new BukkitInformationProvider(this.server));
            this.informationManager.registerRecommendations(
                    this.getService().getCommandManager(),
                    this.getService().getCommandParser(),
                    this.getService().getCommandDispatcher(),
                    null
            );
        }

        @NotNull
        @Contract(pure = true)
        private static Boolean isError(CommandResult it) {
            return it instanceof UnsatisfiedRequirementsResult || it instanceof MissingInformationResult;
        }

        List<String> complete(String commandString,
                              Plugin owner,
                              CommandSender sender,
                              Consumer<InformationProviders> managerConsumer) {
            try {
                LocalizedSender localizedSender = this.service.getLocalizedSender(sender);
                InformationProviders manager = this.informationManager.copy();

                managerConsumer.accept(manager);

                return this.getService()
                           .getCompletion()
                           .complete(commandString, owner, manager, localizedSender.getLocalizer());
            } catch (Exception e) {
                sender.sendMessage(
                        ChatColor.RED + "Exception during command completion, report to developer (include the server log)");
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        void dispatch(String commandString,
                      Plugin owner,
                      CommandSender sender,
                      Consumer<InformationProviders> managerConsumer) {
            try {
                LocalizedSender localizedSender = this.service.getLocalizedSender(sender);
                Printer redPrinter = PrinterUtil.getPrinter(localizedSender, Colors.RED);

                InformationProviders manager = this.informationManager.copy();

                managerConsumer.accept(manager);

                Either<ParseFail, List<CommandResult>> result =
                        this.getCommandProcessor().parseAndDispatch(commandString, owner, manager,
                                localizedSender.getLocalizer());

                if (result.isLeft()) {
                    this.getHandler().handleFail(result.getLeft(),
                            redPrinter);
                } else {
                    List<CommandResult> commandResults = result.getRight();

                    if (CollectionsKt.any(commandResults, Dispatcher::isError)) {
                        this.getHandler()
                            .handleResults(commandResults,
                                    redPrinter
                            );
                    }
                }

            } catch (Exception e) {
                sender.sendMessage(
                        ChatColor.RED + "Exception during command dispatch, report to developer (include the server log)");
                e.printStackTrace();
            }
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
