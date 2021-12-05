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
import io.github.jamalam360.notify.util.NotifyLogger;
import io.github.jamalam360.notify.NotifyModInit;
import io.github.jamalam360.notify.resolver.VersionResolver;
import io.github.jamalam360.notify.util.Utils;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.version.VersionComparisonOperator;

import java.io.IOException;

/**
 * @author Jamalam360
 */
public class JsonFileResolver implements VersionResolver {
    @Override
    public boolean canResolve(ModMetadata metadata) {
        return metadata.containsCustomValue("notify_json");
    }

    @Override
    public Version resolveLatestVersion(ModMetadata metadata, String minecraftVersion) throws VersionParsingException, IOException {
        JsonReader reader = Utils.openJsonFromUrl(metadata.getCustomValue("notify_json").getAsString());
        Version minecraftVersionParsed = Version.parse(minecraftVersion);

        boolean notFound = true;
        Version finalResult = null;

        reader.beginObject();

        while (reader.hasNext() && notFound) {
            if (reader.peek() == JsonToken.NAME) {
                String objectName = reader.nextName();
                if (objectName.equals(metadata.getId())) {
                    if (reader.peek() == JsonToken.BEGIN_OBJECT) {
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
                                if (VersionComparisonOperator.SAME_TO_NEXT_MINOR.test(vers, minecraftVersionParsed)) {
                                    specificVersion = Version.parse(str);
                                }
                            }
                        }

                        finalResult = specificVersion == null ? wildcardVersion : specificVersion;
                        notFound = false;
                    }
                } else {
                    reader.skipValue();
                }
            }
        }

        reader.close();

        NotifyModInit.statistics.jsonMod();

        if (notFound || finalResult == null) {
            NotifyLogger.warn(false, "Mod %s provided a Notify JSON URL, but that ID does not exist in the JSON file", metadata.getId());
            return Version.parse("0.0.0");
        } else {
            return finalResult;
        }
    }
}
