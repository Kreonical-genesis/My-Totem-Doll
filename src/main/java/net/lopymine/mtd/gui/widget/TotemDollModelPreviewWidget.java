package net.lopymine.mtd.gui.widget;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.*;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.doll.manager.StandardTotemDollManager;
import net.lopymine.mtd.model.base.MModel;
import net.lopymine.mtd.model.bb.manager.BlockBenchModelManager;

@Getter
@Setter
public class TotemDollModelPreviewWidget extends ClickableWidget {

	private final float size;

	private final TotemDollData data;

	private boolean loading;
	private boolean failedLoading;

	public TotemDollModelPreviewWidget(int x, int y, float size) {
		super(x, y, (int) size, (int) size, Text.of(""));
		this.size = size;
		this.data = StandardTotemDollManager.getStandardDoll().copy();
	}

	@Override
	protected void /*? if >=1.21 {*/renderWidget/*?} else {*//*renderButton*//*?}*/(DrawContext context, int mouseX, int mouseY, float delta) {
		context.enableScissor(this.getX(), this.getY(), (this.getX() + this.getWidth()), (int) (this.getY() + this.getHeight()));
		if (this.loading) {
			this.renderLoadingText(context);
		} else {
			this.renderPreview(context);
		}
		context.disableScissor();
	}

	protected void renderLoadingText(DrawContext context) {
		int halfOfSize = (int) this.size / 2;
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		context.drawCenteredTextWithShadow(textRenderer, this.getLoadingText(Util.getMeasuringTimeMs()), this.getX() + halfOfSize, this.getY() + halfOfSize - (textRenderer.fontHeight / 2), -1);
	}

	protected void renderPreview(DrawContext context) {
		TotemDollRenderer.renderPreview(context, this.getX(), this.getY(), (int) this.getSize(), (int) this.getSize(), this.getSize(), this.getData().refreshAndApplyRenderProperties());
	}

	public void updateModel(Identifier id) {
		this.loading = true;
		BlockBenchModelManager.getModelAsyncAsResponse(id, (response) -> {
			if (!response.isEmpty()) {
				MModel value = response.value();
				this.loading = false;
				this.updateModel(value);
			} else {
				this.failedLoading = true;
			}
		});
	}

	public void updateModel(MModel model) {
		this.data.setStandardMModel(model);
	}

	private Text getLoadingText(long tick) {
		if (this.failedLoading) {
			return MyTotemDoll.text("text.loading.failed");
		}

		int i = (int) (tick / 300L % 4L);
		return MyTotemDoll.text("text.loading.%s".formatted(i));
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {

	}
}
