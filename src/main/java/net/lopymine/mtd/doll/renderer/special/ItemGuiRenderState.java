package net.lopymine.mtd.doll.renderer.special;

//? if >=1.21.6 {

import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public record ItemGuiRenderState(
		@Nullable
		ItemStack stack,
		int x,
		int y,
		int width,
		int height,
		float size,
		Quaternionf rotation,
		@Nullable ScreenRect scissorArea,
		@Nullable ScreenRect bounds
) implements SpecialGuiElementRenderState {

	public ItemGuiRenderState(
			@Nullable
			ItemStack stack,
			int x,
			int y,
			int width,
			int height,
			float size,
			Quaternionf rotation,
			@Nullable ScreenRect scissorArea
	) {
		this(stack, x, y, width, height, size, rotation, scissorArea, SpecialGuiElementRenderState.createBounds(x, y, x + width, y + height, scissorArea));
	}

	@Override
	public int x1() {
		return this.x();
	}

	@Override
	public int x2() {
		return this.x1() + this.width();
	}

	@Override
	public int y1() {
		return this.y();
	}

	@Override
	public int y2() {
		return this.y1() + this.height();
	}

	@Override
	public float scale() {
		return 1.0F;
	}
}
//?}
