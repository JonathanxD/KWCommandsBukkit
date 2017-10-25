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

import com.github.jonathanxd.kwcommands.util.StringParseKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CommandStringUtil {

    @NotNull
    public static String getCommandString(@NotNull String message) {
        if (message.length() > 1) {
            return message.substring(1);
        }
        return "";
    }

    @NotNull
    public static List<String> getCommandStringListFromMessage(@NotNull String message) {
        if (!message.isEmpty())
            return StringParseKt.toCommandStringList(message);

        return Collections.emptyList();
    }

    @Nullable
    public static String getCommandOwnerPlugin(@NotNull String command) {

        char[] chars = command.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == ':' && i - 1 > -1 && chars[i - 1] != '\\') {
                return StringParseKt.escape(command.substring(0, i), '\\');
            }

        }

        return null;
    }

}
