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
package com.github.jonathanxd.kwcommandsbukkit.info;

import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.kwcommands.information.Information;
import com.github.jonathanxd.kwcommands.information.InformationProvider;
import com.github.jonathanxd.kwcommands.information.InformationProviders;
import com.github.jonathanxd.kwcommands.util.InfoUtilKt;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class BukkitInformationProvider implements InformationProvider {
    private final Information<Server> serverInfo;

    public BukkitInformationProvider(Server server) {
        this.serverInfo = BukkitInfo.createServerInformation(server);
    }

    @Nullable
    @Override
    public <T> Information<T> provide(Information.Id<? extends T> id, InformationProviders providers) {
        return InformationProvider.DefaultImpls.provide(this, id, providers);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> Information<T> provide(TypeInfo<? extends T> typeInfo,
                                      String[] tags,
                                      InformationProviders informationProviders) {
        if (InfoUtilKt.matches(typeInfo, tags, BukkitInfo.SERVER_ID.getType(), BukkitInfo.SERVER_ID.getTags())) {
            return (Information<T>) this.serverInfo;
        }

        if (typeInfo.isAssignableFrom(BukkitInfo.PLUGIN)) {
            if (tags.length == 0)
                return null;

            String s = tags[0];

            Plugin plugin = this.serverInfo.getValue().getPluginManager().getPlugin(s);

            if (plugin != null)
                return (Information<T>) BukkitInfo.createPluginInformation(plugin);
        }

        return null;
    }
}
