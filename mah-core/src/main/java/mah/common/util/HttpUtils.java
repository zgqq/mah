package mah.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zgq on 2017-01-09 17:30
 */
public class HttpUtils {

    public static String getContent(String urlStr) {
        String content = null;
        try {
            content = IOUtils.toString(getInputStream(urlStr));
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getInputStream(String urlStr) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            return inputStream;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
