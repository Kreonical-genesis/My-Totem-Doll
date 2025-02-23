package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.*;
import net.minecraft.client.RunArgs.QuickPlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.gui.screen.WelcomeScreen;

import java.util.List;
import java.util.function.Function;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	//? if >=1.21 {
	/*@Inject(at = @At("HEAD"), method = "createInitScreens")
	private void generated(List<Function<Runnable, Screen>> list, CallbackInfo ci) {
		MyTotemDollConfig config = MyTotemDollClient.getConfig();
		if (config.isFirstRun() || FabricLoader.getInstance().isDevelopmentEnvironment()) {
			list.add(WelcomeScreen::new);
			config.setFirstRun(false);
		}
	}
	*///?} else {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;onInitFinished(Lnet/minecraft/client/realms/RealmsClient;Lnet/minecraft/resource/ResourceReload;Lnet/minecraft/client/RunArgs$QuickPlay;)V"), method = "<init>")
	private void generated(MinecraftClient instance, RealmsClient realmsClient, ResourceReload resourceReload, QuickPlay quickPlay, Operation<Void> original) {
		Runnable runnable = () -> original.call(instance, realmsClient, resourceReload, quickPlay);

		MyTotemDollConfig config = MyTotemDollClient.getConfig();
		if (config.isFirstRun() || FabricLoader.getInstance().isDevelopmentEnvironment()) {
			instance.setScreen(new WelcomeScreen(runnable));
			config.setFirstRun(false);
		} else {
			runnable.run();
		}
	}
	//?}

}
