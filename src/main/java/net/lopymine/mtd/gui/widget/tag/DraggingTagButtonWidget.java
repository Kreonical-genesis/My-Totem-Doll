package net.lopymine.mtd.gui.widget.tag;

import lombok.*;
import net.minecraft.client.gui.DrawContext;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.config.other.vector.Vec2i;
import net.lopymine.mtd.tag.Tag;

@Getter
@Setter
public class DraggingTagButtonWidget extends TagButtonWidget {

	private final int originalX;
	private final int originalY;
	private int originX;
	private int originY;
	private boolean dragging;

	public DraggingTagButtonWidget(Tag tag, int originX, int originY, int originalX, int originalY, int x, int y, TagPressAction pressAction) {
		super(tag, x, y, pressAction);
		this.originX   = originX;
		this.originY   = originY;
		this.originalX = originalX;
		this.originalY = originalY;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!this.over(mouseX, mouseY)) {
			return false;
		}
		if (this.isResetPosButton(button)) {
			this.resetPosition();
			return true;
		}
		if (this.isDraggingButton(button)) {
			this.setDragging(true);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void /*? if >=1.21 {*/ renderWidget /*?} else {*//*renderButton *//*?}*/(DrawContext context, int mouseX, int mouseY, float delta) {
		int x = this.isDragging() ? mouseX - (this.getWidth() / 2) : this.getX();
		int y = this.isDragging() ? mouseY - (this.getHeight() / 2): this.getY();
		super.renderButton(context, x, y);
		if (!this.isDragging()) {
			this.requestTooltip();
		}
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.isDragging() && this.isDraggingButton(button)) {
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.isDragging()) {
			this.setDragging(false);
			this.setDraggingPosition((int) mouseX, (int) mouseY);
			return true;
		}
		return false;
	}

	private void resetPosition() {
		this.setDraggingPosition(this.originalX, this.originalY);
	}

	private void setDraggingPosition(int draggingX, int draggingY) {
		MyTotemDollConfig config = MyTotemDollClient.getConfig();
		Vec2i pos = config.getTagButtonPos();

		pos.setX((draggingX - (this.getWidth() / 2)) - this.originX);
		pos.setY((draggingY - (this.getHeight() / 2)) - this.originY);

		config.save();

		this.setPosition(draggingX, draggingY);
	}

	private boolean isDraggingButton(int button) {
		return button == 1;
	}

	private boolean isResetPosButton(int button) {
		return button == 2;
	}
}
