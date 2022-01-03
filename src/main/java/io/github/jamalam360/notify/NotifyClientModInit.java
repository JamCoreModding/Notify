package io.github.jamalam360.notify;

import com.terraformersmc.modmenu.util.mod.Mod;
import io.github.alkyaly.enumextender.EnumExtender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.LiteralText;

import java.util.Map;

/**
 * @author Jamalam360
 */
public class NotifyClientModInit implements ClientModInitializer {
    public static Mod.Badge UPDATE_BADGE;

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            UPDATE_BADGE = EnumExtender.addToEnum(Mod.Badge.class, null, "NOTIFY_UPDATE", Map.of(
                    "text", new LiteralText("Update Status"),
                    "outlineColor", 0xFF0000,
                    "fillColor", 0xFF0000,
                    "key", "null"
            ));
        }
    }
}
