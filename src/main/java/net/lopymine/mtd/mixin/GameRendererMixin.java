package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.utils.abc.Badabums;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

//? if <=1.21.1 {
/*import net.minecraft.client.render.model.json.ModelTransformationMode;
 *///?}

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	//? if >=1.21.2 {

	@Shadow
	@Nullable
	private ItemStack floatingItem;


	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw(Ljava/util/function/Consumer;)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(DrawContext drawContext, Consumer<?> drawCallback, Operation<Void> original, @Local MatrixStack matrices) {
		if (TotemDollRenderer.canRender(this.floatingItem)) {
			drawContext.draw((sus) -> {
				if (!TotemDollRenderer.renderFloatingDoll(matrices, this.floatingItem, drawContext.vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV)) {
					original.call(drawContext, drawCallback);
				}
			});
		} else {
			original.call(drawContext, drawCallback);
		}
	}

	//?} elif >=1.21 {

	/*@Shadow
	@Nullable
	private ItemStack floatingItem;

	@SuppressWarnings("deprecation")
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw(Ljava/lang/Runnable;)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(DrawContext drawContext, Runnable drawCallback, Operation<Void> original, @Local MatrixStack matrices) {
		if (TotemDollRenderer.canRender(this.floatingItem)) {
			drawContext.draw(() -> {
				if (!TotemDollRenderer.renderFloatingDoll(matrices, this.floatingItem, drawContext.vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV)) {
					original.call(drawContext, drawCallback);
				}
			});
		} else {
			original.call(drawContext, drawCallback);
		}
	}

	*///?} else {

	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;I)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(ItemRenderer itemRenderer, ItemStack stack, ModelTransformationMode transformationType, int light, int overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int seed, Operation<Void> original) {
		if (TotemDollRenderer.canRender(stack)) {
			TotemDollRenderer.renderFloatingDoll(matrices, stack, vertexConsumers, light, overlay);
		} else {
			original.call(itemRenderer, stack, transformationType, light, overlay, matrices, vertexConsumers, world, seed);
		}
	}

	*///?}
}

