package net.lopymine.mtd.mixin.yacl;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.mtd.yacl.YACLConfigurationScreen;
import net.lopymine.mtd.yacl.custom.TransparencySprites;

import org.jetbrains.annotations.Nullable;

@Mixin(Screen.class)
public abstract class ScreenMixin {

	//? if >=1.20.5 {

	@WrapWithCondition(method = "renderBackground", at = @At(value = "INVOKE",
			target = /*? if >=1.21.6 {*/ "Lnet/minecraft/client/gui/screen/Screen;applyBlur(Lnet/minecraft/client/gui/DrawContext;)V"
			/*?} elif >=1.21.2 {*/
			/*"Lnet/minecraft/client/gui/screen/Screen;applyBlur()V"
			*//*?} else {*/
			/*"Lnet/minecraft/client/gui/screen/Screen;applyBlur(F)V"
			*//*?}*/))
	public boolean disableBlur(Screen instance /*? if >=1.21.6 {*/, DrawContext context /*?} elif <=1.21.1 {*/ /*, float v  *//*?}*/) {
		return YACLConfigurationScreen.notOpen(((Screen) (Object) this));
	}

	@ModifyArg(method = "renderDarkening(Lnet/minecraft/client/gui/DrawContext;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderBackgroundTexture(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;IIFFII)V"), index = 1)
	private Identifier swapBackgroundTexture(Identifier original) {
		if (YACLConfigurationScreen.notOpen(((Screen) (Object) this))) {
			return original;
		}
		return TransparencySprites.getMenuBackgroundTexture();
	}
	//?} else {

	
	/*@Shadow @Nullable
	public MinecraftClient client;

	@Inject(at = @At("HEAD"), method = "renderBackgroundTexture", cancellable = true)
	private void disableBackgroundTextureRendering(DrawContext context, CallbackInfo ci) {
		if (!YACLConfigurationScreen.notOpen(((Screen) (Object) this)) && this.client != null && this.client.world != null) {
			ci.cancel();
		}
	}

	@Unique
	private static final String INJECT_METHOD = /^? >=1.20.2 {^/ "renderInGameBackground" /^?} else {^/ /^"renderBackground" ^//^?}^/;

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(IIIIII)V"), method = INJECT_METHOD)
	private void swapBackgroundGradientColor(DrawContext context, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, Operation<Void> original) {
		if (!YACLConfigurationScreen.notOpen(((Screen) (Object) this)) && this.client != null && this.client.world != null) {
			original.call(context, startX, startY, endX, endY, 335544320, 335544320);
			return;
		}
		original.call(context, startX, startY, endX, endY, colorStart, colorEnd);
	}

	//? if <=1.20.1 {

	/^@Shadow public abstract void renderBackground(DrawContext context);

	@Inject(at = @At("HEAD"), method = "render")
	private void renderWithBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (!YACLConfigurationScreen.notOpen(((Screen)(Object)this))) {
			this.renderBackground(context);
		}
	}

	^///?}

	*///?}
}
