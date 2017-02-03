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
package mah.ui.support.swing.layout;

import mah.ui.input.InputMode;
import mah.ui.layout.ClassicAbstractLayout;
import mah.ui.layout.LayoutFactoryBean;
import mah.ui.pane.Pane;
import mah.ui.pane.input.InputPane;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.pane.input.InputPaneImpl;
import mah.ui.support.swing.theme.SwingLayoutTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Created by zgq on 2017-01-08 11:56
 */
public class ClassicAbstractLayoutImpl extends SwingLayoutSupport implements ClassicAbstractLayout {
    private static ClassicAbstractLayoutImpl instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassicAbstractLayoutImpl.class);
    private JPanel panel;
    private InputPaneImpl inputPane;
    private SwingPane bottomPane;
    private boolean init;

    private ClassicAbstractLayoutImpl() {}

    @Override
    public void init() {
        if (init == true) {
            LOGGER.warn("The layout has already been initialized");
            return;
        }
        LOGGER.info("Initializing classic abstract layout");
        this.panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        this.panel.setLayout(boxLayout);
        this.inputPane = InputPaneImpl.newInstance();
        this.panel.add(inputPane.getPanel());
        initKeybind();
        init = true;
    }

    private void initKeybind() {
        JTextComponent input = inputPane.getInput();
        input.addKeyListener(new KeyHandler());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    private void updateBottomPane(SwingPane swingPane) {
        removeBottomPane();
        addBottomPane(swingPane);
    }

    private void addBottomPane(SwingPane swingPane) {
        JPanel panel = swingPane.getPanel();
        if (panel != null) {
            this.panel.add(panel);
        }
    }

    private void removeBottomPane() {
        if (bottomPane != null) {
            this.panel.remove(bottomPane.getPanel());
        }
    }

    @Override
    public void updatePane(Pane pane) {
        if (pane == null) {
            removeBottomPane();
            return;
        }
        if (pane instanceof SwingPane) {
            SwingPane swingPane = (SwingPane) pane;
            updateBottomPane(swingPane);
            bottomPane = swingPane;
            applyThemeToPane();
        }
    }

    private void applyToLayout() {
        Color layoutColor = getColorByProperty("background-color");
        this.panel.setBackground(layoutColor);
    }

    private void applyThemeToPane() {
        if (bottomPane != null) {
            bottomPane.apply(getCurrentTheme());
        }
    }

    @Override
    public void applyTheme(SwingLayoutTheme theme) {
        inputPane.apply(theme);
        applyToLayout();
        applyThemeToPane();
    }

    private void check() {
        if (!init) {
            throw new IllegalStateException("This layout " + getName() + " has not been initialized");
        }
    }

    @Override
    public void setDefaultMode() {
        check();
        InputMode inputMode = InputMode.triggerMode();
        inputMode.updateActionHandler(inputPane);
    }

    @Override
    public void onSetCurrentLayout() {
        inputPane.requireFocus();
    }

    @Override
    public InputPane getInputPane() {
        return inputPane;
    }

    public static ClassicAbstractLayoutImpl instance() {
        if (instance == null) {
            instance = newInstance();
        }
        return instance;
    }

    private static ClassicAbstractLayoutImpl newInstance() {
        ClassicAbstractLayoutImpl abstractClassicLayout = new ClassicAbstractLayoutImpl();
        LayoutFactoryBean.getInstance().initBean(abstractClassicLayout);
        return abstractClassicLayout;
    }

    @Override
    public String getName() {
        return "classic_abstract_layout";
    }
}
