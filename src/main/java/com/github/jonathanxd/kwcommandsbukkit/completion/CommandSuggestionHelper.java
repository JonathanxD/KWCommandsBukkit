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

import com.github.jonathanxd.iutils.collection.Collections3;
import com.github.jonathanxd.kwcommands.argument.Argument;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.command.CommandName;
import com.github.jonathanxd.kwcommands.manager.CommandManager;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kotlin.text.RegexOption;

// Hard to understand, I know, this is a fragment of another project
// Rewrite is needed
public final class CommandSuggestionHelper {

    private final CommandManager commandManager;
    private final SuggestionManager completionManager;

    public CommandSuggestionHelper(CommandManager commandManager, SuggestionManager completionManager) {
        this.commandManager = commandManager;
        this.completionManager = completionManager;
    }

    private static Command getSubCommand(String name, Command root) {
        return root.getSubCommand(name);
    }

    private static List<String> partial(String name, Command root) {
        List<String> finds = new ArrayList<>();

        for (Command spec : root.getSubCommands()) {
            String result;
            if ((result = CommandSuggestionHelper.testName(name, spec)) != null) {
                finds.add(result);
            }
        }

        return finds;
    }

    /**
     * Tests if {@code name} matches the name of {@code spec} command.
     */
    private static String testName(String name, Command spec) {
        List<CommandName> cnames = Collections3.listOf(spec.getName());

        cnames.addAll(spec.getAlias());

        for (CommandName txt : cnames) {
            if (txt.compareTo(name) == 0) {
                return txt.toString();
            } else {
                String stringName = txt.toString();
                String name2 = name;

                boolean ignoreCase = true;

                if (txt instanceof CommandName.RegexName) {
                    ignoreCase = ((CommandName.RegexName) txt)
                            .getRegex()
                            .getOptions()
                            .contains(RegexOption.IGNORE_CASE);
                }

                if (ignoreCase) {
                    stringName = stringName.toLowerCase();
                    name2 = name2.toLowerCase();
                }

                if (!(txt instanceof CommandName.RegexName) && stringName.startsWith(name2)) {
                    return stringName;
                }
            }
        }
        return null;

    }

    public SuggestionManager getCompletionManager() {
        return this.completionManager;
    }

    public List<String> getSuggestions(CommandSender sender, String[] names, Command root) {

        List<String> suggestions = new ArrayList<>();

        Command current = root;

        for (int x = 0; x < names.length; ++x) {

            String name = names[x];

            Command subCommand = CommandSuggestionHelper.getSubCommand(name, current);

            if (subCommand != null) {
                current = subCommand;
            } else {
                List<String> partial = CommandSuggestionHelper.partial(name, current);

                if (partial.isEmpty()) {
                    List<String> next = this.argumentSuggestions(sender, current, Arrays.copyOfRange(names, x, names.length));

                    if (next.isEmpty()) {
                        return Collections.emptyList();
                    } else {
                        return next;
                    }
                } else {
                    suggestions.addAll(partial);
                }

            }
        }
        return suggestions;
    }

    private List<String> argumentSuggestions(CommandSender sender, Command current, String[] args) {
        List<String> argumentSuggestions = new ArrayList<>();
        if (completionManager == null)
            return argumentSuggestions;

        List<Argument<?>> arguments = current.getArguments();
        int pos = args.length - 1;

        if (pos < arguments.size()) {
            Argument<?> argumentSpec = arguments.get(pos);

            argumentSuggestions.addAll(completionManager.getSuggestionsFor(sender, argumentSpec, args, commandManager));
        }

        return argumentSuggestions;
    }

    public List<String> getSuggestionsFor(CommandSender sender,
                                          CommandManager commandManager,
                                          String commandName,
                                          String[] args,
                                          boolean full) {

        List<String> suggestions = new ArrayList<>();

        for (Command commandSpec : commandManager.createListWithCommands()) {

            String result;

            if ((result = CommandSuggestionHelper.testName(commandName, commandSpec)) != null) {

                if (full && !result.equalsIgnoreCase(commandName)) {
                    continue;
                }

                List<String> suggestionsOfSub = this.getSuggestions(sender, args, commandSpec);
                if (!suggestionsOfSub.isEmpty()) {
                    suggestions.addAll(suggestionsOfSub);
                    break;
                } else if (args.length != 0) {

                    List<String> argsList = this.argumentSuggestions(sender, commandSpec, args);

                    if (!argsList.isEmpty()) {
                        suggestions.addAll(argsList);
                    }
                } else {
                    suggestions.add(result);
                }

            }
        }

        return suggestions;
    }

}
