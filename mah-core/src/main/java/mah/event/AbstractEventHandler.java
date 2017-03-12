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
package mah.event;

import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * Created by zgq on 2/13/17.
 */
public abstract class AbstractEventHandler<T> implements ComparableEventHandler<T> {
    private final int priority;

    public AbstractEventHandler(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull ComparableEventHandler o) {
        int handlerPriority = o.getPriority();
        if (this.priority == handlerPriority) {
            return 0;
        }
        return (this.priority < handlerPriority) ? 1 : -1;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
