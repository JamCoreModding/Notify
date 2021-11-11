package io.github.jamalam360.notify;

import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamalam360
 */
public class NotifyModFetcher {
    public static List<NotifyMod> getModsWithNotify() {
        FabricLoader loader = FabricLoader.getInstance();
        List<NotifyMod> mods = new ArrayList<>();

        loader.getAllMods().forEach(mod -> {
            if (mod.getMetadata().containsCustomValue("notify_version_url")) {
                NotifyMod notifyMod = new NotifyMod(mod.getMetadata().getId(), mod.getMetadata().getCustomValue("notify_version_url").getAsString());
                mods.add(notifyMod);
            }
        });

        return mods;
    }
}
