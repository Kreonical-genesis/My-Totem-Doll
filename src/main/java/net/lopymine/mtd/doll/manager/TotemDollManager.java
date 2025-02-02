package net.lopymine.mtd.doll.manager;

import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.skin.provider.SkinProvider;
import net.lopymine.mtd.skin.provider.extended.MojangSkinProvider;
import net.lopymine.mtd.tag.manager.TagsSkinProviders;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TotemDollManager {

	public static TotemDollData getDoll(String nickname) {
		if (TagsSkinProviders.isProvider(nickname)) {
			return TagsSkinProviders.loadDollFromProvider(nickname);
		}
		return MojangSkinProvider.getInstance().getOrLoadDoll(nickname);
	}

	public static Set<String> getAllLoadedKeys() {
		Set<String> loaded = new HashSet<>();

		for (SkinProvider value : TagsSkinProviders.getSkinProvidersIds().values()) {
			loaded.addAll(value.getLoadedKeys());
		}

		loaded.addAll(MojangSkinProvider.getInstance().getLoadedKeys());

		return loaded;
	}

	public static Set<TotemDollData> getAllLoadedDolls() {
		Set<TotemDollData> loaded = new HashSet<>();

		for (SkinProvider value : TagsSkinProviders.getSkinProvidersIds().values()) {
			loaded.addAll(value.getLoadedDolls());
		}

		loaded.addAll(MojangSkinProvider.getInstance().getLoadedDolls());

		return loaded;
	}

	public static void reload(Consumer<Float> action) {
		List<SkinProvider> providers = new ArrayList<>(TagsSkinProviders.getSkinProvidersIds().values());
		providers.add(MojangSkinProvider.getInstance());

		Set<CompletableFuture<?>> list = new HashSet<>();
		long startMs = System.currentTimeMillis();

		for (SkinProvider provider : providers) {
			list.add(provider.reloadAll());
		}

		CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).thenApply((__) -> {
			action.accept((System.currentTimeMillis() - startMs) / 1000F);
			return null;
		});
	}

	public static void reload(String value, Consumer<Float> action) {
		long startMs = System.currentTimeMillis();

		SkinProvider skinProvider = TagsSkinProviders.getProviderFor(value);

		CompletableFuture<Void> completableFuture =
				skinProvider == null
						?
						MojangSkinProvider.getInstance().reload(value)
						:
						skinProvider.reload(value);

		if (completableFuture == null) {
			return;
		}

		completableFuture.thenApply((__) -> {
			action.accept((System.currentTimeMillis() - startMs) / 1000F);
			return null;
		});
	}
}
