package net.oilcake.mitelros.config;

import fi.dy.masa.malilib.config.options.ConfigInteger;
import net.oilcake.mitelros.util.ConfigChallengeColor;

public class ConfigIntegerChallenge extends ConfigInteger {
	
	public ConfigIntegerChallenge(String name, int defaultValue) {
		super(name, defaultValue);
	}
	
	public ConfigIntegerChallenge(String name, int defaultValue, String comment) {
		super(name, defaultValue, comment);
	}
	
	public ConfigIntegerChallenge(String name, int defaultValue, int minValue, int maxValue) {
		super(name, defaultValue, minValue, maxValue);
	}
	
	public ConfigIntegerChallenge(String name, int defaultValue, int minValue, int maxValue, String comment) {
		super(name, defaultValue, minValue, maxValue, comment);
	}
	
	public ConfigIntegerChallenge(String name, int defaultValue, int minValue, int maxValue, boolean useSlider, String comment) {
		super(name, defaultValue, minValue, maxValue, useSlider, comment);
	}
	
	@Override
	public String getDisplayText() {
		return ConfigChallengeColor.getColor(getIntegerValue()) + String.format("(LVL%d)", getIntegerValue());
	}
	
	public boolean isEnable() {
		return getIntegerValue() > 0;
	}
}
