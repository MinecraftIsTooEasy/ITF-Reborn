package net.oilcake.mitelros.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.cbcore.LangUtil;
import net.minecraft.BlockFluid;
import net.minecraft.BlockFurnace;
import net.minecraft.Entity;
import net.minecraft.EnumChatFormatting;
import net.minecraft.I18n;
import net.minecraft.ItemStack;
import net.minecraft.NBTTagCompound;
import net.minecraft.RaycastCollision;
import net.minecraft.ServerPlayer;
import net.minecraft.TileEntity;
import net.minecraft.TileEntityFurnace;
import net.minecraft.World;
import net.oilcake.mitelros.entity.boss.EntityLich;
import net.oilcake.mitelros.entity.mob.EntityLichShadow;
import net.oilcake.mitelros.mixin.interfaces.ITFFurnace;
import net.oilcake.mitelros.util.WaterHelper;

import java.util.List;

public final class WailaCompat {

    public static void register(IWailaRegistrar registrar) {
        registrar.registerHeadProvider(new PureWaterDataProvider(), BlockFluid.class);

        IWailaDataProvider furnaceProvider = new FurnaceDataProvider();
        registrar.registerBodyProvider(furnaceProvider, BlockFurnace.class);
        registrar.registerNBTProvider(furnaceProvider, BlockFurnace.class);

        IWailaEntityProvider evasionProvider = new EvasionDataProvider();
        registrar.registerBodyProvider(evasionProvider, EntityLich.class);
        registrar.registerNBTProvider(evasionProvider, EntityLich.class);
        registrar.registerBodyProvider(evasionProvider, EntityLichShadow.class);
        registrar.registerNBTProvider(evasionProvider, EntityLichShadow.class);
    }

    private static final class PureWaterDataProvider implements IWailaDataProvider {

        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return null;
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            if (!WaterHelper.isWater(accessor.getBlock())) return currentTip;

            RaycastCollision hit = accessor.getPosition();
            if (WaterHelper.isPureWater(accessor.getWorld(), hit.block_hit_x, hit.block_hit_z)) {
                setTitle(currentTip, EnumChatFormatting.AQUA + I18n.getString("compat.waila.pure_water"));
            }
            return currentTip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currentTip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currentTip;
        }

        @Override
        public NBTTagCompound getNBTData(ServerPlayer player, TileEntity tileEntity, NBTTagCompound tag, World world, int x, int y, int z) {
            return tag;
        }

        private static void setTitle(List<String> currentTip, String title) {
            if (currentTip.isEmpty()) {
                currentTip.add(title);
            } else {
                currentTip.set(0, title);
            }
        }
    }

    private static final class FurnaceDataProvider implements IWailaDataProvider {

        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return null;
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currentTip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            NBTTagCompound tag = accessor.getNBTData();
            if (!tag.hasKey("ITFFurnaceBurning")) return currentTip;
            if (tag.getBoolean("ITFFurnaceBurning")
                    || !tag.getBoolean("ITFFurnaceHasFuel")
                    || tag.getBoolean("ITFFurnaceSelfIgniting")) return currentTip;

            boolean hasEmbers = tag.getBoolean("ITFFurnaceActivated");
            String state = hasEmbers
                    ? "compat.waila.furnace.state.embers"
                    : "compat.waila.furnace.state.ignition_required";
            EnumChatFormatting color = hasEmbers ? EnumChatFormatting.YELLOW : EnumChatFormatting.RED;
            currentTip.add(color + I18n.getString(state));
            return currentTip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currentTip;
        }

        @Override
        public NBTTagCompound getNBTData(ServerPlayer player, TileEntity tileEntity, NBTTagCompound tag, World world, int x, int y, int z) {
            TileEntityFurnace furnace = (TileEntityFurnace) tileEntity;
            ITFFurnace itfFurnace = (ITFFurnace) furnace;
            tag.setBoolean("ITFFurnaceBurning", furnace.isBurning());
            tag.setBoolean("ITFFurnaceHasFuel", furnace.getStackInSlot(TileEntityFurnace.FUEL) != null);
            tag.setBoolean("ITFFurnaceActivated", itfFurnace.itf$IsActive());
            tag.setBoolean("ITFFurnaceSelfIgniting", itfFurnace.itf$canBurnByItself());
            return tag;
        }
    }

    private static final class EvasionDataProvider implements IWailaEntityProvider {

        private static final String EVASIONS_KEY = "WailaNumEvasions";

        @Override
        public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
            return null;
        }

        @Override
        public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
            return currenttip;
        }

        @Override
        public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
            if (!config.getConfig("option.general.showphaseevasions", true)) return currenttip;

            NBTTagCompound tag = accessor.getNBTData();
            if (tag != null && tag.hasKey(EVASIONS_KEY)) {
                int evasions = tag.getInteger(EVASIONS_KEY);
                if (evasions >= 0)
                    currenttip.add(EnumChatFormatting.GRAY + LangUtil.translateG("hud.msg.phase_evasions", evasions));
            }
            return currenttip;
        }

        @Override
        public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
            return currenttip;
        }

        @Override
        public NBTTagCompound getNBTData(ServerPlayer player, Entity entity, NBTTagCompound tag, World world) {
            int evasions = entity instanceof EntityLich lich
                    ? lich.getNumEvasions()
                    : entity instanceof EntityLichShadow shadow ? shadow.getNumEvasions() : -1;
            if (evasions >= 0) tag.setInteger(EVASIONS_KEY, evasions);
            return tag;
        }
    }
}
