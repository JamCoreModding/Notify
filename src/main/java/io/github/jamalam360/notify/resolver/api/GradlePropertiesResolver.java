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

import io.github.jamalam360.notify.resolver.VersionResolver;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jamalam360
 */
public class GradlePropertiesResolver implements VersionResolver {
    private static final String REGEX_BASE = "{property_name}\\s*=\\s*(\\d+\\.\\d+\\.\\d+)";

    @Override
    public boolean canResolve(ModMetadata metadata) {
        return metadata.containsCustomValue("notify_gradle_properties_url") && metadata.containsCustomValue("notify_gradle_properties_key");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public Version resolveLatestVersion(ModMetadata metadata, String minecraftVersion) throws VersionParsingException, IOException {
        URL url = new URL(metadata.getCustomValue("notify_gradle_properties_url").getAsString());
        Pattern p = Pattern.compile(REGEX_BASE.replace("{property_name}", metadata.getCustomValue("notify_gradle_properties_key").getAsString()));
        Matcher matcher = p.matcher(IOUtils.toString(url.openStream(), Charset.defaultCharset()));
        matcher.find();
        return Version.parse(matcher.group(1));
    }
}
