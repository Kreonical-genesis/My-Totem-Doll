package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget.Entry;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.mtd.gui.widget.list.ListWithStaticHeaderWidget;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin {

	@Shadow protected int headerHeight;

	@Shadow protected abstract int getEntryCount();

	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;enableScissor(Lnet/minecraft/client/gui/DrawContext;)V"), method = "renderWidget")
	private boolean disableScissorEnabling(EntryListWidget<?> instance, DrawContext context) {
		return !(((EntryListWidget<?>) (Object) this) instanceof ListWithStaticHeaderWidget<?>);
	}

	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;disableScissor()V"), method = "renderWidget")
	private boolean disableScissorDisabling(DrawContext instance) {
		return !(((EntryListWidget<?>) (Object) this) instanceof ListWithStaticHeaderWidget<?>);
	}

	@Inject(at = @At(value = "HEAD"), method = "getEntryAtPosition", cancellable = true)
	private void calibrate(double x, double y, CallbackInfoReturnable<Entry<?>> cir) {
		if (((EntryListWidget<?>) (Object) this) instanceof ListWithStaticHeaderWidget<? extends Entry<?>> widget) {
			int yPosAtWidget = (int) Math.floor(y - widget.getWidgetY());
			if (yPosAtWidget < widget.getHeightOfStaticHeader() || yPosAtWidget > widget.getHeightOfStaticHeader() + widget.getListHeight()) {
				cir.setReturnValue(null);
				return;
			}

			int i = widget.getRowWidth() / 2;
			int j = widget.getX() + widget.getWidth() / 2;
			int k = j - i;
			int l = j + i;
			int m = MathHelper.floor(y - (double)widget.getY()) - this.headerHeight + (int) widget.getScrollAmount();
			int n = m / widget.getItemHeight();
			cir.setReturnValue(x >= (double)k && x <= (double)l && n >= 0 && m >= 0 && n < this.getEntryCount() ? widget.children().get(n) : null);
		}
	}
}
