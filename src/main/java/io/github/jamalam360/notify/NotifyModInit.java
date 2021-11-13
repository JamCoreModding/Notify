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
import io.github.jamalam360.notify.resolver.NotifyMod;
import io.github.jamalam360.notify.resolver.NotifyModFetcher;
import io.github.jamalam360.notify.resolver.NotifyVersionChecker;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.LiteralText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class NotifyModInit implements ModInitializer {
    public static final Map<String, NotifyVersionChecker.VersionComparisonResult> MOD_UPDATE_STATUS_MAP = new HashMap<>();
    public static Mod.Badge UPDATE_BADGE;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        NotifyLogger.info(false, "Checking versions...");

        List<NotifyMod> notifyMods = NotifyModFetcher.getModsWithNotify();

        for (NotifyMod notifyMod : notifyMods) {
            NotifyVersionChecker.VersionComparisonResult result = NotifyVersionChecker.checkVersion(notifyMod);

            if (result == NotifyVersionChecker.VersionComparisonResult.OUTDATED) {
                if (NotifyModInit.getConfig().verboseLogging) {
                    NotifyLogger.info(
                            true,
                            "Mod %s has updates available; the latest version is v%s, while you have v%s (fetched latest version from %s)",
                            notifyMod.modId(),
                            NotifyVersionChecker.getLatestVersion(notifyMod).getFriendlyString(),
                            NotifyVersionChecker.getCurrentVersion(notifyMod),
                            notifyMod.versionsUrl()
                    );
                } else {
                    NotifyLogger.info(
                            false,
                            "Mod %s has updates available; the latest version is v%s, while you have v%s",
                            notifyMod.modId(),
                            NotifyVersionChecker.getLatestVersion(notifyMod).getFriendlyString(),
                            NotifyVersionChecker.getCurrentVersion(notifyMod)
                    );
                }
            } else {
                NotifyLogger.info(
                        true,
                        "Mod %s is updated to the latest version",
                        notifyMod.modId()
                );
            }

            NotifyModInit.MOD_UPDATE_STATUS_MAP.put(notifyMod.modId(), result);
        }

        Map<String, String> statusMapPlain = new HashMap<>();
        NotifyModInit.MOD_UPDATE_STATUS_MAP.forEach((modId, result) -> statusMapPlain.put(modId, result.name()));
        FabricLoader.getInstance().getObjectShare().put("notify:notify_statuses", statusMapPlain);

        UPDATE_BADGE = EnumExtender.addToEnum(Mod.Badge.class, null, "NOTIFY_UPDATE", Map.of(
                "text", new LiteralText("Update Status"),
                "outlineColor", 0xFF0000,
                "fillColor", 0xFF0000,
                "key", "null"
        ));
    }

    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}