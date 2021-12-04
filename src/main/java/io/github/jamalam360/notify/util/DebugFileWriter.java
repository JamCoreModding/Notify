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

package io.github.jamalam360.notify.util;

import io.github.jamalam360.notify.NotifyLogger;
import io.github.jamalam360.notify.NotifyModInit;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.FileWriter;
import java.util.stream.Stream;

public class DebugFileWriter {
    public static void write() {
        StringBuilder sb = new StringBuilder();

        sb.append("Number of non-blacklisted mods: ").append(NotifyModInit.statistics.getTotalModCount());
        sb.append("\n");
        sb.append("Number of compatible mods: ").append(NotifyModInit.statistics.getTotalSupportedModCount());
        sb.append("\n");
        sb.append("Percentage coverage: ").append(Math.round(NotifyModInit.statistics.getPercentageCoverage() * 100) / 100D).append("%");
        sb.append("\n");
        sb.append("\n");
        sb.append("Number of mods with possible Modrinth support: ").append(getModrinthSupportingMods().count())
                .append(" (").append(((double) getModrinthSupportingMods().count() / (double) NotifyModInit.statistics.getTotalModCount()) * 100D).append("% of non-blacklisted mods)");
        sb.append("\n");
        sb.append("Number of mods with possible CurseForge support: ").append(getCurseForgeSupportingMods().count())
                .append(" (").append(((double) getCurseForgeSupportingMods().count() / (double) NotifyModInit.statistics.getTotalModCount()) * 100D).append("% of non-blacklisted mods)");
        sb.append("\n");
        sb.append("Number of mods with possible Notify JSON support: ").append(getJsonSupportingMods().count())
                .append(" (").append(((double) getJsonSupportingMods().count() / (double) NotifyModInit.statistics.getTotalModCount()) * 100D).append("% of non-blacklisted mods)");
        sb.append("\n");
        sb.append("Number of mods with possible Notify gradle.properties support: ").append(getGradlePropertiesSupportingMods().count())
                .append(" (").append(((double) getGradlePropertiesSupportingMods().count() / (double) NotifyModInit.statistics.getTotalModCount()) * 100D).append("% of non-blacklisted mods)");

        sb.append("\n");
        sb.append("\n");

        sb.append("Number of mods resolved with: ");
        sb.append("\n");
        sb.append("    Modrinth: ").append(NotifyModInit.statistics.getModrinthResolvedCount())
                .append(" (").append((NotifyModInit.statistics.getModrinthResolvedCount() / NotifyModInit.statistics.getTotalSupportedModCount()) * 100).append("% of supported mods)");
        sb.append("\n");
        sb.append("    CurseForge: ").append(NotifyModInit.statistics.getCurseForgeResolvedCount())
                .append(" (").append((NotifyModInit.statistics.getCurseForgeResolvedCount() / NotifyModInit.statistics.getTotalSupportedModCount()) * 100).append("% of supported mods)");
        sb.append("\n");
        sb.append("    gradle.properties: ");
        sb.append("\n");
        sb.append("        Explicit: ").append(NotifyModInit.statistics.getSpecifiedGradlePropertiesResolvedCount())
                .append(" (").append((NotifyModInit.statistics.getSpecifiedGradlePropertiesResolvedCount() / NotifyModInit.statistics.getTotalSupportedModCount()) * 100).append("% of supported mods)");
        sb.append("\n");
        sb.append("        Inferred: ").append(NotifyModInit.statistics.getNonSpecifiedGradlePropertiesResolvedCount())
                .append(" (").append((NotifyModInit.statistics.getNonSpecifiedGradlePropertiesResolvedCount() / NotifyModInit.statistics.getTotalSupportedModCount()) * 100).append("% of supported mods)");
        sb.append("\n");
        sb.append("    Notify JSON: ").append(NotifyModInit.statistics.getJsonResolvedCount())
                .append(" (").append((NotifyModInit.statistics.getJsonResolvedCount() / NotifyModInit.statistics.getTotalSupportedModCount()) * 100).append("% of supported mods)");

        sb.append("\n");
        sb.append("\n");

        FabricLoader.getInstance().getAllMods().stream().filter(m -> !Utils.isIgnored(m)).forEach(m -> appendMod(sb, m));

        try {
            FileWriter writer = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve("notify").resolve("notify_dump.txt").toFile(), false);
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            NotifyLogger.warn(false, "Failed to write debug file");
        }
    }

    private static void appendMod(StringBuilder sb, ModContainer mod) {
        sb.append(mod.getMetadata().getId()).append(":");
        sb.append("\n");
        sb.append("    Notify Status: ").append(NotifyModInit.MOD_UPDATE_STATUS_MAP.get(mod.getMetadata().getId()).name());
        sb.append("\n");
        sb.append("    Actual Version: ").append(mod.getMetadata().getVersion().getFriendlyString());
        sb.append("\n");
        sb.append("    Modrinth Support: ").append(getModrinthSupportingMods().anyMatch(m -> m.getMetadata().getId().equals(mod.getMetadata().getId())));
        sb.append("\n");
        sb.append("    CurseForge Support: ").append(getCurseForgeSupportingMods().anyMatch(m -> m.getMetadata().getId().equals(mod.getMetadata().getId())));
        sb.append("\n");
        sb.append("    Notify JSON Support: ").append(getJsonSupportingMods().anyMatch(m -> m.getMetadata().getId().equals(mod.getMetadata().getId())));
        sb.append("\n");
        sb.append("    Explicit Gradle Properties Support: ").append(getGradlePropertiesSupportingMods().anyMatch(m -> m.getMetadata().getId().equals(mod.getMetadata().getId())));
        sb.append("\n");
    }

    private static Stream<ModContainer> getNonIgnoredMods() {
        return FabricLoader.getInstance().getAllMods().stream().filter(m -> !Utils.isIgnored(m));
    }

    private static Stream<ModContainer> getModrinthSupportingMods() {
        return getNonIgnoredMods().filter(m -> !Utils.getContactWithContent(m.getMetadata().getContact(), "modrinth").equals(""));
    }

    private static Stream<ModContainer> getCurseForgeSupportingMods() {
        return getNonIgnoredMods().filter(m -> !Utils.getContactWithContent(m.getMetadata().getContact(), "curseforge").equals(""));
    }

    private static Stream<ModContainer> getJsonSupportingMods() {
        return getNonIgnoredMods().filter(m -> m.getMetadata().containsCustomValue("notify_json"));
    }

    private static Stream<ModContainer> getGradlePropertiesSupportingMods() {
        return getNonIgnoredMods().filter(m -> m.getMetadata().containsCustomValue("notify_gradle_properties_url") && m.getMetadata().containsCustomValue("notify_gradle_properties_key"));
    }
}
