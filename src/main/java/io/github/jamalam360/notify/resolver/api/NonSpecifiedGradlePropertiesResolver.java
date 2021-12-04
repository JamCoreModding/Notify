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

import io.github.jamalam360.notify.NotifyModInit;
import io.github.jamalam360.notify.resolver.VersionResolver;
import io.github.jamalam360.notify.util.Utils;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jamalam360
 */
public class NonSpecifiedGradlePropertiesResolver implements VersionResolver {
    private static final String REGEX_BASE = "^\\s*{property_name}\\s*=\\s*([a-zA-Z0-9.+-]*)";
    private static final String[] COMMON_KEYS = {"mod_version", "version", "modVersion"};
    private static final String[] COMMON_BRANCHES = {"main", "master"};

    private final Map<String, Version> versionModIdCache = new HashMap<>();

    @Override
    public boolean canResolve(ModMetadata metadata) {
        try {
            if (!NotifyModInit.getConfig().enableNonSpecifiedGradleProperties) return false;
        } catch (RuntimeException ignored) { //Thrown when config is not loaded because we are running a test
        }

        String ghUrl = Utils.getContactWithContent(metadata.getContact(), "github");

        if (ghUrl.equals("")) return false;
        String[] urlComponents = ghUrl.split("/");

        for (String branch : COMMON_BRANCHES) {
            String urlName = "https://raw.githubusercontent.com/" + urlComponents[3] + "/" + urlComponents[4] + "/" + branch + "/" + "gradle.properties";

            try {
                URL url = new URL(urlName);
                String gradlePropertiesContent = IOUtils.toString(url, Charset.defaultCharset());

                for (String key : COMMON_KEYS) {
                    try {
                        Pattern p = Pattern.compile(REGEX_BASE.replace("{property_name}", key), Pattern.MULTILINE);
                        Matcher m = p.matcher(gradlePropertiesContent);
                        if (m.find()) {
                            versionModIdCache.put(metadata.getId(), Version.parse(m.group(1)));
                            return true;
                        }
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        return false;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return false;
    }

    @Override
    public Version resolveLatestVersion(ModMetadata metadata, String minecraftVersion) throws VersionParsingException, IOException {
        NotifyModInit.statistics.nonSpecifiedGradlePropertiesMod();
        return versionModIdCache.get(metadata.getId());
    }
}
