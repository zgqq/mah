package mah.plugin.support.weather;

import mah.openapi.plugin.PluginSupport;

/**
 * Created by zgq on 17-1-2 下午3:43.
 */
public class WeatherPlugin extends PluginSupport{

    @Override
    public void init() {
        registerCommand(new QueryWeatherCommand());
    }
}
