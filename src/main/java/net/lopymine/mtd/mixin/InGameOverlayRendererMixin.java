package net.lopymine.mtd.mixin;

//? if >=1.21.6 {

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;I)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(ItemRenderer instance, ItemStack stack, ItemDisplayContext displayContext, int light, int overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int seed, Operation<Void> original) {
		if (!TotemDollRenderer.renderedFloatingDoll(matrices, stack, vertexConsumers, light, overlay)) {
			original.call(instance, stack, displayContext, light, overlay, matrices, vertexConsumers, world, seed);
		}
	}

}
//?}
