package net.lopymine.mtd.gui.widget.info;

import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.gui.tooltip.info.InfoTooltipData;

public class TipsWidget extends InfoWidget {

	public static final Identifier TEXTURE = MyTotemDoll.id("textures/gui/info/tips.png");

	public TipsWidget(int x, int y) {
		super(x, y, 9, 9, new InfoTooltipData("tags.tips", -1), TEXTURE);
	}
}
