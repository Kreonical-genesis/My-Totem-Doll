package net.lopymine.mtd.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;

import net.lopymine.mtd.utils.mixin.ItemStackWithPlayerEntity;


@Mixin(ItemStack.class)
public class ItemStackWithPlayerEntityMixin implements ItemStackWithPlayerEntity {

	@Unique
	private AbstractClientPlayerEntity player;

	@Override
	public void myTotemDoll$setPlayerEntity(AbstractClientPlayerEntity player) {
		this.player = player;
	}

	@Override
	public AbstractClientPlayerEntity myTotemDoll$getPlayerEntity() {
		return this.player;
	}
}
