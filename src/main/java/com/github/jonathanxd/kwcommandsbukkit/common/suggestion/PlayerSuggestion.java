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
package com.github.jonathanxd.kwcommandsbukkit.common.suggestion;

import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.kwcommands.argument.Argument;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommandsbukkit.completion.Suggestion;
import com.github.jonathanxd.kwcommandsbukkit.completion.append.AppendType;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PlayerSuggestion implements Suggestion {
    private final Server server;

    public PlayerSuggestion(Server server) {
        this.server = server;
    }

    @Override
    public AppendType getSuggestions(Argument<?> spec,
                                     String[] input,
                                     List<String> lastSuggestions,
                                     List<String> yourSuggestions,
                                     CommandManager commandManager) {

        if (spec.getType().isAssignableFrom(TypeInfo.of(Player.class))) {
            Collection<? extends Player> onlinePlayers = server.getOnlinePlayers();

            for (String currentInput : input) {
                onlinePlayers.stream()
                        .filter(player -> player.getName().toLowerCase().contains(currentInput))
                        .map(Player::getName)
                        .forEach(yourSuggestions::add);
            }
        }

        return AppendType.APPEND;
    }
}
