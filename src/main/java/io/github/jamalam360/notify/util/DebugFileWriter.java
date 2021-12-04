package io.github.jamalam360.notify.util;

import io.github.jamalam360.notify.NotifyModInit;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.File;
import java.io.FileWriter;
import java.util.stream.Stream;

public class DebugFileWriter {
    public static void write() {
        StringBuilder sb = new StringBuilder();

        sb.append("Number of non-ignored mods: ").append(Utils.getLoadedNonIgnoredModCount());
        sb.append("\n");
        sb.append("Number of compatible mods: ").append(Utils.getNotifySupportedModCount());
        sb.append("\n");
        sb.append("Percentage coverage: ").append(NotifyModInit.MOD_COVERAGE);
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
            FileWriter writer = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve("notify_dump.txt").toFile(), false);
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
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
