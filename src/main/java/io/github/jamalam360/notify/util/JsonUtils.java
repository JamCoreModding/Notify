package io.github.jamalam360.notify.util;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

/**
 * @author Jamalam360
 */
public class JsonUtils {
    public static String getString(JsonReader reader, String key) throws IOException {
        String name = reader.nextName();
        while (!name.equals(key) && reader.hasNext()) {
            reader.skipValue();
            name = reader.nextName();
        }

        return reader.nextString();
    }
}
