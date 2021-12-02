package io.github.jamalam360.notify.mixin;

import io.github.jamalam360.notify.NotifyModInit;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Shadow
    @Final
    private boolean doBackgroundFade;

    @Shadow
    private long backgroundFadeStart;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawStringWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 0
            )
    )
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (NotifyModInit.getConfig().renderMainMenuText) {
            drawStringWithShadow(
                    matrices,
                    this.textRenderer,
                    "Notify Coverage: " + NotifyModInit.MOD_COVERAGE,
                    3,
                    3,
                    16777215 | MathHelper.ceil(this.doBackgroundFade ? MathHelper.clamp(
                            (this.doBackgroundFade ? (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F) - 1.0F,
                            0.0F,
                            1.0F
                    ) : 255.0F) << 24
            );
        }
    }
}
