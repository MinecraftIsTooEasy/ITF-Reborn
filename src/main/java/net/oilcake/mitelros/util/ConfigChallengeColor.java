package net.oilcake.mitelros.util;

import net.minecraft.EnumChatFormatting;

public class ConfigChallengeColor {
	public static EnumChatFormatting getColor(int level) {
		return switch (level) {
			case -3 -> EnumChatFormatting.GREEN;
			case -2 -> EnumChatFormatting.AQUA;
			case -1 -> EnumChatFormatting.DARK_AQUA;
			case 1 -> EnumChatFormatting.BLUE;
			case 2 -> EnumChatFormatting.YELLOW;
			case 3 -> EnumChatFormatting.GOLD;
			case 4 -> EnumChatFormatting.RED;
			case 5 -> EnumChatFormatting.DARK_RED;
			default -> EnumChatFormatting.WHITE;
		};
	}
}
