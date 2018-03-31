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
package com.github.jonathanxd.kwcommandsbukkit.text;

import com.github.jonathanxd.iutils.localization.Locale;
import com.github.jonathanxd.iutils.text.TextComponent;
import com.github.jonathanxd.iutils.text.localizer.Localizer;
import com.github.jonathanxd.iutils.text.localizer.TextLocalizer;
import com.github.jonathanxd.kwcommandsbukkit.service.LocaleProvider;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SenderLocalizer implements Localizer {
    private final CommandSender sender;
    private final TextLocalizer localizer;
    private final LocaleProvider provider;
    private Locale locale;

    public SenderLocalizer(CommandSender sender,
                           TextLocalizer localizer,
                           LocaleProvider provider) {
        this.sender = sender;
        this.localizer = localizer;
        this.provider = provider;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public TextLocalizer getLocalizer() {
        return this.localizer;
    }

    public LocaleProvider getProvider() {
        return this.provider;
    }

    @NotNull
    public Locale getLocale() {
        return this.locale != null
                ? this.locale
                : this.provider.provide(this.getSender())
                               .orElse(this.getLocalizer().getLocale());
    }

    public void setLocale(@Nullable Locale locale) {
        this.locale = locale;
    }

    @Override
    public List<TextComponent> getLocalizations(@NotNull TextComponent textComponent) {
        return this.getLocalizer().getLocalizations(textComponent, this.getLocale());
    }

    @Override
    public List<TextComponent> getLocalizations(@NotNull TextComponent textComponent,
                                                @NotNull Map<String, TextComponent> args) {
        return this.getLocalizer().getLocalizations(textComponent, args, this.getLocale());
    }

    @NotNull
    @Override
    public String localize(@NotNull TextComponent textComponent,
                           @NotNull Map<String, TextComponent> args) {
        return this.getLocalizer().localize(textComponent, args, this.getLocale());
    }

    @NotNull
    @Override
    public String localize(@NotNull TextComponent textComponent) {
        return this.getLocalizer().localize(textComponent, this.getLocale());
    }
}
