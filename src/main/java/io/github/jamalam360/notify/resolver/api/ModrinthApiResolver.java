/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jamalam360
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.notify.resolver.api;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import io.github.jamalam360.notify.util.WebUtils;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jamalam360
 */
public class ModrinthApiResolver {
    private static final String API_URL = "https://api.modrinth.com";
    private static final String MODS = "/api/v1/mod/{mod_id}";
    private static final String VERSIONS = "/api/v1/mod/{mod_id}/version";

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern URL_PATTERN = Pattern.compile("^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$");

    public static Version resolve(String modUrl, String minecraftVersion) throws IOException, IllegalStateException, VersionParsingException {
        Matcher matcher = URL_PATTERN.matcher(modUrl);
        //noinspection ResultOfMethodCallIgnored
        matcher.matches();

        //List<String> versions = getModVersions(getModrinthId(matcher.group(6).substring(0, matcher.group(6).length() - 1)), minecraftVersion);
        List<String> versions = getModVersions("AANobbMI", minecraftVersion);
        //TODO Get actual game version rather than ID
        return Version.parse(versions.get(versions.size() - 1));
    }

    private static List<String> getModVersions(String modId, String minecraftVersion) throws IOException {
        List<String> versions = new ArrayList<>();

        String url = API_URL + VERSIONS.replace("{mod_id}", modId);
        JsonReader reader = WebUtils.openJson(url);

        reader.beginArray();
        boolean finished = false;
        String currentId = "";

        while (!finished) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            } else if (reader.peek() == JsonToken.END_OBJECT) {
                reader.endObject();
            } else if (reader.peek() == JsonToken.END_ARRAY) {
                finished = true;
            } else if (reader.peek() == JsonToken.NAME) {
                String name = reader.nextName();
                if (name.equals("id")) {
                    currentId = reader.nextString();
                } else if (name.equals("game_versions")) {
                    reader.beginArray();

                    while (reader.peek() != JsonToken.END_ARRAY) {
                        String version = reader.nextString();
                        if (version.equals(minecraftVersion)) {
                            versions.add(currentId);
                        }
                    }

                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
        }

        reader.close();
        return versions;
    }

    private static String getModrinthId(String modSlug) throws IOException {
        String modUrl = API_URL + MODS.replace("{mod_id}", modSlug);
        JsonReader reader = WebUtils.openJson(modUrl);

        reader.beginObject();

        String name = reader.nextName();
        while (!name.equals("id")) {
            name = reader.nextName();
        }

        String response = reader.nextString();
        reader.close();
        return response;
    }
}
