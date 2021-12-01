package io.github.jamalam360.notify.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.metadata.ContactInformation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class Utils {
    static final Gson GSON = new Gson();

    public static String getContactWithContent(ContactInformation information, String content) {
        for (Map.Entry<String, String> entry : information.asMap().entrySet()) {
            if (entry.getValue().contains(content)) {
                return entry.getValue();
            }
        }

        return "";
    }

    public static String getKeyFromJson(JsonReader reader, String key) throws IOException {
        String name = reader.nextName();
        while (!name.equals(key) && reader.hasNext()) {
            reader.skipValue();
            name = reader.nextName();
        }

        return reader.nextString();
    }

    public static JsonReader openJsonFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        return GSON.newJsonReader(new InputStreamReader(url.openStream()));
    }
}
