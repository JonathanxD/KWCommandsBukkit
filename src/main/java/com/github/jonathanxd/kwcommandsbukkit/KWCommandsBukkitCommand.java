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
package com.github.jonathanxd.kwcommandsbukkit;

import com.github.jonathanxd.iutils.localization.Locale;
import com.github.jonathanxd.iutils.text.Text;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.json.CmdJson;
import com.github.jonathanxd.kwcommands.json.CmdJsonType;
import com.github.jonathanxd.kwcommands.manager.CommandManager;
import com.github.jonathanxd.kwcommands.printer.Printer;
import com.github.jonathanxd.kwcommands.reflect.annotation.Arg;
import com.github.jonathanxd.kwcommands.reflect.annotation.Info;
import com.github.jonathanxd.kwcommandsbukkit.util.PrinterUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Iterator;
import java.util.List;

public class KWCommandsBukkitCommand {

    @CmdJson(type = CmdJsonType.RESOURCE, value = "/kwbukkit/commands/main_command.json")
    public void kcommands(@Arg("command") Command command,
                          @Info CommandSender sender,
                          @Info CommandManager commandManager) {

        if (command != null) {
            Printer greenPrinter = PrinterUtil.getGreenPrinter(sender);
            greenPrinter.printFromRoot(command, 0);

            greenPrinter.flush();

        } else {

            List<Command> commands = commandManager.createListWithCommands();

            if (commands.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "No command registered!");
            }

            Iterator<Command> iterator = commands.iterator();

            while (iterator.hasNext()) {
                Command ncommand = iterator.next();
                Printer greenPrinter = PrinterUtil.getGreenPrinter(sender);
                greenPrinter.printFromRoot(ncommand, 0);

                if (iterator.hasNext())
                    greenPrinter.printPlain(Text.single("------------------------------------"));

                greenPrinter.flush();

            }
        }
    }

    @CmdJson(type = CmdJsonType.RESOURCE, value = "/kwbukkit/commands/set_locale_command.json")
    public void setlocale(@Arg("locale") Locale locale,
                          @Info CommandSender sender) {

        Printer greenPrinter = PrinterUtil.getGreenPrinter(sender);
        KWCommandsBukkit.LOCALIZER.setLocale(locale);

        greenPrinter.printPlain(Texts.I.getLocaleSetText(Text.single(locale.getName())));
        greenPrinter.flush();
    }
}
