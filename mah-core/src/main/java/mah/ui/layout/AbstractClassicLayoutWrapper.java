package mah.ui.layout;

import mah.ui.pane.Pane;

/**
 * Created by zgq on 2017-01-08 12:24
 */
public class AbstractClassicLayoutWrapper implements AbstractClassicLayout{

    private final AbstractClassicLayout layout;

    public AbstractClassicLayoutWrapper(AbstractClassicLayout layout) {
        this.layout = layout;
    }

    @Override
    public void updatePane(Pane pane) {
        layout.updatePane(pane);
    }

}
