package net.lopymine.mtd.modmenu.yacl.simple;

import lombok.Getter;

@Getter
public enum SimpleContent {
	NONE("none"),
	IMAGE("png"),
	WEBP("webp"),
	GIF("gif");

	private final String fileExtension;

	SimpleContent(String fileExtension) {
		this.fileExtension = fileExtension;
	}
}
