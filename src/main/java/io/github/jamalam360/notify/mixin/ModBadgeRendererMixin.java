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

package io.github.jamalam360.notify.mixin;

import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.ModBadgeRenderer;
import io.github.jamalam360.notify.NotifyModInit;
import io.github.jamalam360.notify.resolver.NotifyVersionChecker;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Jamalam360
 */

@Mixin(ModBadgeRenderer.class)
public class ModBadgeRendererMixin {
    @Shadow(remap = false)
    protected Mod mod;

    @Redirect(
            method = "drawBadge(Lnet/minecraft/client/util/math/MatrixStack;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getText()Lnet/minecraft/text/Text;"
            ),
            remap = false
    )
    public Text notify$getTextRedirect(Mod.Badge instance) {
        if (instance == NotifyModInit.UPDATE_BADGE) {
            NotifyVersionChecker.VersionComparisonResult version = NotifyModInit.MOD_UPDATE_STATUS_MAP.get(this.mod.getId());

            switch (version) {
                case UPDATED -> {
                    return new LiteralText("Updated");
                }
                case OUTDATED -> {
                    return new LiteralText("Update Available");
                }
                default -> { // FAILURE, or nothing
                    return new LiteralText("Failed to Fetch Version");
                }
            }
        } else {
            return instance.getText();
        }
    }

    @Redirect(
            method = "drawBadge(Lnet/minecraft/client/util/math/MatrixStack;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getOutlineColor()I"
            ),
            remap = false
    )
    public int notify$getOutLineColorRedirect(Mod.Badge instance) {
        if (instance == NotifyModInit.UPDATE_BADGE) {
            NotifyVersionChecker.VersionComparisonResult version = NotifyModInit.MOD_UPDATE_STATUS_MAP.get(this.mod.getId());

            switch (version) {
                case UPDATED -> {
                    return 0xff107454;
                }
                case OUTDATED -> {
                    return 0xff6f6c6a;
                }
                default -> { // FAILURE, or nothing
                    return 0xff841426;
                }
            }
        } else {
            return instance.getOutlineColor();
        }
    }

    @Redirect(
            method = "drawBadge(Lnet/minecraft/client/util/math/MatrixStack;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getFillColor()I"
            ),
            remap = false
    )
    public int notify$getFillColorRedirect(Mod.Badge instance) {
        if (instance == NotifyModInit.UPDATE_BADGE) {
            NotifyVersionChecker.VersionComparisonResult version = NotifyModInit.MOD_UPDATE_STATUS_MAP.get(this.mod.getId());

            switch (version) {
                case UPDATED -> {
                    return 0xff093929;
                }
                case OUTDATED -> {
                    return 0xff31302f;
                }
                default -> { // FAILURE, or nothing
                    return 0xff530C17;
                }
            }
        } else {
            return instance.getFillColor();
        }
    }
}
