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
package com.github.jonathanxd.kwcommandsbukkit.util;

import com.github.jonathanxd.iutils.text.Color;
import com.github.jonathanxd.iutils.text.localizer.Localizer;
import com.github.jonathanxd.kwcommands.printer.CommonPrinter;
import com.github.jonathanxd.kwcommands.printer.Printer;

import org.bukkit.command.CommandSender;

import kotlin.Unit;

public class PrinterUtil {

    public static Printer getPrinter(Printer printer, Color color) {
        return new CommonPrinter(printer.getLocalizer(), f -> {
            printer.printPlain(color.append(f));
            printer.flush();
            return Unit.INSTANCE;
        }, false);
    }

    public static Printer getPrinter(CommandSender sender,
                                     Localizer localizer) {
        return new CommonPrinter(localizer, f -> {
            sender.sendMessage(f);
            return Unit.INSTANCE;
        }, false);
    }


}
