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

import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;

public class CommandMapHelper {
    public static CommandMap getSimpleCommandMap(Server server) {
        Object scm = CommandMapHelper.getFieldValue(server, "commandMap");

        if (CommandMapHelper.check(scm, SimpleCommandMap.class)) {
            return (SimpleCommandMap) scm;
        } else {
            scm = CommandMapHelper.getFieldValue(server, SimpleCommandMap.class);
            if (CommandMapHelper.check(scm, SimpleCommandMap.class)) {
                return (SimpleCommandMap) scm;
            }
        }

        throw new IllegalStateException("Cannot setup simpleCommandMap!");
    }

    private static boolean check(Object object, Class<?> type) {
        return object != null && type != null && type.isInstance(object);
    }

    private static <T> T getFieldValue(Object object, String fieldName) {
        Field field = CommandMapHelper.getField(object, fieldName);
        return CommandMapHelper.getFieldValue(object, field);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getFieldValue(Object object, Class<T> fieldType) {
        Field field = CommandMapHelper.getField(object, fieldType);
        return CommandMapHelper.getFieldValue(object, field);

    }

    @SuppressWarnings("unchecked")
    private static <T> T getFieldValue(Object object, Field field) {
        if (field == null)
            return null;

        field.setAccessible(true);

        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static Field getField(Object object, String fieldName) {
        try {
            return object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
        }

        return null;
    }

    private static Field getField(Object object, Class<?> fieldType) {

        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getType() == fieldType) {
                field.setAccessible(true);
                return field;
            }
        }

        return null;
    }

}
