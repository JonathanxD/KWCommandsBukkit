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
package com.github.jonathanxd.kwcommandsbukkit.completion;

import com.github.jonathanxd.kwcommands.argument.Argument;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.util.CommandUtilKt;
import com.github.jonathanxd.kwcommandsbukkit.completion.append.AppendType;
import com.github.jonathanxd.kwcommandsbukkit.completion.append.Mixer;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SuggestionManager {
    private final Server server;
    private final Set<Suggestion> completions = new HashSet<>();

    public SuggestionManager(Server server) {
        this.server = server;
    }

    /**
     * Register argument completion.
     *
     * @param completion Argument completion.
     * @return {@link Set#add(Object)}
     */
    public boolean register(Suggestion completion) {
        return this.completions.add(completion);
    }

    /**
     * Unregister argument completion
     *
     * @param completion Argument completion.
     * @return {@link Set#remove(Object)}
     */
    public boolean unregister(Suggestion completion) {
        return this.completions.remove(completion);
    }

    /**
     * Return Server
     *
     * @return Server
     */
    public Server getServer() {
        return server;
    }

    public List<String> getSuggestionsFor(CommandSender sender, Argument<?> spec, String[] args, CommandManager commandManager) {

        String lastArg = args.length > 0 ? args[args.length - 1] : "";
        final String last = lastArg != null ? lastArg : "";


        List<String> original = spec.getPossibilities().invoke().stream()
                .filter(Objects::nonNull)
                .filter(s -> s.startsWith(last))
                .collect(Collectors.toList());

        List<String> yourSuggestions = new ArrayList<>();
        List<String> lastSuggestions = new ArrayList<>();

        for (Suggestion completion : completions) {
            yourSuggestions.clear();
            lastSuggestions.clear();
            lastSuggestions.addAll(original);

            AppendType appendType = completion.getSuggestions(sender, spec, args, lastSuggestions, yourSuggestions, commandManager);

            AppendType.apply(appendType, original, yourSuggestions, new Mixer.StringListMixer());
        }

        if (original.isEmpty()) { // Argument tip
            sender.sendMessage(ChatColor.GOLD + "= Tip =");
            sender.sendMessage(ChatColor.GREEN + "Required argument: " + CommandUtilKt.getNameOrId(spec));
            sender.sendMessage(ChatColor.GREEN + "Type: " + CommandUtilKt.getTypeStr(spec));
            sender.sendMessage(ChatColor.GREEN + "Description: " + ChatColor.AQUA + spec.getDescription());
            original.add(""); // No Bukkit, you can't complete with Player name if I don't add players to auto complete, stop doing this shit.
        }

        return original;
    }


}
