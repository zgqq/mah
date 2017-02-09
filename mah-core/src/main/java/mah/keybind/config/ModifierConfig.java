/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mah.keybind.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zgq on 2/9/17.
 */
public class ModifierConfig {
    private static final Map<String, Modifier> MODIFIERS = new HashMap<>();

    static {
        for (Modifier modifier : Modifier.values()) {
            MODIFIERS.put(modifier.getName(), modifier);
        }
    }

    private final Modifier origin;
    private final Modifier as;


    public enum Modifier {
        CAPLOCK("Caplock"), L_CONTROL("LControl"), R_CONTROL("RControl"), L_ALT("LAlt"), R_ALT("RAlt"),
        L_META("LMeta"), R_META("RMeta");
        private final String name;

        Modifier(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private ModifierConfig(Modifier origin, Modifier as) {
        this.origin = origin;
        this.as = as;
    }

    public Modifier getOrigin() {
        return origin;
    }

    public Modifier getAs() {
        return as;
    }

    public static ModifierConfig of(String origin, String as) {
        return new ModifierConfig(getModifier(origin), getModifier(as));
    }

    public static Modifier getModifier(String name) {
        return MODIFIERS.get(name);
    }
}
