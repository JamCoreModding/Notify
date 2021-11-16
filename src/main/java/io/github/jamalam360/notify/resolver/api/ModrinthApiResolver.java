package io.github.jamalam360.notify.resolver.api;

import com.google.gson.stream.JsonReader;
import io.github.jamalam360.notify.util.JsonUtils;
import io.github.jamalam360.notify.util.WebUtils;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

import java.io.IOException;
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

    public static Version resolve(String modUrl) throws IOException, IllegalStateException, VersionParsingException {
        Matcher matcher = URL_PATTERN.matcher(modUrl);
        //noinspection ResultOfMethodCallIgnored
        matcher.matches();
        String versionsUrl = API_URL + VERSIONS.replace("{mod_id}", getModrinthId(matcher.group(6).substring(0, matcher.group(6).length() - 1)));

        JsonReader reader = WebUtils.openJson(versionsUrl);
        reader.beginArray();
        reader.beginObject();
        String result = JsonUtils.getString(reader, "version_number");
        reader.close();

        return Version.parse(result);
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
