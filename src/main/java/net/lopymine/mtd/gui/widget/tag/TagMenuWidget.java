package net.lopymine.mtd.gui.widget.tag;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.*;

import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.extension.ItemStackExtension;
import net.lopymine.mtd.gui.widget.list.ListWithStaticHeaderWidget;
import net.lopymine.mtd.gui.widget.tag.TagMenuWidget.TagRow;
import net.lopymine.mtd.tag.*;
import net.lopymine.mtd.tag.manager.TagsManager;
import net.lopymine.mtd.utils.*;
import net.lopymine.mtd.utils.tooltip.IRequestableTooltipScreen;
import java.util.*;
import java.util.stream.*;
import org.jetbrains.annotations.*;

@ExtensionMethod(ItemStackExtension.class)
public class TagMenuWidget extends ListWithStaticHeaderWidget<TagRow> {

	public static final Identifier BACKGROUND = MyTotemDoll.id("textures/gui/tag_menu/background_new.png");

	public TagMenuWidget(int x, int y, NameApplier nameApplier) {
		super(x, y, 50, 156, 16, 35);

		List<Tag> list = TagsManager.getTags().values().stream().toList();
		for (int i = 0; i < list.size(); i += 2) {
			List<Tag> tags = getRangeOfList(list, i);
			List<TagButtonWidget> widgets = new ArrayList<>();
			for (Tag tag : tags) {
				TagButtonWidget tagButtonWidget = createTagButtonWidget(nameApplier, tag);
				widgets.add(tagButtonWidget);
			}
			this.addEntry(new TagRow(widgets));
		}

		List<CustomModelTag> customModelIds = TagsManager.getCustomModelIdsTags().values().stream().filter(tag -> tag.getTag() != 'm' && tag.getTag() != 'n').toList();
		if (!customModelIds.isEmpty()) {
			this.addEntry(new SeparatorRow(MyTotemDoll.text("tag_menu.custom_models.title")));
		}

		List<TagButtonWidget> allCustomModelWidgets = new ArrayList<>();
		for (int i = 0; i < customModelIds.size(); i += 2) {
			List<CustomModelTag> tags = getRangeOfList(customModelIds, i);
			List<TagButtonWidget> tagRowWidget = new ArrayList<>();
			for (CustomModelTag tag : tags) {
				CustomModelTagButtonWidget tagButtonWidget = createCustomModelTagButtonWidget(nameApplier, tag, allCustomModelWidgets);

				tagRowWidget.add(tagButtonWidget);
				allCustomModelWidgets.add(tagButtonWidget);
			}
			this.addEntry(new TagRow(tagRowWidget));
		}
	}

	@Override
	protected void renderStaticHeader(DrawContext context, int x, int y) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		ClickableWidget.drawScrollableText(context, textRenderer, MyTotemDoll.text("tag_menu.title"), this.getX() + 8, this.getWidgetY() + 13, this.getX() + this.getWidth() - 8, this.getWidgetY() + 13 + textRenderer.fontHeight, -1);
	}

	@Override
	public int getRowWidth() {
		return 30;
	}

	@Override
	public int getRowLeft() {
		return this.getX() + 10;
	}

	@Override
	public int getListHeight() {
		return super.getListHeight();
	}

	@Override
	protected void drawMenuListBackground(DrawContext context) {
//				if (this.getHoveredEntry() != null) {
//			context.fill(this.getX(), this.getY(), this.getRight() + 5, this.getBottom(), Argb.getArgb(0, 255, 0));
//		} else {
//			context.fill(this.getX(), this.getY(), this.getRight() + 5, this.getBottom(), Argb.getArgb(255, 0, 0));
//		}
		DrawUtils.drawTexture(context, BACKGROUND, this.getX(), this.getWidgetY(), 0, 0, 50, 166, 50, 166);
	}

	@Override
	protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, /*? if >=1.21 {*/ double horizontalAmount, /*?}*/ double verticalAmount) {
		TagRow entry = this.getEntryAtPosition(mouseX, mouseY);
		if (entry != null && entry.mouseScrolled(mouseX, mouseY, /*? if >=1.21 {*/horizontalAmount, /*?}*/ verticalAmount)) {
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, /*? if >=1.21 {*/horizontalAmount, /*?}*/ verticalAmount);
	}

	@Override
	public boolean needScrollBar() {
		return false;
	}

	public void updateButtons(ItemStack stack) {
		String tags = getTags(stack);

		for (TagButtonWidget widget : this.getAllTagButtons()) {
			if (tags != null) {
				widget.setPressed(tags.contains(widget.getText()));
			} else {
				widget.setPressed(false);
			}
		}
	}

	public void updateCustomModelTagButtons(ItemStack stack) {
		this.updateCustomModelTagButtonsData(stack);
	}

	private void updateCustomModelTagButtonsData(ItemStack stack) {
		for (CustomModelTagButtonWidget widget : this.getCustomModelTagButtons()) {
			widget.updateData(stack.getTotemDollData());
		}
	}

	private List<TagButtonWidget> getAllTagButtons() {
		return this.children()
				.stream()
				.map(TagRow::children)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	private List<CustomModelTagButtonWidget> getCustomModelTagButtons() {
		return this.children()
				.stream()
				.map(TagRow::children)
				.flatMap(Collection::stream)
				.flatMap((widget) -> {
					if (widget instanceof CustomModelTagButtonWidget tagButtonWidget) {
						return Stream.of(tagButtonWidget);
					}
					return Stream.empty();
				})
				.collect(Collectors.toList());
	}

	private static @NotNull TagButtonWidget createTagButtonWidget(NameApplier nameApplier, Tag tag) {
		char character = tag.getTag();

		TagButtonWidget tagButtonWidget = new TagButtonWidget(tag, 0, 0, (widget) -> {
			updateItemStackName(nameApplier, widget, character);
		});

		tagButtonWidget.setTooltip(TagsManager.getTagDescription(character));
		return tagButtonWidget;
	}

	private static @NotNull CustomModelTagButtonWidget createCustomModelTagButtonWidget(NameApplier nameApplier, CustomModelTag tag, List<TagButtonWidget> allCustomModelWidgets) {
		char character = tag.getTag();

		return new CustomModelTagButtonWidget(tag, 0, 0, (tagButtonWidget) -> {
			updateItemStackName(nameApplier, tagButtonWidget, character);

			for (TagButtonWidget widget : allCustomModelWidgets) {
				if (!widget.equals(tagButtonWidget)) {
					widget.setPressed(false);
					updateItemStackName(nameApplier, widget, widget.getTag().getTag());
				}
			}
		});
	}

	private static @NotNull <E> List<E> getRangeOfList(List<E> list, int startIndex) {
		List<E> tags = new ArrayList<>();
		tags.add(list.get(startIndex));
		if (startIndex + 1 < list.size()) {
			tags.add(list.get(startIndex + 1));
		}
		return tags;
	}

	private static void updateItemStackName(NameApplier nameApplier, TagButtonWidget b, char c) {
		String name = b.isPressed() ? TagsManager.addTag(nameApplier.getName(), c) : TagsManager.removeTag(nameApplier.getName(), c);
		nameApplier.setName(name);
	}

	@Nullable
	private static String getTags(ItemStack stack) {
		Text text = stack.getRealCustomName();
		if (text == null) {
			return null;
		}
		String customName = text.getString();
		return TagsManager.getTagsFromName(customName);
	}


	public interface NameApplier {

		String getName();

		void setName(String name);

	}

	public static class TagRow extends ElementListWidget.Entry<TagRow> {

		private final List<TagButtonWidget> buttons;

		public TagRow(List<TagButtonWidget> buttons) {
			this.buttons = buttons;
		}

		@Override
		public List<TagButtonWidget> selectableChildren() {
			return this.buttons;
		}

		@Override
		public List<TagButtonWidget> children() {
			return this.buttons;
		}

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			int xOffset = 0;

			for (TagButtonWidget widget : this.buttons) {
				widget.setPosition(x + xOffset, y);
				widget.setCanBeHovered(hovered);
				widget.render(context, mouseX, mouseY, tickDelta);
				xOffset += widget.getWidth() + 2;
			}
		}
	}

	public static class SeparatorRow extends TagRow {

		public static final Identifier SEPARATOR = MyTotemDoll.id("textures/gui/tag_menu/separator.png");

		private final Text text;

		public SeparatorRow(Text text) {
			super(new ArrayList<>());
			this.text = text;
		}

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			MinecraftClient client = MinecraftClient.getInstance();
			TextRenderer textRenderer = client.textRenderer;

			RenderUtils.enableBlend();
			DrawUtils.drawTexture(context, SEPARATOR, x - 1, y + (entryHeight / 2) - 3, 0, 0, 32, 7, 32, 7);
			RenderUtils.disableBlend();

			if (hovered) {
				if (!(client.currentScreen instanceof IRequestableTooltipScreen tooltipScreen)) {
					return;
				}

				tooltipScreen.myTotemDoll$requestTooltip(((c, mx, my, d) -> {
					c.drawOrderedTooltip(textRenderer, textRenderer.wrapLines(this.text, 10000), mx, my);
				}));
			}
		}
	}

}
