package net.lopymine.mtd.utils;

public class LightningUtils {

	public static void disable3dLighting() {
		//? if >=1.21.6 {
		net.minecraft.client.MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(net.minecraft.client.render.DiffuseLighting.Type.ITEMS_FLAT);
		//?} else {
		/*net.minecraft.client.render.DiffuseLighting.disableGuiDepthLighting();
		 *///?}
	}

	public static void enable3dLighting() {
		//? if >=1.21.6 {
		net.minecraft.client.MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(net.minecraft.client.render.DiffuseLighting.Type.ITEMS_3D);
		//?} else {
		/*net.minecraft.client.render.DiffuseLighting.enableGuiDepthLighting();
		*///?}
	}
}
