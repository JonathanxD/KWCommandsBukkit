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

import com.github.jonathanxd.iutils.localization.Locale;
import com.github.jonathanxd.iutils.localization.LocaleManager;
import com.github.jonathanxd.iutils.text.Color;
import com.github.jonathanxd.iutils.text.Style;
import com.github.jonathanxd.iutils.text.Text;
import com.github.jonathanxd.iutils.text.TextComponent;
import com.github.jonathanxd.iutils.text.localizer.FastTextLocalizer;
import com.github.jonathanxd.iutils.text.localizer.TextLocalizer;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ColoredLocalizer implements TextLocalizer {

    private final TextLocalizer def;

    public ColoredLocalizer(@NotNull TextLocalizer def) {
        this.def = new FastTextLocalizer(
                def.getLocaleManager(),
                def.getDefaultLocale(),
                def.getLocale(),
                ColoredLocalizer::fromColor,
                ColoredLocalizer::fromStyle);
    }

    @NotNull
    public static TextComponent fromColor(@NotNull Color color) {
        switch (color.getName()) {
            case "red":
                return Text.single(ChatColor.RED.toString());
            case "aqua":
                return Text.single(ChatColor.AQUA.toString());
            case "black":
                return Text.single(ChatColor.BLACK.toString());
            case "blue":
                return Text.single(ChatColor.BLUE.toString());
            case "dark_aqua":
                return Text.single(ChatColor.DARK_AQUA.toString());
            case "dark_blue":
                return Text.single(ChatColor.DARK_BLUE.toString());
            case "dark_gray":
                return Text.single(ChatColor.DARK_GRAY.toString());
            case "dark_green":
                return Text.single(ChatColor.DARK_GREEN.toString());
            case "dark_purple":
                return Text.single(ChatColor.DARK_PURPLE.toString());
            case "dark_red":
                return Text.single(ChatColor.DARK_RED.toString());
            case "gold":
                return Text.single(ChatColor.GOLD.toString());
            case "gray":
                return Text.single(ChatColor.GRAY.toString());
            case "green":
                return Text.single(ChatColor.GREEN.toString());
            case "light_purple":
                return Text.single(ChatColor.LIGHT_PURPLE.toString());
            case "white":
                return Text.single(ChatColor.WHITE.toString());
            case "yellow":
                return Text.single(ChatColor.YELLOW.toString());
            default:
                return Text.single(ChatColor.WHITE.toString());
        }
    }

    @NotNull
    public static TextComponent fromStyle(@NotNull Style style) {
        // None=
        if (!(style.isBold() || style.isUnderline() || style.isStrikeThrough() || style
                .isObfuscated() || style.isItalic()))
            return Text.single(ChatColor.RESET);

        TextComponent component = Text.single("");

        if (style.isBold()) {
            component = component.append(ChatColor.BOLD.toString());
        }

        if (style.isItalic()) {
            component = component.append(ChatColor.ITALIC.toString());
        }

        if (style.isObfuscated()) {
            component = component.append(ChatColor.MAGIC.toString());
        }

        if (style.isStrikeThrough()) {
            component = component.append(ChatColor.STRIKETHROUGH.toString());
        }

        if (style.isUnderline()) {
            component = component.append(ChatColor.UNDERLINE.toString());
        }

        return component;
    }

    @NotNull
    public TextLocalizer getWrapped() {
        return this.def;
    }

    @NotNull
    @Override
    public String localize(@NotNull TextComponent textComponent) {
        return this.getWrapped().localize(textComponent);
    }

    @NotNull
    @Override
    public String localize(@NotNull TextComponent textComponent, @NotNull Map<String, TextComponent> args) {
        return this.getWrapped().localize(textComponent, args);
    }

    @NotNull
    @Override
    public String localize(@NotNull TextComponent textComponent, @Nullable Locale locale) {
        return this.getWrapped().localize(textComponent, locale);
    }

    @NotNull
    @Override
    public String localize(@NotNull TextComponent textComponent,
                           @NotNull Map<String, TextComponent> args, @Nullable Locale locale) {
        return this.getWrapped().localize(textComponent, args, locale);
    }

    @NotNull
    @Override
    public LocaleManager getLocaleManager() {
        return this.getWrapped().getLocaleManager();
    }

    @NotNull
    @Override
    public Locale getDefaultLocale() {
        return this.getWrapped().getDefaultLocale();
    }

    @NotNull
    @Override
    public Locale setDefaultLocale(@NotNull Locale locale) {
        return this.getWrapped().setDefaultLocale(locale);
    }

    @NotNull
    @Override
    public Locale getLocale() {
        return this.getWrapped().getLocale();
    }

    @NotNull
    @Override
    public Locale setLocale(@NotNull Locale locale) {
        return this.getWrapped().setLocale(locale);
    }
}
