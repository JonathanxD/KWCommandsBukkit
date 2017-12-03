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
package com.github.jonathanxd.kwcommandsbukkit.util;

import com.github.jonathanxd.kwcommands.printer.CommonPrinter;
import com.github.jonathanxd.kwcommands.printer.Printer;
import com.github.jonathanxd.kwcommandsbukkit.KWCommandsBukkitPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.WeakHashMap;

import kotlin.Unit;

public class PrinterUtil {

    private static final Map<CommandSender, Printer> greenPrinter = new WeakHashMap<>();
    private static final Map<CommandSender, Printer> redPrinter = new WeakHashMap<>();

    public static Printer getGreenPrinter(CommandSender sender) {
        Printer printer = greenPrinter.get(sender);

        if (printer != null)
            return printer;

        printer = PrinterUtil.getPrinter(sender, ChatColor.GREEN);

        greenPrinter.put(sender, printer);

        return printer;
    }

    public static Printer getRedPrinter(CommandSender sender) {
        Printer printer = redPrinter.get(sender);

        if (printer != null)
            return printer;

        printer = PrinterUtil.getPrinter(sender, ChatColor.RED);

        redPrinter.put(sender, printer);

        return printer;
    }

    private static Printer getPrinter(CommandSender sender, ChatColor color) {
        return new CommonPrinter(KWCommandsBukkitPlugin.LOCALIZER, f -> {
            sender.sendMessage(color.toString() + f);
            return Unit.INSTANCE;
        }, false);
    }


}
