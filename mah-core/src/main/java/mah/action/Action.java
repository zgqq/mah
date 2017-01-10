package mah.action;

/**
 * Created by zgq on 2017-01-09 09:12
 */
public interface Action {


    String getName();

    Class<?> getHandler();

    void actionPerformed(ActionEvent actionEvent);


}
