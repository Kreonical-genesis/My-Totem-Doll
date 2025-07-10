package net.lopymine.mtd.doll.renderer.special;

//? if >=1.21.6 {

import net.lopymine.mtd.MyTotemDoll;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.render.DiffuseLighting.Type;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;

public class ItemGuiElementRenderer extends SpecialGuiElementRenderer<ItemGuiRenderState> {

	public ItemGuiElementRenderer(Immediate vertexConsumers) {
		super(vertexConsumers);
	}

	@Override
	public Class<ItemGuiRenderState> getElementClass() {
		return ItemGuiRenderState.class;
	}

	@Override
	protected void render(ItemGuiRenderState state, MatrixStack matrices) {
		MinecraftClient client = MinecraftClient.getInstance();

		client.gameRenderer.getDiffuseLighting().setShaderLights(Type.ITEMS_FLAT);
		matrices.multiply(state.rotation());
		float size = state.size();
		matrices.scale(-size, -size, size);
		client.getItemRenderer().renderItem(state.stack(),
				ItemDisplayContext.FIXED,
				15728880,
				OverlayTexture.DEFAULT_UV,
				matrices,
				this.vertexConsumers,
				client.world,
				0
		);
	}

	@Override
	protected float getYOffset(int height, int windowScaleFactor) {
		return height / 2F;
	}

	@Override
	protected String getName() {
		return "%s-item-special-gui-renderer".formatted(MyTotemDoll.MOD_ID);
	}
}
//?}
