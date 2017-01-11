package mah.ui.support.swing.layout;

/**
 * Created by zgq on 2017-01-08 12:24
 */
public class DefaultLayout extends SwingAbstractClassicLayoutWrapper implements SwingLayout {

    public DefaultLayout() {
        super(ClassicAbstractLayoutImpl.instance());
    }

    public void use() {
        getLayout().updatePane(null);
    }


    @Override
    public String getName() {
        return "default_layout";
    }
}
