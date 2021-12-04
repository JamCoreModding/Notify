package io.github.jamalam360.notify.mixin;

import com.terraformersmc.modmenu.gui.ModsScreen;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import io.github.jamalam360.notify.NotifyModInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;
import java.util.Locale;

/**
 * @author Jamalam360
 */

@SuppressWarnings("rawtypes")
@Mixin(value = DescriptionListWidget.class, remap = false)
public abstract class DescriptionListWidgetMixin extends EntryListWidget {
    @Shadow
    @Final
    private ModsScreen parent;

    public DescriptionListWidgetMixin(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    @SuppressWarnings({"JavaReflectionInvocation", "unchecked"})
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod;getLicense()Ljava/util/Set;"
            )
    )
    public void notify$appendNotifyStatus(MatrixStack license, int author, int contributor, float authors, CallbackInfo ci) {
        /*
         * So let's talk about this.
         * First of all, we're using reflection because my access widener wasn't playing nice, and I couldn't be bothered with the hassle of it.
         * Second of all, nothing. No further discussion, if it works it's not stupid!
         *
         * So in conclusion: this works don't touch it.
         */

        String s = "  " + NotifyModInit.MOD_UPDATE_STATUS_MAP.get(this.parent.getSelectedEntry().mod.getId()).name().toLowerCase(Locale.ROOT);
        s = s.replaceFirst(String.valueOf(s.charAt(2)), String.valueOf(s.charAt(2)).toUpperCase(Locale.ROOT));

        try {
            Class<?> clazz = Class.forName("com.terraformersmc.modmenu.gui.widget.DescriptionListWidget$DescriptionEntry");
            Constructor c = clazz.getDeclaredConstructor(DescriptionListWidget.class, OrderedText.class, DescriptionListWidget.class);
            c.setAccessible(true);
            children().add(c.newInstance(this, LiteralText.EMPTY.asOrderedText(), this));
            children().add(c.newInstance(this, new LiteralText("Notify Status:").asOrderedText(), this));
            children().add(c.newInstance(this, new LiteralText(s).asOrderedText(), this));
        } catch (Exception ignored) { // This will probably never happen, so we can just ignore it anyways.
        }
    }
}
