package io.github.jamalam360.notify.resolver.api;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import io.github.jamalam360.notify.NotifyLogger;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionComparisonOperator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Jamalam360
 */
public class JsonFileResolver {
    private static final Gson GSON = new Gson();

    public static Version resolve(String url, String modId, Version minecraftVersion) throws IOException, VersionParsingException {
        JsonReader reader;

        URL versionsUrl = new URL(url);
        reader = GSON.newJsonReader(new InputStreamReader(versionsUrl.openStream()));

        reader.beginObject();
        boolean notFound = true;
        Version finalResult = null;

        while (reader.hasNext() && notFound) {
            if (reader.nextName().equals(modId)) {
                reader.beginObject();

                Version wildcardVersion = null;
                Version specificVersion = null;

                while (reader.hasNext() && reader.peek() != JsonToken.END_OBJECT) {
                    String name = reader.nextName();
                    String str = reader.nextString();

                    if (name.equals("*")) {
                        wildcardVersion = Version.parse(str);
                    } else {
                        Version vers = Version.parse(name);
                        if (VersionComparisonOperator.SAME_TO_NEXT_MAJOR.test(vers, minecraftVersion)) {
                            specificVersion = Version.parse(str);
                        }
                    }
                }

                finalResult = specificVersion == null ? wildcardVersion : specificVersion;
                notFound = false;
            }
        }

        reader.close();

        if (notFound || finalResult == null) {
            NotifyLogger.warn(false, "Mod %s provided a Notify JSON URL, but that ID does not exist in the JSON file", modId);
            return null;
        } else {
            return finalResult;
        }
}
}
