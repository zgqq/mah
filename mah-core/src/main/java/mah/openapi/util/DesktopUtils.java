package mah.openapi.util;

/**
 * Created by zgq on 2017-01-10 17:58
 */
public final class DesktopUtils {

    private DesktopUtils() {

    }

    public static void openBrowser(String url) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                java.net.URI uri = java.net.URI.create(url);
                java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    dp.browse(uri);
                } else {
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("xdg-open " + url);
                }
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
