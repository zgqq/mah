package mah.action;

/**
 * Created by zgq on 2017-01-09 09:52
 */
public abstract class AbstractAction implements Action {

    private final String name;
    private final Class<?> handler;

    public AbstractAction(String name, Class<?> handler) {
        this.name = name;
        this.handler = handler;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getHandler() {
        return handler;
    }
}


