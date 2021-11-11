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

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifyModInit implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_NAME = "Notify";

    public static final Map<NotifyMod, NotifyVersionChecker.VersionComparisonResult> MOD_UPDATE_STATUS_MAP = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing " + MOD_NAME);

        List<NotifyMod> notifyMods = NotifyModFetcher.getModsWithNotify();

        for (NotifyMod notifyMod : notifyMods) {
            NotifyVersionChecker.VersionComparisonResult result = NotifyVersionChecker.checkVersion(notifyMod);

            if (result == NotifyVersionChecker.VersionComparisonResult.OUTDATED) {
                LOGGER.log(Level.INFO, "Mod " + notifyMod.modId() + " is outdated. The latest version is v" + NotifyVersionChecker.getLatestVersion(notifyMod).getFriendlyString() + ", while you have v" + NotifyVersionChecker.getCurrentVersion(notifyMod));
            } else {
                if (FabricLoader.getInstance().isDevelopmentEnvironment() && result == NotifyVersionChecker.VersionComparisonResult.UPDATED) {
                    LOGGER.log(Level.INFO, "Mod " + notifyMod.modId() + " is up to date. You are only seeing this message for debugging because you are in a development environment.");
                }
            }

            MOD_UPDATE_STATUS_MAP.put(notifyMod, result);
        }

        Map<String, String> statusMapPlain = new HashMap<>();
        MOD_UPDATE_STATUS_MAP.forEach((mod, result) -> statusMapPlain.put(mod.modId(), result.name()));
        FabricLoader.getInstance().getObjectShare().put("notify:notify_statuses", statusMapPlain);
    }
}