package mah.app;

/**
 * Created by zgq on 2017-01-08 11:35
 */
public interface ApplicationListener {

    default void start(ApplicationEvent applicationEvent){

    }

    default void afterStart(ApplicationEvent applicationEvent) {

    }

}
