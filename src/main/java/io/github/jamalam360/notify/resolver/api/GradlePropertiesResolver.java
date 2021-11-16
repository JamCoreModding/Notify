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
