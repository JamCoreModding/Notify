package io.github.jamalam360.notify;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
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

            while (reader.hasNext() && notFound) {
                if (reader.nextName().equals(mod.modId())) {
                    notFound = false;
                }
            }

            if (notFound) {
                NotifyModInit.LOGGER.log(Level.WARN, "Could not find version for mod " + mod.modId() + ", but a version JSON URL was supplied");
            } else {
                return Version.parse(reader.nextString());
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
