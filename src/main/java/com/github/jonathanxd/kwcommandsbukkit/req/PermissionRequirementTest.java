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
package com.github.jonathanxd.kwcommandsbukkit.req;

import com.github.jonathanxd.iutils.text.TextComponent;
import com.github.jonathanxd.kwcommands.information.Information;
import com.github.jonathanxd.kwcommands.requirement.Requirement;
import com.github.jonathanxd.kwcommands.requirement.RequirementTester;
import com.github.jonathanxd.kwcommandsbukkit.Texts;

import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

public class PermissionRequirementTest implements RequirementTester<Permissible, String> {

    public static final PermissionRequirementTest INSTANCE = new PermissionRequirementTest();

    protected PermissionRequirementTest() {
    }

    @NotNull
    @Override
    public TextComponent getName() {
        return Texts.I.getRequirementPermissionText();
    }

    @Override
    public boolean test(Requirement<Permissible, String> requirement, Permissible permissible) {
        return permissible != null && permissible.hasPermission(requirement.getRequired());
    }
}
