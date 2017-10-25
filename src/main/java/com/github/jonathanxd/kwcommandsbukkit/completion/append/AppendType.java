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
package com.github.jonathanxd.kwcommandsbukkit.completion.append;

import java.util.List;

/**
 * Suggestions append Types
 */
public enum AppendType {
    /**
     * Append your suggestions to original list
     */
    APPEND,
    /**
     * Replace old suggestions with your suggestions (will clean up the original list)
     */
    REPLACE,
    /**
     * Mix your suggestions with original suggestions, normally the system will compare the lists
     * and mix the lines, example:
     * <pre>
     * {@code
     *
     * Original Suggestions = [Mary, Carl, Buggy Herobrine]
     * Your Suggestions = [Mary, Carl Buggy]
     * Final Suggestions = [Mary, Carl Buggy, Buggy Herobrine]
     *
     * }
     * </pre>
     */
    MIX;

    public static <E, T extends List<E>> T apply(AppendType type, T dest, T source, Mixer<T> mixer) {
        if(type == APPEND) {
            dest.addAll(source);
        } else if(type == REPLACE) {
            dest.clear();
            dest.addAll(source);
        } else if(type == MIX) {
            dest = mixer.mix(dest, source);
        }

        return dest;
    }

}
