package net.lopymine.mtd.utils.tooltip;

import net.minecraft.client.gui.DrawContext;

public interface TooltipRequest {

	void render(DrawContext context, int mouseX, int mouseY, float delta);

}
