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
package com.github.jonathanxd.kwcommandsbukkit.common;

import com.github.jonathanxd.iutils.localization.Locale;
import com.github.jonathanxd.kwcommands.command.Command;
import com.github.jonathanxd.kwcommands.json.MapTypeResolver;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CommonTypes {

    public static void register(MapTypeResolver typeResolver) {
        // Java
        typeResolver.set("String", String.class);
        typeResolver.set("Byte", Byte.class);
        typeResolver.set("Char", Character.class);
        typeResolver.set("Short", Short.class);
        typeResolver.set("Int", Integer.class);
        typeResolver.set("Long", Long.class);
        typeResolver.set("Float", Float.class);
        typeResolver.set("Double", Double.class);

        // KWCommands
        typeResolver.set("Command", Command.class);
        typeResolver.set("Locale", Locale.class);

        // Bukkkit
        typeResolver.set("Sender", CommandSender.class);
        typeResolver.set("CommandSender", CommandSender.class);
        typeResolver.set("Player", Player.class);
        typeResolver.set("Server", Server.class);
        typeResolver.set("Plugin", Plugin.class);

        // Bukkit other
        typeResolver.set("Entity", Entity.class);
        typeResolver.set("EntityType", EntityType.class);

        typeResolver.set("Material", Material.class);
        typeResolver.set("ItemType", Material.class);
        typeResolver.set("BlockType", Material.class);

        typeResolver.set("ItemStack", ItemStack.class);
    }

}
