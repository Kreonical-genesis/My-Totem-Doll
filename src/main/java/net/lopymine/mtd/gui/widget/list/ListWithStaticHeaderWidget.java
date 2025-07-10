package net.lopymine.mtd.gui.widget.list;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.gui.widget.ElementListWidget.Entry;

import net.lopymine.mtd.utils.ColorUtils;

@Getter
public abstract class ListWithStaticHeaderWidget<E extends Entry<E>> extends ElementListWidget<E> {

	protected final int heightOfStaticHeader;

	//? if <1.21 {
	/*public boolean visible = true;
	*///?}

	public ListWithStaticHeaderWidget(int x, int y, int width, int height, int itemHeight, int headerHeight) {
		super(MinecraftClient.getInstance(), width, height - headerHeight, y + headerHeight /*? if <1.21 {*//*, y + height *//*?}*/, itemHeight);
		this.setX(x);
		//? if <1.21 {
		/*this.setRenderBackground(false);
		this.setRenderHorizontalShadows(false);
		*///?}
		this.setRenderHeader(true, 0);
		this.heightOfStaticHeader = headerHeight;
	}

	//? >=1.21.4 {
	private void setRenderHeader(boolean bl, int height) {
		this.renderHeader = bl;
	}
	//?}

	//? if >=1.21 {
	@Override
	protected void drawHeaderAndFooterSeparators(DrawContext context) {
	}
	//?}

	//? if <1.21 {
	/*@Override
	protected void renderBackground(DrawContext context) {
		this.drawMenuListBackground(context);
	}

	protected void drawMenuListBackground(DrawContext context) {

	}
	*///?}

	//? if <1.21 {
	/*@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (!this.visible) {
			return;
		}
		super.render(context, mouseX, mouseY, delta);
	}
	*///?}

	@Override
	protected void renderList(DrawContext context, int mouseX, int mouseY, float delta) {
		//context.fill(this.left, this.top, this.right + 5, this.bottom, ColorUtils.getArgb(255, 0, 255));
		//context.fill(this.getX(), this.getListY(), this.getX() + this.getWidth() + 5, this.getListY() + this.getListHeight(), ColorUtils.getArgb(0, 0, 255));
		context.enableScissor(this.getX() - 5, this.getListY(), this.getX() + this.getWidth(), this.getListY() + this.getListHeight());
		super.renderList(context, mouseX, mouseY, delta);
		context.disableScissor();
	}

	public int getListY() {
		return this.getY();
	}

	public int getListHeight() {
		return this.getHeight();
	}

	public int getWidgetY() {
		return this.getListY() - this.heightOfStaticHeader;
	}

	public int getWidgetHeight() {
		return this.getHeightOfStaticHeader() + this.getListHeight();
	}

	@Override
	protected void renderHeader(DrawContext context, int x, int y) {
		//context.fill(this.getX() - 5, this.getWidgetY(), this.getRight(), this.getListY(), ColorUtils.getArgb(0, 0, 255));
		this.renderStaticHeader(context, x, this.getWidgetY());
	}

	protected abstract void renderStaticHeader(DrawContext context, int x, int y);

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseY >= (double)this.getWidgetY() && mouseY <= (double)this.getBottom() && mouseX >= (double)this.getX() && mouseX <= (double)this.getRight();
	}

	@Override
	/*? >=1.21.2 {*/ public/*?} else {*/ /*protected *//*?}*/ int getRowTop(int index) {
		return super.getRowTop(index) - 4;
	}

	public int getItemHeight() {
		return this.itemHeight;
	}

	public void setListScrollAmount(int i) {
		//? >=1.21.4 {
		this.setScrollY(i);
		//?} else {
		/*this.setScrollAmount(i);
		*///?}
	}

	//? >=1.21.4 {

	@Override
	protected boolean overflows() {
		return this.needScrollBar() && super.overflows();
	}

	//?} else {


	/*/^? if >=1.21 {^/@Override/^?}^/
	public boolean isScrollbarVisible() {
		return this.needScrollBar();
	}

	*///?}

	//? if <=1.20.1 {
	/*public int getX() {
		return this.left;
	}

	public int getY() {
		return this.top;
	}

	public void setX(int x) {
		this.left = x;
		this.right = this.left + this.getWidth();
	}

	public void setY(int y) {
		this.top = y + this.heightOfStaticHeader;
		this.bottom = this.top + this.getHeight();
	}

	public void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height - this.heightOfStaticHeader;
	}

	public int getBottom() {
		return this.getY() + this.getHeight();
	}

	public int getRight() {
		return this.getX() + this.getWidth();
	}

	*///?} else {

	@Override
	public void setHeight(int height) {
		super.setHeight(height - this.heightOfStaticHeader);
	}

	@Override
	public void setY(int y) {
		super.setY(y + this.heightOfStaticHeader);
	}

	//?}

	protected abstract boolean needScrollBar();
}
