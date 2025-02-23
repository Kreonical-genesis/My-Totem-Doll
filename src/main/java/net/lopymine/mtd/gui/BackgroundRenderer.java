package net.lopymine.mtd.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.mtd.utils.DrawUtils;
import net.lopymine.mtd.yacl.custom.TransparencySprites;

public class BackgroundRenderer {

	public static void drawTransparencyWidgetBackground(DrawContext context, int x, int y, int width, int height, boolean enabled, boolean hovered) {
		Identifier menuBackgroundTexture = enabled ? TransparencySprites.getMenuListBackgroundTexture() : TransparencySprites.DARKER_MENU_BACKGROUND_TEXTURE;
		Identifier menuSeparatorTexture = TransparencySprites.getMenuSeparatorTexture();
		drawTransparencyBackground(context, x, y, width, height, menuBackgroundTexture, menuSeparatorTexture, true, true, true, true);
		if (hovered) {
			context.drawBorder(x, y, width, height, -1);
		}
	}

	public static void drawTransparencyWidgetBackground(DrawContext context, int x, int y, int width, int height, boolean enabled, int borderColor) {
		Identifier menuBackgroundTexture = enabled ? TransparencySprites.getMenuListBackgroundTexture() : TransparencySprites.DARKER_MENU_BACKGROUND_TEXTURE;
		Identifier menuSeparatorTexture = TransparencySprites.getMenuSeparatorTexture();
		drawTransparencyBackground(context, x, y, width, height, menuBackgroundTexture, menuSeparatorTexture, true, true, true, true);
		context.drawBorder(x, y, width, height, borderColor);
	}

	public static void drawTransparencyBackground(DrawContext context, int x, int y, int width, int height, boolean list) {
		drawTransparencyBackground(context, x, y, width, height, list, true, true, true, true);
	}

	public static void drawTransparencyBackground(DrawContext context, int x, int y, int width, int height, boolean list, boolean up) {
		drawTransparencyBackground(context, x, y, width, height, list, up, true, true, true);
	}

	public static void drawTransparencyBackground(DrawContext context, int x, int y, int width, int height, boolean list, boolean up, boolean bottom) {
		drawTransparencyBackground(context, x, y, width, height, list, up, bottom, true, true);
	}

	public static void drawTransparencyBackground(DrawContext context, int x, int y, int width, int height, boolean list, boolean up, boolean bottom, boolean right) {
		drawTransparencyBackground(context, x, y, width, height, list, up, bottom, right, true);
	}

	public static void drawTransparencyBackground(DrawContext context, int x, int y, int width, int height, boolean list, boolean up, boolean bottom, boolean right, boolean left) {
		Identifier menuBackgroundTexture = list ? TransparencySprites.getMenuListBackgroundTexture() : TransparencySprites.getMenuBackgroundTexture();
		Identifier menuSeparatorTexture = TransparencySprites.getMenuSeparatorTexture();
		drawTransparencyBackground(context, x, y, width, height, menuBackgroundTexture, menuSeparatorTexture, up, bottom, right, left);
	}

	public static void drawTransparencyBackground(DrawContext context, int x, int y, int width, int height, Identifier backgroundTexture, Identifier separatorTexture, boolean up, boolean bottom, boolean right, boolean left) {

		MatrixStack matrices = context.getMatrices();

		RenderSystem.enableBlend();

		// BACKGROUND
		DrawUtils.drawTexture(context, backgroundTexture, x + 2, y + 2, 0, 0, width - 4, height - 4, 32, 32);

		// UP
		if (up) {
			DrawUtils.drawTexture(context, separatorTexture, x + 2, y,0, 0, width - 4, 2, 32, 2);
		}

		// BOTTOM
		if (bottom) {
			matrices.push();
			matrices.translate(x + width - 2, y + height, 0);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
			DrawUtils.drawTexture(context, separatorTexture, 0, 0, 0, 0, width - 4, 2, 32, 2);
			matrices.pop();
		}

		// RIGHT
		if (right) {
			matrices.push();
			matrices.translate(x + width, y + 1, 0);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
			DrawUtils.drawTexture(context, separatorTexture, 0, 0, 0f, 0f, height - 2, 2, 32, 2);
			matrices.pop();
		}

		// LEFT
		if (left) {
			matrices.push();
			matrices.translate(x, y + height - 1, 0);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
			DrawUtils.drawTexture(context, separatorTexture, 0, 0, 0f, 0f, height - 2, 2, 32, 2);
			matrices.pop();
		}

		// UP-LEFT CORNER
		if (up && left) {
			DrawUtils.drawTexture(context, separatorTexture, x, y, 0, 0, 2, 1, 1, 0);
		}

		// UP-RIGHT CORNER
		if (up && right) {
			DrawUtils.drawTexture(context, separatorTexture, x + width - 2, y, 0, 0, 2, 1, 1, 0);
		}

		// BOTTOM-LEFT CORNER
		if (bottom && left) {
			DrawUtils.drawTexture(context, separatorTexture, x, y + height - 1, 0, 0, 2, 1, 1, 0);
		}

		// BOTTOM-RIGHT CORNER
		if (bottom && right) {
			DrawUtils.drawTexture(context, separatorTexture, x + width - 2, y + height - 1, 0, 0, 2, 1, 1, 0);
		}

		RenderSystem.disableBlend();
	}
}
