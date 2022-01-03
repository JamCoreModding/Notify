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

package io.github.jamalam360.notify.mixin.modmenu;

import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.ModBadgeRenderer;
import io.github.jamalam360.notify.ModMenuEnumExtender;
import io.github.jamalam360.notify.NotifyModInit;
import io.github.jamalam360.notify.resolver.NotifyVersionChecker;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Needed to dynamically change the color and text of badges in Mod Menu, depending on the update status
 *
 * @author Jamalam360
 */

@SuppressWarnings("DuplicatedCode")
@Mixin(value = ModBadgeRenderer.class, remap = false)
public abstract class ModBadgeRendererMixin {
    @Shadow
    protected Mod mod;

    @Unique
    private Text notify$capturedText = null;

    //region Development Environment Redirects
    @Group(name = "notify$drawBadgeRedirect")
    @Dynamic("Modifying Class From ModMenu")
    @Redirect(
            method = "drawBadge(Lnet/minecraft/client/util/math/MatrixStack;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/ModBadgeRenderer;drawBadge(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;IIII)V"
            )
    )
    public void notify$dev$drawBadgeRedirect(ModBadgeRenderer instance, MatrixStack matrixStack, OrderedText text, int outlineColor, int fillColor, int mouseX, int mouseY) {
        if (notify$capturedText.asString().equals("Updated")) {
            if (NotifyModInit.getConfig().displayUpdatedBadge) {
                instance.drawBadge(matrixStack, text, outlineColor, fillColor, mouseX, mouseY);
            }
        } else if (notify$capturedText.asString().equals("Unsupported")) {
            if (NotifyModInit.getConfig().displayUnsupportedBadge) {
                instance.drawBadge(matrixStack, text, outlineColor, fillColor, mouseX, mouseY);
            }
        } else {
            instance.drawBadge(matrixStack, text, outlineColor, fillColor, mouseX, mouseY);
        }
    }

    @Group(name = "notify$getTextRedirect")
    @Dynamic("Modifying Class From ModMenu")
    @Redirect(
            method = "drawBadge(Lnet/minecraft/client/util/math/MatrixStack;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getText()Lnet/minecraft/text/Text;"
            )
    )
    public Text notify$dev$getTextRedirect(Mod.Badge instance) {
        Text returnValue = new LiteralText("");

        if (instance == ModMenuEnumExtender.UPDATE_BADGE) {
            NotifyVersionChecker.VersionComparisonResult version = NotifyModInit.MOD_UPDATE_STATUS_MAP.get(this.mod.getId());

            switch (version) {
                case UPDATED -> returnValue = new LiteralText("Updated");
                case OUTDATED -> returnValue = new LiteralText("Update Available");
                case UNSUPPORTED -> returnValue = new LiteralText("Unsupported");
                default -> returnValue = new LiteralText("Failed to Fetch Version");
            }
        } else if (instance != null) {
            returnValue = instance.getText();
        }

        notify$capturedText = returnValue;
        return returnValue;
    }

    @Group(name = "notify$getOutlineColorRedirect")
    @Dynamic("Modifying Class From ModMenu")
    @Redirect(
            method = "drawBadge(Lnet/minecraft/client/util/math/MatrixStack;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getOutlineColor()I"
            )
    )
    public int notify$dev$getOutlineColorRedirect(Mod.Badge instance) {
        if (instance == ModMenuEnumExtender.UPDATE_BADGE) {
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
        } else if (instance != null) {
            return instance.getOutlineColor();
        } else {
            return 0;
        }
    }

    @Group(name = "notify$getFillColorRedirect")
    @Dynamic("Modifying Class From ModMenu")
    @Redirect(
            method = "drawBadge(Lnet/minecraft/client/util/math/MatrixStack;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getFillColor()I",
                    args = {"log=true"}
            )
    )
    public int notify$dev$getFillColorRedirect(Mod.Badge instance) {
        if (instance == ModMenuEnumExtender.UPDATE_BADGE) {
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
        } else if (instance != null) {
            return instance.getFillColor();
        } else {
            return 0;
        }
    }
    //endregion

    //region Production Environment Redirects
    @Group(name = "notify$drawBadgeRedirect")
    @Dynamic("Modifying Class From ModMenu")
    @Redirect(
            method = "drawBadge(Lnet/minecraft/class_4587;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/ModBadgeRenderer;drawBadge(Lnet/minecraft/class_4587;Lnet/minecraft/class_5481;IIII)V"
            )
    )
    public void notify$production$drawBadgeRedirect(ModBadgeRenderer instance, MatrixStack matrixStack, OrderedText text, int outlineColor, int fillColor, int mouseX, int mouseY) {
        if (notify$capturedText.asString().equals("Updated")) {
            if (NotifyModInit.getConfig().displayUpdatedBadge) {
                instance.drawBadge(matrixStack, text, outlineColor, fillColor, mouseX, mouseY);
            }
        } else if (notify$capturedText.asString().equals("Unsupported")) {
            if (NotifyModInit.getConfig().displayUnsupportedBadge) {
                instance.drawBadge(matrixStack, text, outlineColor, fillColor, mouseX, mouseY);
            }
        } else {
            instance.drawBadge(matrixStack, text, outlineColor, fillColor, mouseX, mouseY);
        }
    }

    @Group(name = "notify$getTextRedirect")
    @Dynamic("Modifying Class From ModMenu")
    @Redirect(
            method = "drawBadge(Lnet/minecraft/class_4587;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getText()Lnet/minecraft/class_2561;"
            )
    )
    public Text notify$production$getTextRedirect(Mod.Badge instance) {
        Text returnValue = new LiteralText("");

        if (instance == ModMenuEnumExtender.UPDATE_BADGE) {
            NotifyVersionChecker.VersionComparisonResult version = NotifyModInit.MOD_UPDATE_STATUS_MAP.get(this.mod.getId());

            switch (version) {
                case UPDATED -> returnValue = new LiteralText("Updated");
                case OUTDATED -> returnValue = new LiteralText("Update Available");
                case UNSUPPORTED -> returnValue = new LiteralText("Unsupported");
                default -> returnValue = new LiteralText("Failed to Fetch Version");
            }
        } else if (instance != null) {
            returnValue = instance.getText();
        }

        notify$capturedText = returnValue;
        return returnValue;
    }

    @Group(name = "notify$getOutlineColorRedirect")
    @Dynamic("Modifying Class From ModMenu")
    @Redirect(
            method = "drawBadge(Lnet/minecraft/class_4587;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getOutlineColor()I"
            )
    )
    public int notify$production$getOutlineColorRedirect(Mod.Badge instance) {
        if (instance == ModMenuEnumExtender.UPDATE_BADGE) {
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
        } else if (instance != null) {
            return instance.getOutlineColor();
        } else {
            return 0;
        }
    }

    @Group(name = "notify$getFillColorRedirect")
    @Dynamic("Modifying Class From ModMenu")
    @Redirect(
            method = "drawBadge(Lnet/minecraft/class_4587;Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;getFillColor()I"
            )
    )
    public int notify$production$getFillColorRedirect(Mod.Badge instance) {
        if (instance == ModMenuEnumExtender.UPDATE_BADGE) {
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
        } else if (instance != null) {
            return instance.getFillColor();
        } else {
            return 0;
        }
    }
    //endregion
}
