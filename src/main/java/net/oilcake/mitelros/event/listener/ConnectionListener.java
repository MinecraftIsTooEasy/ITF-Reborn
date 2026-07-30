package net.oilcake.mitelros.event.listener;

import moddedmite.rustedironcore.api.event.listener.IConnectionListener;
import moddedmite.rustedironcore.api.util.StringUtil;
import net.minecraft.Minecraft;
import net.minecraft.NetClientHandler;
import net.minecraft.Packet1Login;
import net.oilcake.mitelros.config.ITFConfig;

public class ConnectionListener implements IConnectionListener {

    @Override
    public void onClientLoggedIn(NetClientHandler clientHandler, Packet1Login login) {
        Minecraft client = Minecraft.getMinecraft();

        if (ITFConfig.Statement.getBooleanValue() && StringUtil.getCurrentLanguage().equals("zh_CN")) {
            client.thePlayer.addChatMessage("[ITF-RB] 本模组的官方英文名为ITF Reborn, 官方中文名为黄昏重生, 如果被我发现你仅用\"重生\"两字指代, 没你好果子吃! 本声明可通过配置文件关闭.");
        }
    }
}
