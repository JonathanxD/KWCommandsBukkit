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
package com.github.jonathanxd.kwcommandsbukkit.text;

import com.github.jonathanxd.iutils.localization.Locale;
import com.github.jonathanxd.iutils.localization.LocaleManager;
import com.github.jonathanxd.iutils.text.Color;
import com.github.jonathanxd.iutils.text.Style;
import com.github.jonathanxd.iutils.text.Text;
import com.github.jonathanxd.iutils.text.TextComponent;
import com.github.jonathanxd.iutils.text.converter.FastTextLocalizer;
import com.github.jonathanxd.iutils.text.converter.TextLocalizer;

import org.bukkit.ChatColor;

import java.util.Map;

public class ColoredLocalizer implements TextLocalizer {

    private final TextLocalizer def;

    public ColoredLocalizer(TextLocalizer def) {
        this.def = new FastTextLocalizer(
                def.getLocaleManager(),
                def.getDefaultLocale(),
                def.getLocale(),
                ColoredLocalizer::fromColor,
                ColoredLocalizer::fromStyle);
    }

    public static TextComponent fromColor(Color color) {
        switch (color) {
            case RED:
                return Text.single(ChatColor.RED.toString());
            case AQUA:
                return Text.single(ChatColor.AQUA.toString());
            case BLACK:
                return Text.single(ChatColor.BLACK.toString());
            case BLUE:
                return Text.single(ChatColor.BLUE.toString());
            case DARK_AQUA:
                return Text.single(ChatColor.DARK_AQUA.toString());
            case DARK_BLUE:
                return Text.single(ChatColor.DARK_BLUE.toString());
            case DARK_GRAY:
                return Text.single(ChatColor.DARK_GRAY.toString());
            case DARK_GREEN:
                return Text.single(ChatColor.DARK_GREEN.toString());
            case DARK_PURPLE:
                return Text.single(ChatColor.DARK_PURPLE.toString());
            case DARK_RED:
                return Text.single(ChatColor.DARK_RED.toString());
            case GOLD:
                return Text.single(ChatColor.GOLD.toString());
            case GRAY:
                return Text.single(ChatColor.GRAY.toString());
            case GREEN:
                return Text.single(ChatColor.GREEN.toString());
            case LIGHT_PURPLE:
                return Text.single(ChatColor.LIGHT_PURPLE.toString());
            case WHITE:
                return Text.single(ChatColor.WHITE.toString());
            case YELLOW:
                return Text.single(ChatColor.YELLOW.toString());
            default:
                return Text.single("");
        }
    }

    public static TextComponent fromStyle(Style style) {

        switch (style) {
            case BOLD:
                return Text.single(ChatColor.BOLD.toString());
            case ITALIC:
                return Text.single(ChatColor.ITALIC.toString());
            case OBFUSCATED:
                return Text.single(ChatColor.MAGIC.toString());
            case RESET:
                return Text.single(ChatColor.RESET.toString());
            case STRIKETHROUGH:
                return Text.single(ChatColor.STRIKETHROUGH.toString());
            case UNDERLINE:
                return Text.single(ChatColor.UNDERLINE.toString());
            default:
                return Text.single("");
        }

    }

    public TextLocalizer getWrapped() {
        return this.def;
    }

    @Override
    public String localize(TextComponent textComponent) {
        return this.getWrapped().localize(textComponent);
    }

    @Override
    public String localize(TextComponent textComponent, Map<String, TextComponent> args) {
        return this.getWrapped().localize(textComponent, args);
    }

    @Override
    public String localize(TextComponent textComponent, Locale locale) {
        return this.getWrapped().localize(textComponent, locale);
    }

    @Override
    public String localize(TextComponent textComponent, Map<String, TextComponent> args, Locale locale) {
        return this.getWrapped().localize(textComponent, args, locale);
    }

    @Override
    public LocaleManager getLocaleManager() {
        return this.getWrapped().getLocaleManager();
    }

    @Override
    public Locale getDefaultLocale() {
        return this.getWrapped().getDefaultLocale();
    }

    @Override
    public Locale setDefaultLocale(Locale locale) {
        return this.getWrapped().setDefaultLocale(locale);
    }

    @Override
    public Locale getLocale() {
        return this.getWrapped().getLocale();
    }

    @Override
    public Locale setLocale(Locale locale) {
        return this.getWrapped().setLocale(locale);
    }
}
