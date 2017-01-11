package mah.plugin.support.weather;

import mah.common.json.JSONArr;
import mah.common.json.JSONObj;
import mah.common.json.JSONUtils;
import mah.common.util.HttpUtils;
import mah.plugin.command.PluginCommandSupport;
import mah.ui.annnotation.Layout;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.FullItemImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 16-1-02.
 */
public class QueryWeatherCommand extends PluginCommandSupport {

    private String url = "http://api.map.baidu.com/telematics/v3/weather?output=json&ak=Gy7SGUigZ4HxGYDaq9azWy09&location=";
    @Layout
    private ClassicItemListLayout layout;

    public QueryWeatherCommand() {
        init();
        layout = getLayoutFactory().createClassicItemListLayout();
    }

    public void init() {
        addTriggerEventHandler(event -> triggerHook());
        addCommonFilterEventHandler(event -> {
            filterHook(event.getContent());
        });
    }


    private InputStream downloadImage(String url) {
        InputStream inputStream = HttpUtils.getInputStream(url);
        return inputStream;
    }

    private FullItemImpl createResult(JSONObj weatherData) {
        String date = weatherData.getString("date");
        String weather = weatherData.getString("weather");
        String wind = weatherData.getString("wind");
        String temperature = weatherData.getString("temperature");
        String dayPictureUrl = weatherData.getString("dayPictureUrl");
        InputStream inputStream = downloadImage(dayPictureUrl);
        return new FullItemImpl.Builder(date + "    " + weather) //
                .iconInputStream(inputStream) //
                .description(wind + "," + temperature).build();
    }

    private FullItemImpl createQueryingItem() {
        FullItemImpl.Builder builder = new FullItemImpl.Builder("Weather Query");
        return builder.iconInputStream(getInputStreamFromClasspath("weather/querying.png")).description("querying").build();
    }

    private void showQuerying() {
        layout.updateItems(createQueryingItem());
    }

    private void showError(JSONObj weatherData) {
        FullItemImpl errorItem = new FullItemImpl.Builder("Query error") //
                .description(weatherData.getString("status")) //
                .iconInputStream(getInputStreamFromClasspath("weather/unknown.png")).build();
        layout.updateItems(errorItem);
    }

    protected void triggerHook() throws Exception {
        queryWeather("河源");
    }

    private void queryWeather(String city) {
        showQuerying();
        JSONObj json = JSONUtils.getJSON(url + city);
        int code = json.getInt("error");
        if (code == 0) {
            JSONArr results = json.getJSONArr("results");
            if (results != null && results.size() > 0) {
                List<FullItemImpl> resultItems = new ArrayList<>();
                JSONObj result = (JSONObj) results.get(0);
                JSONArr weatherdatas = result.getJSONArr("weather_data");
                for (Object weatherdataObj : weatherdatas) {
                    JSONObj weatherdata = (JSONObj) weatherdataObj;
                    resultItems.add(createResult(weatherdata));
                }
                layout.updateItems(resultItems);
            }
        } else {
            showError(json);
        }
    }

    protected void filterHook(String content) throws Exception {
        if (content != null && content.trim().length() > 0) {
            queryWeather(content);
        }
    }

    @Override
    public String getName() {
        return "QueryWeather";
    }

}

