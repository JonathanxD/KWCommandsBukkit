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

import com.github.jonathanxd.iutils.reflection.Reflection;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.json.DefaultJsonParser;
import com.github.jonathanxd.kwcommands.json.JsonCommandParser;
import com.github.jonathanxd.kwcommands.json.MapTypeResolver;
import com.github.jonathanxd.kwcommands.json.TypeResolverKt;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.manager.CommandManagerImpl;
import com.github.jonathanxd.kwcommands.processor.CommandProcessor;
import com.github.jonathanxd.kwcommands.processor.Processors;
import com.github.jonathanxd.kwcommands.reflect.env.ReflectionEnvironment;
import com.github.jonathanxd.kwcommandsbukkit.common.CommonArguments;
import com.github.jonathanxd.kwcommandsbukkit.common.CommonSuggestions;
import com.github.jonathanxd.kwcommandsbukkit.common.CommonTypes;
import com.github.jonathanxd.kwcommandsbukkit.completion.CommandSuggestionHelper;
import com.github.jonathanxd.kwcommandsbukkit.completion.SuggestionManager;
import com.github.jonathanxd.kwcommandsbukkit.service.KWCommandsBukkitService;
import com.github.jonathanxd.kwcommandsbukkit.util.CommandMapHelper;

import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import kotlin.jvm.functions.Function1;

public class KWCommandsBukkitPlugin extends JavaPlugin {

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
        private final CommandSuggestionHelper commandSuggestionHelper;
        private final ReflectionEnvironment reflectionEnvironment;
        private final MapTypeResolver typeResolver = new MapTypeResolver();
        private final JsonCommandParser parser = new DefaultJsonParser(typeResolver);

        private KWCommandsBukkitServiceImpl(Server server) {
            this.server = server;
            this.commandMap = CommandMapHelper.getSimpleCommandMap(server);
            this.dispatcher = new KWBukkitCommand.Dispatcher(server, this);
            this.commandManager = new CommandManagerImpl();
            this.commandProcessor = Processors.createCommonProcessor(this.commandManager);
            this.commandSuggestionHelper = new CommandSuggestionHelper(this.commandManager, new SuggestionManager(server));
            this.reflectionEnvironment = new ReflectionEnvironment(this.commandManager);

            // Commons
            CommonSuggestions.register(this.commandSuggestionHelper.getCompletionManager());
            CommonArguments.register(server, this.reflectionEnvironment);
            TypeResolverKt.registerDefaults(this.typeResolver);
            CommonTypes.register(this.typeResolver);
        }

        @Override
        public void registerCommand(Command command, Plugin plugin) {
            boolean register = this.commandMap.register(command.getName().toString(), plugin.getName(),
                    new KWBukkitCommand(plugin, command, this.dispatcher));

            if (register)
                this.commandManager.registerCommand(command, plugin);
        }

        @Override
        public void registerCommands(Object obj, Plugin plugin) {
            Function1<Class<?>, Object> instanceProvider = clazz -> {
                if (clazz == obj.getClass())
                    return obj;

                try {
                    return Reflection.getInstance(clazz);
                } catch (Exception ignored) {
                }

                throw new IllegalStateException("Cannot resolve instance of '"+clazz+"'!");
            };

            for (Command command : this.reflectionEnvironment.fromClass(obj.getClass(), instanceProvider, plugin)) {
                this.registerCommand(command, plugin);
            }

            for (Command command : this.reflectionEnvironment.fromJsonClass(obj.getClass(), instanceProvider, f -> this.parser)) {
                this.registerCommand(command, plugin);
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
        public CommandSuggestionHelper getSuggestionHelper() {
            return this.commandSuggestionHelper;
        }
    }
}
