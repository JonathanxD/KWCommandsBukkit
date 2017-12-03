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

import com.github.jonathanxd.iutils.annotation.Named;
import com.github.jonathanxd.iutils.text.TextComponent;
import com.github.jonathanxd.iutils.text.dynamic.DynamicGenerator;
import com.github.jonathanxd.iutils.text.dynamic.Section;

public interface Texts {
    Texts I = DynamicGenerator.generate(Texts.class);

    // Player
    @Section({"validator", "player"})
    TextComponent getPlayerValidatorText();

    @Section({"type", "invalid_player"})
    TextComponent getInvalidPlayerText();

    // Any player
    @Section({"validator", "any_player"})
    TextComponent getAnyPlayerValidatorText();

    @Section({"validator", "invalid_any_player"})
    TextComponent getInvalidAnyPlayerText();

    // Plugin
    @Section({"validator", "plugin"})
    TextComponent getPluginValidatorText();

    @Section({"type", "invalid_plugin"})
    TextComponent getInvalidPluginText();

    // Command
    @Section({"validator", "command"})
    TextComponent getCommandValidatorText();

    @Section({"type", "invalid_command"})
    TextComponent getInvalidCommandText();

    // Locale
    @Section({"validator", "locale"})
    TextComponent getLocaleValidatorText();

    @Section({"type", "invalid_locale"})
    TextComponent getInvalidLocaleText();

    @Section({"message", "permission"})
    TextComponent getPermissionText();

    @Section({"message", "locale_set"})
    TextComponent getLocaleSetText(@Named("locale") TextComponent locale);

    @Section({"requirement", "permission"})
    TextComponent getRequirementPermissionText();
}
