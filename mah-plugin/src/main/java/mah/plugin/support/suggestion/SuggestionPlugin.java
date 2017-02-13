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
package mah.plugin.support.suggestion;

import mah.command.CommandManager;
import mah.event.EventHandler;
import mah.openapi.plugin.PluginSupport;
import mah.ui.input.InputPaneFactoryBean;
import mah.ui.input.InputTextChangedEvent;

/**
 * Created by zgq on 2/13/17.
 */
public class SuggestionPlugin extends PluginSupport {
    @Override
    public void prepare() throws Exception {
        CommandHistories commandHistories = new CommandHistories();
        CommandStatistics commandStatistics = new CommandStatistics(commandHistories);
        CommandManager.getInstance().addCommandPostProcessor(commandStatistics);
        EventHandler<? extends InputTextChangedEvent> notFoundCommandEvent
                = new TextChangedHandler(commandHistories);
        InputPaneFactoryBean.getInstance().setOnInputTextChanged(notFoundCommandEvent);
        InputPaneFactoryBean.getInstance().setOnCaretMotionChanged(new CaretMotionHandler(commandHistories));
    }
}
