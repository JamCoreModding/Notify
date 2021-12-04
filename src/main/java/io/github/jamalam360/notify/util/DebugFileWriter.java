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
        sb.append("Percentage coverage: ").append(NotifyModInit.statistics.getPercentageCoverage()).append("%");
        sb.append("\n");
        sb.append("\n");
        sb.append("Number of mods with Modrinth support: ").append(getModrinthSupportingMods());
        sb.append("\n");
        sb.append("Number of mods with CurseForge support: ").append(getCurseForgeSupportingMods());
        sb.append("\n");
        sb.append("Number of mods with Notify JSON support: ").append(getJsonSupportingMods());
        sb.append("\n");
        sb.append("Number of mods with Notify gradle.properties support: ").append(getGradlePropertiesSupportingMods());

        try {
            FileWriter writer = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve("notify").resolve("notify_dump.txt").toFile(), false);
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            NotifyLogger.warn(false, "Failed to write debug file");
        }
    }

    private static Stream<ModContainer> getNonIgnoredMods() {
        return FabricLoader.getInstance().getAllMods().stream().filter(m -> !Utils.isIgnored(m));
    }

    private static long getModrinthSupportingMods() {
        return getNonIgnoredMods().filter(m -> !Utils.getContactWithContent(m.getMetadata().getContact(), "modrinth").equals("")).count();
    }

    private static long getCurseForgeSupportingMods() {
        return getNonIgnoredMods().filter(m -> !Utils.getContactWithContent(m.getMetadata().getContact(), "curseforge").equals("")).count();
    }

    private static long getJsonSupportingMods() {
        return getNonIgnoredMods().filter(m -> m.getMetadata().containsCustomValue("notify_json")).count();
    }

    private static long getGradlePropertiesSupportingMods() {
        return getNonIgnoredMods().filter(m -> m.getMetadata().containsCustomValue("notify_gradle_properties_url") && m.getMetadata().containsCustomValue("notify_gradle_properties_key")).count();
    }
}
