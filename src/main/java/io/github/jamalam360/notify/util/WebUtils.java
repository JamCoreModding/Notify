package io.github.jamalam360.notify.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Jamalam360
 */
public class WebUtils {
    private static final Gson GSON = new Gson();

    public static JsonReader openJson(String urlString) throws IOException {
        JsonReader reader = null;
        URL url = new URL(urlString);
        return GSON.newJsonReader(new InputStreamReader(url.openStream()));
    }
}
