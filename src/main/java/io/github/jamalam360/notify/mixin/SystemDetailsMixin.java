package io.github.jamalam360.notify.mixin;

import io.github.jamalam360.notify.NotifyErrorHandler;
import net.minecraft.util.SystemDetails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

/**
 * @author Jamalam360
 */

@Mixin(SystemDetails.class)
public abstract class SystemDetailsMixin {
    @Shadow
    public abstract void addSection(String string, Supplier<String> supplier);

    @Inject(at = @At("RETURN"), method = "<init>")
    public void notify$addCrashingMod(CallbackInfo info) {
        if (NotifyErrorHandler.hasError()) {
            addSection("Notify Mod Causing Resolution Error", NotifyErrorHandler::getErrorMod);
        }
    }
}
