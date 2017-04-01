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
package mah.ui.support.swing.pane.input;

import mah.ui.UiException;
import mah.event.EventHandler;
import mah.ui.font.FontManager;
import mah.ui.input.CaretMotionEvent;
import mah.ui.input.InputPaneFactoryBean;
import mah.ui.input.InputTextChangedEvent;
import mah.ui.input.TextState;
import mah.ui.pane.input.InputPane;
import mah.ui.pane.input.InputPaneSupport;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.theme.SwingLayoutTheme;
import mah.ui.theme.LayoutTheme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.text.AttributedCharacterIterator;

/**
 * Created by zgq on 2017-01-08 12:00
 */
public class InputPaneImpl extends InputPaneSupport implements InputPane, SwingPane {
    private JTextComponent input;
    private JPanel panel;
    private final int panelPrefWidth;
    private final int panelPrefHeight;
    private TextProcessor textProcessor;
    private boolean notifyTextChanged;

    private InputPaneImpl() {
        this(600, 70);
    }

    private InputPaneImpl(int panelPrefWidth, int panelPrefHeight) {
        this.panelPrefWidth = panelPrefWidth;
        this.panelPrefHeight = panelPrefHeight;
        this.notifyTextChanged = true;
        this.textProcessor = new NulltextProcessor();
    }

    public void init() {
        this.panel = new JPanel();
        this.panel.setLayout(new GridBagLayout());
        this.panel.setOpaque(false);
        initInput();
    }

    public Document getDocument() {
        return this.input.getDocument();
    }


    public void setDocument(Document document) {
        this.input.setDocument(document);
        listenInput((JTextPane) input);
    }

    public void setTextProcessor(TextProcessor textProcessor) {
        this.textProcessor = textProcessor;
    }

    public JTextComponent getInput() {
        return input;
    }

    private void initInput() {
        JTextPane input = new JTextPane();
        int inputWidth = (int) (panelPrefWidth * 0.9);
        int inputHeight = (int) (panelPrefHeight * 0.8);
        input.setPreferredSize(new Dimension((inputWidth), inputHeight));
        input.setBorder(BorderFactory.createCompoundBorder(
                input.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        input.setFont(new Font(FontManager.getInstance().getCurrentFontName(), Font.PLAIN, 24));
        limitInputCharacterNum(input);
        listenInputMethod(input);
        listenInput(input);
        disposeKeybinds(input);

        this.input = input;
        this.panel.setPreferredSize(new Dimension(panelPrefWidth, panelPrefHeight));
        this.panel.add(input);
    }

    private void listenInput(final JTextPane input) {
        input.getDocument()
                .addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            if (isNotifyTextChanged()) {
                                fireTextChangedEvent(e.getDocument());
                            }
                        } catch (BadLocationException e1) {
                            throw new UiException(e1);
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            if (isNotifyTextChanged()) {
                                fireTextChangedEvent(e.getDocument());
                            }
                        } catch (BadLocationException e1) {
                            throw new UiException(e1);
                        }
                    }

                    private void fireTextChangedEvent(Document document) throws BadLocationException {
                        final TextState oldState = ((StyledDocument) document).getOldState();
                        SwingUtilities.invokeLater(() -> {
                            try {
                                String newText = document.getText(0,
                                        document.getLength());
                                String oldText = oldState.getText();

                                TextContext textContext = new TextContext(oldText, newText, document);
                                newText = textProcessor.process(textContext);

                                TextState newState = new TextState.Builder(newText, input.getCaretPosition()).build();
                                InputTextChangedEvent inputTextChangedEvent = new InputTextChangedEvent(
                                        newState,
                                        oldState);
                                for (EventHandler eventHandler : getInputTextChangedHandlers()) {
                                    eventHandler.handle(inputTextChangedEvent);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                });
    }

    private void limitInputCharacterNum(JTextPane input) {
        input.setStyledDocument(new StyledDocument(this));
    }

    private void listenInputMethod(final JTextPane input) {
        input.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                AttributedCharacterIterator text = event.getText();
                if (text != null) {
                    int committedCharacterCount = event.getCommittedCharacterCount();
                    char c = text.first();
                    StringBuilder textBuffer = new StringBuilder();
                    boolean allLetter = true;
                    while (committedCharacterCount-- > 0) {
                        textBuffer.append(c);
                        c = text.next();
                        if (!Character.isLetter(c)) {
                            allLetter = false;
                        }
                    }
                    if (event.getCommittedCharacterCount() > 0) {
                        event.consume();
                        if (allLetter) {
                            return;
                        }
                        SwingUtilities.invokeLater(() -> {
                            int caretPosition = input.getCaretPosition();
                            try {
                                input.getDocument().insertString(caretPosition, textBuffer.toString(), null);
                            } catch (BadLocationException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {
            }

        });
    }

    private void disposeKeybinds(JTextComponent input) {
        InputMap inputMap = input.getInputMap();
        KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK, false);
        inputMap.put(keystroke, "none");
        KeyStroke keystroke2 = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, KeyEvent.CTRL_MASK, false);
        inputMap.put(keystroke2, "none");
        KeyStroke keystroke3 = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, false);
        inputMap.put(keystroke3, "none");
        KeyStroke keystroke4 = KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK, false);
        inputMap.put(keystroke4, "none");
        KeyStroke keystroke5 = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        inputMap.put(keystroke5, "none");
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof SwingLayoutTheme) {
            SwingLayoutTheme layoutTheme = (SwingLayoutTheme) theme;
            String panelBackgroundColor = layoutTheme.findProperty("background-color");
            panel.setBackground(Color.decode(panelBackgroundColor));
            String inputFieldBackgroundColor = layoutTheme.findProperty("input-field-background-color");
            input.setBackground(Color.decode(inputFieldBackgroundColor));
            String fontColor = layoutTheme.findProperty("input-field-font-color");
            input.setForeground(Color.decode(fontColor));
            String cursorColor = layoutTheme.findProperty("input-cursor-color");
            input.setCaretColor(Color.decode(cursorColor));
        }
    }

    public static InputPaneImpl newInstance() {
        InputPaneImpl inputPane = new InputPaneImpl();
        InputPaneFactoryBean.getInstance().initBean(inputPane);
        inputPane.init();
        return inputPane;
    }

    @Override
    public void setText(String text) {
        input.setText(text);
    }

    @Override
    public void setCaretPosition(int position) {
        java.util.List<EventHandler<? extends CaretMotionEvent>> caretMotionHandlers = getCaretMotionHandlers();
        CaretMotionEvent caretMotionEvent = new CaretMotionEvent(this, input.getCaretPosition(), position);
        input.setCaretPosition(position);
        try {
            for (EventHandler caretMotionHandler : caretMotionHandlers) {
                caretMotionHandler.handle(caretMotionEvent);
            }
        } catch (Exception e) {
            throw new UiException(e);
        }
    }

    @Override
    protected boolean canMotionForward() {
        return getCaretPosition() < getDocument().getLength();
    }

    @Override
    public String getText() {
        return input.getText();
    }


    @Override
    public int getCaretPosition() {
        return input.getCaretPosition();
    }


    public void requireFocus() {
        input.requestFocus();
    }

    @Override
    public void remove(int off, int len) {
        try {
            input.getDocument().remove(off, len);
        } catch (BadLocationException e) {
            throw new UiException("Remove failed,off " + off + ",len " + len + "", e);
        }
    }

    @Override
    public void endOfLine() {
        if (canMotionForward()) {
            setCaretPosition(getDocument().getLength());
        }
    }

    @Override
    protected int motionLength() {
        return getDocument().getLength();
    }

    @Override
    protected int textLength() {
        return ((StyledDocument) input.getDocument()).length();
    }

    public void setNotifyTextChanged(boolean notifyTextChanged) {
        this.notifyTextChanged = notifyTextChanged;
    }

    public boolean isNotifyTextChanged() {
        return notifyTextChanged;
    }
}
