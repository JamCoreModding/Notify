package io.github.jamalam360.notify.resolver.api;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jamalam360
 */
public class GradlePropertiesResolver {
    private static final String REGEX_BASE = "{{property_name}}=(\\d+\\.\\d+\\.\\d+)";

    public static Version resolve(String propertiesUrl, String propertyName) throws IOException, VersionParsingException {
        URL url = new URL(propertiesUrl);
        Pattern p = Pattern.compile(REGEX_BASE.replace("{{property_name}}", propertyName));
        Matcher matcher = p.matcher(IOUtils.toString(url.openStream(), Charset.defaultCharset()));
        matcher.find();
        return Version.parse(matcher.group(1));
    }
}
