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
package com.github.jonathanxd.kwcommandsbukkit.text;

import com.github.jonathanxd.iutils.text.TextComponent;
import com.github.jonathanxd.iutils.text.localizer.Localizer;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.printer.Printer;
import com.github.jonathanxd.kwcommands.util.KLocale;
import com.github.jonathanxd.kwcommandsbukkit.util.PrinterUtil;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class LocalizedSender implements Printer {
    @NotNull
    private final CommandSender sender;

    @NotNull
    private final SenderLocalizer localizer;

    @NotNull
    private final Printer printer;

    public LocalizedSender(@NotNull CommandSender sender, @NotNull SenderLocalizer localizer) {
        this.sender = sender;
        this.localizer = localizer;
        this.printer = PrinterUtil.getPrinter(sender, localizer);
    }

    @NotNull
    public SenderLocalizer getLocalizer() {
        return this.localizer;
    }

    @NotNull
    public CommandSender getSender() {
        return this.sender;
    }

    public void sendMessage(@NotNull String s) {
        this.sender.sendMessage(s);
    }

    public void sendMessages(@NotNull String[] s) {
        this.sender.sendMessage(s);
    }

    public void sendMessage(@NotNull TextComponent message) {
        this.sender.sendMessage(this.localizer.localize(message));
    }

    public void sendMessages(@NotNull TextComponent[] messages) {
        for (TextComponent message : messages) {
            this.sendMessage(message);
        }
    }

    public void sendMessages(@NotNull Iterable<? extends TextComponent> messages) {
        for (TextComponent message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public void printCommand(Command command, int i) {
        this.printer.printCommand(command, i);
    }

    @Override
    public void printFromRoot(Command command, int i) {
        this.printer.printFromRoot(command, i);
    }

    @Override
    public void printTo(Command command, int i, Function1<? super String, Unit> function1) {
        this.printer.printTo(command, i, function1);
    }

    @Override
    public void printPlain(TextComponent textComponent) {
        this.printer.printPlain(textComponent);
    }

    @Override
    public void printEmpty() {
        this.printer.printEmpty();
    }

    @Override
    public void flush() {
        this.printer.flush();
    }
}
