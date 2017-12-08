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

import com.github.jonathanxd.iutils.localization.LocaleManager;
import com.github.jonathanxd.iutils.reflection.Reflection;
import com.github.jonathanxd.iutils.text.converter.FastTextLocalizer;
import com.github.jonathanxd.iutils.text.converter.TextLocalizer;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.completion.Completion;
import com.github.jonathanxd.kwcommands.completion.CompletionImpl;
import com.github.jonathanxd.kwcommands.dispatch.CommandDispatcher;
import com.github.jonathanxd.kwcommands.dispatch.CommandDispatcherImpl;
import com.github.jonathanxd.kwcommands.json.DefaultJsonParser;
import com.github.jonathanxd.kwcommands.json.JsonCommandParser;
import com.github.jonathanxd.kwcommands.json.MapTypeResolver;
import com.github.jonathanxd.kwcommands.json.TypeResolverKt;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.manager.CommandManagerImpl;
import com.github.jonathanxd.kwcommands.manager.InstanceProvider;
import com.github.jonathanxd.kwcommands.parser.CommandParser;
import com.github.jonathanxd.kwcommands.parser.CommandParserImpl;
import com.github.jonathanxd.kwcommands.processor.CommandProcessor;
import com.github.jonathanxd.kwcommands.processor.Processors;
import com.github.jonathanxd.kwcommands.reflect.env.ReflectionEnvironment;
import com.github.jonathanxd.kwcommands.util.KLocale;
import com.github.jonathanxd.kwcommandsbukkit.common.CommonArguments;
import com.github.jonathanxd.kwcommandsbukkit.common.CommonTypes;
import com.github.jonathanxd.kwcommandsbukkit.service.KWCommandsBukkitService;
import com.github.jonathanxd.kwcommandsbukkit.text.ColoredLocalizer;
import com.github.jonathanxd.kwcommandsbukkit.util.CommandMapHelper;

import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Paths;

public class KWCommandsBukkitPlugin extends JavaPlugin {

    public static final LocaleManager LOCALE_MANAGER = KLocale.INSTANCE.getLocaleManager();
    public static final TextLocalizer LOCALIZER = new ColoredLocalizer(KLocale.INSTANCE.getLocalizer());

    static {
        KLocale.INSTANCE.setLocalizer(LOCALIZER);
        KLocale.INSTANCE.getDefaultLocale().load(
                Paths.get("kwbukkit/lang/"),
                null,
                KWCommandsBukkitPlugin.class.getClassLoader());
        KLocale.INSTANCE.getPtBr().load(
                Paths.get("kwbukkit/lang/"),
                null,
                KWCommandsBukkitPlugin.class.getClassLoader());
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Enabling KWCommandsBukkit");

        this.getServer().getServicesManager()
                .register(KWCommandsBukkitService.class,
                        new KWCommandsBukkitServiceImpl(this.getServer()),
                        this,
                        ServicePriority.Normal);


        this.getLogger().info("Enabled KWCommandsBukkit");

        KWCommandsBukkitService service = this.getServer().getServicesManager().load(KWCommandsBukkitService.class);

        service.registerCommands(new KWCommandsBukkitCommand(), this);
    }

    private final static class KWCommandsBukkitServiceImpl implements KWCommandsBukkitService {
        private final Server server;
        private final CommandMap commandMap;
        private final KWBukkitCommand.Dispatcher dispatcher;
        private final CommandManager commandManager;
        private final CommandProcessor commandProcessor;
        private final CommandParser commandParser;
        private final CommandDispatcher commandDispatcher;
        private final Completion completion;
        private final ReflectionEnvironment reflectionEnvironment;
        private final MapTypeResolver typeResolver = new MapTypeResolver();
        private final JsonCommandParser parser = new DefaultJsonParser(typeResolver);

        private KWCommandsBukkitServiceImpl(Server server) {
            this.server = server;
            this.commandMap = CommandMapHelper.getSimpleCommandMap(server);
            this.commandManager = new CommandManagerImpl();
            this.commandParser = new CommandParserImpl(this.commandManager);
            this.commandDispatcher = new CommandDispatcherImpl(this.commandManager);
            this.commandProcessor = Processors.createCommonProcessor(this.commandManager, this.commandParser, this.commandDispatcher);
            this.completion = new CompletionImpl(this.commandParser);
            this.reflectionEnvironment = new ReflectionEnvironment(this.commandManager);
            this.dispatcher = new KWBukkitCommand.Dispatcher(server, this);

            // Commons
            CommonArguments.register(server, this.reflectionEnvironment, LOCALE_MANAGER);
            TypeResolverKt.registerDefaults(this.typeResolver);
            CommonTypes.register(this.typeResolver);
        }

        @Override
        public void registerCommand(Command command, Plugin plugin) {
            boolean register = this.commandMap.register(command.getName(), plugin.getName(),
                    new KWBukkitCommand(plugin, command, this.dispatcher));

            if (register)
                this.commandManager.registerCommand(command, plugin);
        }

        @Override
        public void registerCommands(Object obj, Plugin plugin) {
            InstanceProvider instanceProvider = clazz -> {
                if (clazz == obj.getClass())
                    return obj;

                try {
                    return Reflection.getInstance(clazz);
                } catch (Exception ignored) {
                }

                throw new IllegalStateException("Cannot resolve instance of '" + clazz + "'!");
            };

            for (Command command : this.reflectionEnvironment.fromClass(obj.getClass(), instanceProvider, plugin)) {
                this.registerCommand(command, plugin);
            }

            for (Command command : this.reflectionEnvironment.fromJsonClass(obj.getClass(), instanceProvider, f -> this.parser)) {
                this.registerCommand(command, plugin);
            }
        }

        @Override
        public void unregisterCommand(Command command) {

            org.bukkit.command.Command bukkit = this.commandMap.getCommand(command.getName());

            if (!(bukkit instanceof KWBukkitCommand))
                return;

            boolean unregister = bukkit.unregister(this.commandMap);

            if (unregister) {
                Object owner = ((KWBukkitCommand) bukkit).getPlugin();
                this.getCommandManager().unregisterCommand(command, owner);
            }
        }

        @Override
        public CommandManager getCommandManager() {
            return this.commandManager;
        }

        @Override
        public CommandProcessor getCommandProcessor() {
            return this.commandProcessor;
        }

        @Override
        public CommandDispatcher getCommandDispatcher() {
            return this.commandDispatcher;
        }

        @Override
        public Completion getCompletion() {
            return this.completion;
        }

        @Override
        public CommandParser getCommandParser() {
            return this.commandParser;
        }

        public ReflectionEnvironment getReflectionEnvironment() {
            return this.reflectionEnvironment;
        }
    }
}
