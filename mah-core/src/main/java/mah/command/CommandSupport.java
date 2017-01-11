package mah.command;

import mah.action.ActionHandler;
import mah.command.event.*;
import mah.mode.EnableMode;
import mah.mode.Mode;
import mah.mode.ModeManager;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zgq on 2017-01-09 09:10
 */
public abstract class CommandSupport implements EnableMode, ActionHandler, Command {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private final List<EventHandler<? extends InitializeEvent>> initializeHandlers = new ArrayList<>(3);
    private final List<EventHandler<? extends TriggerEvent>> triggerEventHandlers = new ArrayList<>(3);
    private final List<EventHandler<? extends CommonFilterEvent>> commonFilterEventHandlers = new ArrayList<>(3);
    private Mode mode;

//    @Override
//    public void trigger(CommandEvent event) throws Exception {
//        if (mode != null) {
//            triggerMode(mode.getName());
//        }
//        triggerHook(event);
//    }

    protected final void triggerMode() {
        if (mode != null) {
            triggerMode(mode.getName());
        }
    }

    protected void triggerMode(String mode) {
        ModeManager.getInstance().triggerMode(mode);
    }

    @Override
    public List<EventHandler<? extends CommonFilterEvent>> getCommonFilterEventHandlers() {
        return commonFilterEventHandlers;
    }

    @Override
    public List<EventHandler<? extends InitializeEvent>> getInitializeHandlers() {
        return initializeHandlers;
    }

    @Override
    public List<EventHandler<? extends TriggerEvent>> getTriggerEventHandlers() {
        return triggerEventHandlers;
    }

//    protected void triggerHook(CommandEvent event) throws Exception {
//
//    }

    @Override
    public void registerMode(ModeManager modeManager) {
        Mode mode = registerModeHook(modeManager);
        if (mode == null) {
            return;
        }
        this.mode = mode;
        modeManager.registerMode(mode, this);
    }

    protected Mode registerModeHook(ModeManager modeManager) {
        return null;

    }

    protected final <T> Future<T> submit(Callable<T> task) {
        return getExecutor().submit(task);
    }

    protected final Future<?> submit(Runnable task) {
        return getExecutor().submit(task);
    }

    protected final ExecutorService getExecutor() {
        return executor;
    }

    protected final URL getResource(String path) {
        return getClass().getClassLoader().getResource(path);
    }

    protected final InputStream getInputStreamFromClasspath(String path) {
        return CommandSupport.class.getClassLoader().getResourceAsStream(path);
    }

    protected final void addTriggerEventHandler(EventHandler<? extends TriggerEvent> handler) {
        this.triggerEventHandlers.add(handler);
    }

    protected final void addCommonFilterEventHandler(EventHandler<? extends CommonFilterEvent> handler) {
        this.commonFilterEventHandlers.add(handler);
    }

    protected final void addInitializeEventHandler(EventHandler<? extends InitializeEvent> handler) {
        this.initializeHandlers.add(handler);
    }

    @Override
    public String toString() {
        return "CommandSupport{" +
                "mode=" + mode +
                ",name=" + getName() + "}";
    }
}
