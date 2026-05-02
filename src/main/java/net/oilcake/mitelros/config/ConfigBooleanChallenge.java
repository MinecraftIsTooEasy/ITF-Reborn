package net.oilcake.mitelros.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.minecraft.EnumChatFormatting;
import net.oilcake.mitelros.util.ConfigChallengeColor;

public class ConfigBooleanChallenge extends ConfigBoolean {
    private final int level;

    public ConfigBooleanChallenge(String name, String comment, int level) {
        super(name, comment);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String getDisplayText() {
        String level = ConfigChallengeColor.getColor(this.level) + String.format("(LVL%d)", this.level);
        String info = EnumChatFormatting.WHITE + this.getName() + ": " + (this.getBooleanValue() ? "开" : "关");
        return level + info;
    }
}
