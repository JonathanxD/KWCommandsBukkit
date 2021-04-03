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
package com.github.jonathanxd.kwcommandsbukkit;

import com.github.jonathanxd.iutils.localization.LocaleLoader;
import com.github.jonathanxd.iutils.localization.LocaleManager;
import com.github.jonathanxd.iutils.localization.json.JsonLocaleLoader;
import com.github.jonathanxd.iutils.text.localizer.TextLocalizer;
import com.github.jonathanxd.kwcommands.util.KLocale;
import com.github.jonathanxd.kwcommandsbukkit.text.ColoredLocalizer;

import java.nio.file.Paths;

public class KWCommandsBukkit {

    public static final LocaleManager LOCALE_MANAGER = KLocale.INSTANCE.getLocaleManager();
    public static final TextLocalizer LOCALIZER = new ColoredLocalizer(
            KLocale.INSTANCE.getLocalizer());

    static {
        KLocale.INSTANCE.setLocalizer(LOCALIZER);
        LocaleLoader localeLoader = JsonLocaleLoader.JSON_LOCALE_LOADER;

        localeLoader.loadFromResource(KLocale.INSTANCE.getDefaultLocale(),
                Paths.get("kwbukkit/lang/"),
                null,
                KWCommandsBukkitPlugin.class.getClassLoader());

        localeLoader.loadFromResource(KLocale.INSTANCE.getPtBr(),
                Paths.get("kwbukkit/lang/"),
                null,
                KWCommandsBukkitPlugin.class.getClassLoader());
    }
}
