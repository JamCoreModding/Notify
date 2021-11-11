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

package io.github.jamalam360.notify;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionComparisonOperator;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Jamalam360
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class NotifyVersionChecker {
    private static final Gson GSON = new Gson();

    public static Version getCurrentVersion(NotifyMod mod) {
        return FabricLoader.getInstance().getModContainer(mod.modId()).get().getMetadata().getVersion();
    }

    public static Version getMinecraftVersion() {
        return FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion();
    }

    public static VersionComparisonResult checkVersion(NotifyMod mod) {
        Version latestVersion = getLatestVersion(mod);

        if (latestVersion != null) {
            return VersionComparisonOperator.EQUAL.test(getCurrentVersion(mod), latestVersion) ? VersionComparisonResult.UPDATED : VersionComparisonResult.OUTDATED;
        } else {
            return VersionComparisonResult.FAILURE;
        }
    }

    public static Version getLatestVersion(NotifyMod mod) {
        JsonReader reader = null;

        try {
            URL versionsUrl = new URL(mod.versionsUrl());
            reader = GSON.newJsonReader(new InputStreamReader(versionsUrl.openStream()));

            reader.beginObject();
            boolean notFound = true;
            Version finalResult = null;

            while (reader.hasNext() && notFound) {
                if (reader.nextName().equals(mod.modId())) {
                    reader.beginObject();

                    Version wildcardVersion = null;
                    Version specificVersion = null;
                    Version minecraftVersion = getMinecraftVersion();

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

            if (notFound || finalResult == null) {
                NotifyModInit.LOGGER.log(Level.WARN, "Could not find version for mod " + mod.modId() + ", but a version JSON URL was supplied");
            } else {
                return finalResult;
            }

        } catch (MalformedURLException e) {
            NotifyModInit.LOGGER.log(Level.WARN, "Mod %s has a bad versions URL: %s".formatted(mod.modId(), mod.versionsUrl()));
        } catch (IOException e) {
            NotifyModInit.LOGGER.log(Level.WARN, "Caught exception reading version JSON for mod %s: %s".formatted(mod.modId(), mod.versionsUrl()));
        } catch (VersionParsingException e) {
            NotifyModInit.LOGGER.log(Level.WARN, "Could not parse version for mod %s: %s".formatted(mod.modId(), mod.versionsUrl()));
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    NotifyModInit.LOGGER.log(Level.WARN, "Caught exception closing JSON input stream for mod %s: %s".formatted(mod.modId(), mod.versionsUrl()));
                }
            }
        }

        return null;
    }

    public enum VersionComparisonResult {
        UPDATED,
        OUTDATED,
        FAILURE
    }
}
