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
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.LiteralText;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class NotifyModInit implements ModInitializer {
    public static final Map<String, NotifyVersionChecker.VersionComparisonResult> MOD_UPDATE_STATUS_MAP = new HashMap<>();
    public static Mod.Badge UPDATE_BADGE;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        NotifyLogger.info(false, "Checking versions...");

        for (ModContainer notifyMod : FabricLoader.getInstance().getAllMods()) {
            NotifyVersionChecker.VersionComparisonResult result = NotifyVersionChecker.checkVersion(notifyMod);

            if (result == NotifyVersionChecker.VersionComparisonResult.OUTDATED) {
                NotifyLogger.info(
                        false,
                        "Mod %s has updates available",
                        notifyMod.getMetadata().getId()
                );
            } else if (result == NotifyVersionChecker.VersionComparisonResult.UPDATED) {
                NotifyLogger.info(
                        true,
                        "Mod %s is updated to the latest version",
                        notifyMod.getMetadata().getId()
                );
            } else if (result == NotifyVersionChecker.VersionComparisonResult.FAILURE) {
                NotifyLogger.info(
                        true,
                        "Failed to get version of mod %s",
                        notifyMod.getMetadata().getId()
                );
            }

            NotifyModInit.MOD_UPDATE_STATUS_MAP.put(notifyMod.getMetadata().getId(), result);
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

        // This is hell.
        // I'm sorry.
        // I'm so sorry.
        // I'm so very sorry.
        // I'm so very very sorry.
        // I'm so very very very sorry.
        // I'm so very very very very sorry.
        // That chain of comments is the reason why I'm so very very very sorry.
        // I'm so very very very very very sorry.

        // ^^ GitHub Copilot generated that comment, it was too funny to get rid of. Seriously though this is hell, PRs welcome!
        NotifyLogger.info(false, "Notify has %s percent coverage of mods",
                new DecimalFormat("##.##")
                        .format(
                                ((double) MOD_UPDATE_STATUS_MAP.entrySet().stream().filter(
                                        e -> e.getValue() != NotifyVersionChecker.VersionComparisonResult.UNSUPPORTED).count()
                                ) / ((double) FabricLoader.getInstance().getAllMods().stream().filter(m -> m.getMetadata().getAuthors().stream().anyMatch(p -> !p.getName().equals("FabricMC")) && !m.getMetadata().getId().equals("minecraft")).count()) * 100));

        NotifyErrorHandler.finishedResolving();
    }

    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}