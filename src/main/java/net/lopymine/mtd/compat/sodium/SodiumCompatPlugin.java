package net.lopymine.mtd.compat.sodium;

import net.fabricmc.loader.api.*;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.compat.CompatPlugin;

public class SodiumCompatPlugin extends CompatPlugin {

	@Override
	protected String getCompatModId() {
		return "sodium";
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (!super.shouldApplyMixin(targetClassName, mixinClassName)) {
			return false;
		}

		boolean oldMixin = mixinClassName.equals("net.lopymine.mtd.mixin.sodium.ModelPartMixinMixin");
		boolean hotMixin = mixinClassName.equals("net.lopymine.mtd.mixin.sodium.CubeMixinMixin");

		if (hotMixin) {
			return !this.isCurrentVersionOlderThanHot();
		}

		if (oldMixin) {
			return this.isCurrentVersionOlderThanHot();
		}

		return true;
	}

	private boolean isCurrentVersionOlderThanHot() {
		FabricLoader fabricLoader = FabricLoader.getInstance();
		ModContainer modContainer = fabricLoader.getModContainer(this.getCompatModId()).orElseThrow();

		Version currentVersion = modContainer.getMetadata().getVersion();
		Version hotVersion = this.getHotSodiumVersion();

		// <6.0.0 (currentOlder == true)
		// ModelPartMixinMixin

		// >=6.0.0 (currentOlder == false)
		// CubeMixin

		boolean bl = currentVersion.compareTo(hotVersion) < 0;
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			MyTotemDollClient.LOGGER.info("Detected Sodium, current version older than hot: {}", bl);
		}
		return bl;
	}

	private Version getHotSodiumVersion() {
		try {
			return Version.parse("0.6.0+mc1.21.1");
		} catch (VersionParsingException e) {
			throw new RuntimeException(e);
		}
	}
}
