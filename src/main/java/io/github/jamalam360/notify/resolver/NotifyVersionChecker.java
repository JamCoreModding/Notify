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

package io.github.jamalam360.notify.resolver;

import io.github.jamalam360.notify.NotifyLogger;
import io.github.jamalam360.notify.resolver.api.CurseForgeApiResolver;
import io.github.jamalam360.notify.resolver.api.GradlePropertiesResolver;
import io.github.jamalam360.notify.resolver.api.JsonFileResolver;
import io.github.jamalam360.notify.resolver.api.ModrinthApiResolver;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionComparisonOperator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamalam360
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class NotifyVersionChecker {
    private static final List<VersionResolver> RESOLVERS = new ArrayList<>();
    private static Version unsupportedVersion = null;

    static {
        // THESE ARE IN A SPECIFIC ORDER! This is to preserve an 'order of priority' if a mod supports multiple methods
        RESOLVERS.add(new JsonFileResolver());
        RESOLVERS.add(new GradlePropertiesResolver());
        RESOLVERS.add(new ModrinthApiResolver());
        RESOLVERS.add(new CurseForgeApiResolver());

        try {
            unsupportedVersion = Version.parse("0.0.0");
        } catch (VersionParsingException ignored) { // Will never happen ¯\_(ツ)_/¯.
        }
    }

    public static VersionComparisonResult checkVersion(ModContainer mod) {
        Version latestVersion = getLatestVersion(mod);

        if (VersionComparisonOperator.EQUAL.test(latestVersion, unsupportedVersion)) {
            return VersionComparisonResult.UNSUPPORTED;
        } else if (latestVersion != null) {
            return VersionComparisonOperator.EQUAL.test(mod.getMetadata().getVersion(), latestVersion) ? VersionComparisonResult.UPDATED : VersionComparisonResult.OUTDATED;
        } else {
            return VersionComparisonResult.FAILURE;
        }
    }

    public static Version getLatestVersion(ModContainer mod) {
        String minecraftVersion = FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString();

        for (VersionResolver resolver : RESOLVERS) {
            if (resolver.canResolve(mod.getMetadata())) {
                try {
                    return resolver.resolveLatestVersion(mod.getMetadata(), minecraftVersion);
                } catch (MalformedURLException e) {
                    NotifyLogger.warn(false, "Mod %s has a malformed URL", mod.getMetadata().getId());
                    return null;
                } catch (IOException e) {
                    NotifyLogger.warn(false, "Caught IO exception on mod %s", mod.getMetadata().getId());
                    return null;
                } catch (VersionParsingException e) {
                    NotifyLogger.warn(false, "Failed to parse version for mod %s", mod.getMetadata().getId());
                    return null;
                }
            }
        }

        return unsupportedVersion;
    }

    public enum VersionComparisonResult {
        UPDATED,
        OUTDATED,
        FAILURE,
        UNSUPPORTED
    }
}
