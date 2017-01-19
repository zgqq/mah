package mah.app;

/**
 * Created by zgq on 2017-01-08 11:35
 */
public interface ApplicationListener {

    default void shutdown() throws Exception{}

    default void start(ApplicationEvent applicationEvent) throws Exception{}

    default void afterStart(ApplicationEvent applicationEvent) throws Exception{}

}
