package mah.plugin.support.translation;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.mode.AbstractMode;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.Text;
import mah.ui.pane.item.FullItem;
import mah.ui.util.ClipboardUtils;

/**
 * Created by zgq on 2017-01-23 16:11
 */
public class TranslationMode extends AbstractMode {

    private static final String NAME = "translation_mode";

    public TranslationMode() {
        super(NAME);
    }

    @Override
    public void init() {
        registerAction(new CopyWord("CopyWord"));
        registerAction(new CopyExplains("CopyExplains"));
    }

    static abstract class CopyAction extends AbstractAction{

        public CopyAction(String name) {
            super(name, TranslationCommand.class);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) throws Exception {
            Object source = actionEvent.getSource();
            TranslationCommand command = (TranslationCommand) source;
            ClassicItemListLayout layout = command.getLayout();
            actionPerformed(layout);
        }

        protected abstract void actionPerformed(ClassicItemListLayout layout) throws Exception;
    }

    static class CopyWord extends CopyAction{

        public CopyWord(String name) {
            super(name);
        }

        public void actionPerformed(ClassicItemListLayout layout) throws Exception {
            FullItem item = layout.getPendingItem();
            Text content = item.getContent();
            ClipboardUtils.copy(content.getText());
        }
    }

    static class CopyExplains extends CopyAction{

        public CopyExplains(String name) {
            super(name);
        }

        public void actionPerformed(ClassicItemListLayout layout) throws Exception {
            FullItem item = layout.getPendingItem();
            Text explains = item.getDescription();
            ClipboardUtils.copy(explains.getText());
        }
    }
}
