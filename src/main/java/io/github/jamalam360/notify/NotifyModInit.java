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

import com.terraformersmc.modmenu.util.mod.Mod;
import io.github.alkyaly.enumextender.EnumExtender;
import io.github.jamalam360.notify.config.ModConfig;
import io.github.jamalam360.notify.resolver.NotifyVersionChecker;
import io.github.jamalam360.notify.util.DebugFileWriter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.LiteralText;

import java.util.HashMap;
import java.util.Map;

public class NotifyModInit implements ModInitializer {
    public static final Map<String, NotifyVersionChecker.VersionComparisonResult> MOD_UPDATE_STATUS_MAP = new HashMap<>();
    public static Mod.Badge UPDATE_BADGE;
    public static NotifyStatistics statistics = null;

    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    @Override
    public void onInitialize() {
        register();

        NotifyLogger.info(false, "Checking versions...");
        long startTime = System.currentTimeMillis();

        for (ModContainer notifyMod : FabricLoader.getInstance().getAllMods()) {
            NotifyVersionChecker.VersionComparisonResult result = NotifyVersionChecker.checkVersion(notifyMod);

            switch (result) {
                case UPDATED -> NotifyLogger.info(
                        true,
                        "Mod %s is updated to the latest version",
                        notifyMod.getMetadata().getId()
                );
                case OUTDATED -> NotifyLogger.info(
                        false,
                        "Mod %s has updates available",
                        notifyMod.getMetadata().getId()
                );
                case FAILURE -> NotifyLogger.info(
                        true,
                        "Failed to get version of mod %s",
                        notifyMod.getMetadata().getId()
                );
            }

            NotifyModInit.MOD_UPDATE_STATUS_MAP.put(notifyMod.getMetadata().getId(), result);
        }

        NotifyErrorHandler.finishedResolving();

        Map<String, String> statusMapPlain = new HashMap<>();
        NotifyModInit.MOD_UPDATE_STATUS_MAP.forEach((modId, result) -> statusMapPlain.put(modId, result.name()));
        FabricLoader.getInstance().getObjectShare().put("notify:notify_statuses", statusMapPlain);

        statistics = new NotifyStatistics((int) (System.currentTimeMillis() - startTime));
        dumpInfoOnNextLaunchIfEnabled();

        NotifyLogger.info(false, "Notify has %s percent coverage of mods", Math.round(NotifyModInit.statistics.getPercentageCoverage() * 100) / 100D);
        NotifyLogger.info(false, "Notify took %d ms to resolve versions", statistics.getResolveTime());
    }

    private void register() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        UPDATE_BADGE = EnumExtender.addToEnum(Mod.Badge.class, null, "NOTIFY_UPDATE", Map.of(
                "text", new LiteralText("Update Status"),
                "outlineColor", 0xFF0000,
                "fillColor", 0xFF0000,
                "key", "null"
        ));
    }

    private void dumpInfoOnNextLaunchIfEnabled() {
        if (getConfig().dumpInfoOnLaunch) {
            NotifyLogger.info(false, "Dumping debug info to file...");
            DebugFileWriter.write();
        }
    }
}