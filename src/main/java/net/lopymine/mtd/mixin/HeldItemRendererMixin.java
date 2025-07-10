package net.lopymine.mtd.mixin;

//? if <=1.21.3 {

/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import net.lopymine.mtd.extension.ItemStackExtension;

//? <=1.21.1
/^import net.minecraft.client.render.model.json.ModelTransformationMode;^/

@ExtensionMethod(ItemStackExtension.class)
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

	//? <=1.21.1 {
	/^@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V"), method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private void render(ItemRenderer instance, LivingEntity entity, ItemStack item, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, Operation<Void> original) {
	^///?} elif <=1.21.3 {
	/^@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V"), method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private void render(ItemRenderer instance, LivingEntity entity, ItemStack item, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, Operation<Void> original) {
		^///?}
		//? if <=1.21.3 {
		/^if (entity instanceof AbstractClientPlayerEntity playerEntity) {
			item.setPlayerEntity(playerEntity);
		}
		original.call(instance, entity, item, renderMode, leftHanded, matrices, vertexConsumers, world, light, overlay, seed);
		item.setPlayerEntity(null);
	}
	^///?}
}

*///?}