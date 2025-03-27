package net.lopymine.mtd.extension;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.lopymine.mtd.doll.data.*;
import net.lopymine.mtd.doll.manager.*;
import net.lopymine.mtd.tag.manager.TagsManager;
import net.lopymine.mtd.utils.mixin.*;

import org.jetbrains.annotations.Nullable;

public class ItemStackExtension {

	@Nullable
	public static Text getRealCustomName(ItemStack itemStack) {
		//? if >=1.21 {
		return itemStack.get(net.minecraft.component.DataComponentTypes.CUSTOM_NAME);
		//?} else {
		/*net.minecraft.nbt.NbtCompound nbtCompound = itemStack.getSubNbt("display");
		if (nbtCompound != null && nbtCompound.contains("Name", 8)) {
			try {
				Text text = net.minecraft.text.Text.Serializer.fromJson(nbtCompound.getString("Name"));
				if (text != null) {
					return text;
				}

				nbtCompound.remove("Name");
			} catch (Exception var3) {
				nbtCompound.remove("Name");
			}
		}

		return null;
		*///?}
	}

	public static TotemDollData getTotemDollData(ItemStack stack) {
		Text customName = getRealCustomName(stack);

		if (customName != null) {
			String o = TagsManager.getNicknameOrSkinProviderFromName(customName.getString());
			TotemDollData data = TotemDollManager.getDoll(o);

			data.refreshBeforeRendering();

			String tags = TagsManager.getTagsFromName(customName.getString());
			if (tags != null) {
				TagsManager.processTags(tags, data);
			}

			return data;
		}

		return StandardTotemDollManager.getStandardDoll().refreshBeforeRendering();
	}

	public static void setModdedModel(ItemStack itemStack, boolean modded) {
		((ItemStackWithModdedBakedModel) itemStack).myTotemDoll$setModdedModel(modded);
	}

	public static boolean hasModdedModel(ItemStack itemStack) {
		return ((ItemStackWithModdedBakedModel) itemStack).myTotemDoll$isModdedModel();
	}

	public static void setPlayerEntity(ItemStack itemStack, AbstractClientPlayerEntity playerEntity) {
		((ItemStackWithPlayerEntity) itemStack).myTotemDoll$setPlayerEntity(playerEntity);
	}

	public static AbstractClientPlayerEntity getPlayerEntity(ItemStack itemStack) {
		return ((ItemStackWithPlayerEntity) itemStack).myTotemDoll$getPlayerEntity();
	}

}
