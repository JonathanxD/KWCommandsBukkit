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
import com.github.jonathanxd.iutils.text.Color;
import com.github.jonathanxd.iutils.text.Style;
import com.github.jonathanxd.iutils.text.Text;
import com.github.jonathanxd.iutils.text.TextComponent;
import com.github.jonathanxd.iutils.text.converter.DefaultTextLocalizer;
import com.github.jonathanxd.iutils.text.converter.TextLocalizer;

import org.bukkit.ChatColor;

import java.util.Map;

public class ColoredLocalizer extends DefaultTextLocalizer {

    private final TextLocalizer def;

    public ColoredLocalizer(TextLocalizer def) {
        super(def.getLocaleManager(), def.getDefaultLocale(), def.getLocale());
        this.def = def;
    }

    @Override
    public String localize(TextComponent textComponent, Map<String, TextComponent> args, Locale locale) {
        if (textComponent instanceof Color) {
            Color color = (Color) textComponent;

            switch (color) {
                case RED:
                    return super.localize(Text.single(ChatColor.RED.toString()), args, locale);
                case AQUA:
                    return super.localize(Text.single(ChatColor.AQUA.toString()), args, locale);
                case BLACK:
                    return super.localize(Text.single(ChatColor.BLACK.toString()), args, locale);
                case BLUE:
                    return super.localize(Text.single(ChatColor.BLUE.toString()), args, locale);
                case DARK_AQUA:
                    return super.localize(Text.single(ChatColor.DARK_AQUA.toString()), args, locale);
                case DARK_BLUE:
                    return super.localize(Text.single(ChatColor.DARK_BLUE.toString()), args, locale);
                case DARK_GRAY:
                    return super.localize(Text.single(ChatColor.DARK_GRAY.toString()), args, locale);
                case DARK_GREEN:
                    return super.localize(Text.single(ChatColor.DARK_GREEN.toString()), args, locale);
                case DARK_PURPLE:
                    return super.localize(Text.single(ChatColor.DARK_PURPLE.toString()), args, locale);
                case DARK_RED:
                    return super.localize(Text.single(ChatColor.DARK_RED.toString()), args, locale);
                case GOLD:
                    return super.localize(Text.single(ChatColor.GOLD.toString()), args, locale);
                case GRAY:
                    return super.localize(Text.single(ChatColor.GRAY.toString()), args, locale);
                case GREEN:
                    return super.localize(Text.single(ChatColor.GREEN.toString()), args, locale);
                case LIGHT_PURPLE:
                    return super.localize(Text.single(ChatColor.LIGHT_PURPLE.toString()), args, locale);
                case WHITE:
                    return super.localize(Text.single(ChatColor.WHITE.toString()), args, locale);
                case YELLOW:
                    return super.localize(Text.single(ChatColor.YELLOW.toString()), args, locale);
                default:
                    return super.localize(textComponent, args, locale);
            }
        } else if (textComponent instanceof Style) {
            Style style = (Style) textComponent;

            switch (style) {
                case BOLD:
                    return super.localize(Text.single(ChatColor.BOLD.toString()), args, locale);
                case ITALIC:
                    return super.localize(Text.single(ChatColor.ITALIC.toString()), args, locale);
                case OBFUSCATED:
                    return super.localize(Text.single(ChatColor.MAGIC.toString()), args, locale);
                case RESET:
                    return super.localize(Text.single(ChatColor.RESET.toString()), args, locale);
                case STRIKETHROUGH:
                    return super.localize(Text.single(ChatColor.STRIKETHROUGH.toString()), args, locale);
                case UNDERLINE:
                    return super.localize(Text.single(ChatColor.UNDERLINE.toString()), args, locale);
                default:
                    return super.localize(textComponent, args, locale);
            }
        }

        return super.localize(textComponent, args, locale);
    }
}
