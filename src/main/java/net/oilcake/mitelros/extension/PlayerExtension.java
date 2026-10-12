package net.oilcake.mitelros.extension;

import net.minecraft.EntityPlayer;
import net.minecraft.NBTTagCompound;
import net.oilcake.mitelros.status.*;

public class PlayerExtension {
    private final EntityPlayer player;

    private final MiscManager miscManager;
    private final FeastManager feastManager;
    private final DrunkManager drunkManager;
    private final HuntManager huntManager;
    private final DiarrheaManager diarrheaManager;
    private final WaterManager waterManager;
    private final EnchantmentManager enchantmentManager;
    private final CurseManager curseManager;

    public PlayerExtension(EntityPlayer player) {
        this.player = player;
        this.miscManager = new MiscManager(player);
        this.feastManager = new FeastManager(player);
        this.drunkManager = new DrunkManager(player);
        this.huntManager = new HuntManager(player);
        this.diarrheaManager = new DiarrheaManager(player);
        this.waterManager = new WaterManager(player);
        this.enchantmentManager = new EnchantmentManager(player);
        this.curseManager = new CurseManager(player);
    }

    public void tick() {
        if (!this.player.worldObj.isRemote) {
            this.miscManager.update();
            this.diarrheaManager.update();
            this.huntManager.update();
            this.waterManager.update();
            this.drunkManager.update();
        }
        this.feastManager.achievementCheck();
        this.enchantmentManager.update();
    }

    public void saveNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("diarrheaCounter", this.diarrheaManager.getDiarrheaCounter());
        nbtTagCompound.setBoolean("UsedTotemOfHunt", this.huntManager.hunt_cap);
        nbtTagCompound.setInteger("TotemDyingCounter", this.huntManager.hunt_counter);
        nbtTagCompound.setInteger("DrunkDuration", this.drunkManager.getDrunk_duration());
        nbtTagCompound.setByte("KnowledgeTotemUsed", this.miscManager.getKnowledgeTotemCounter());
    }

    public void readNBT(NBTTagCompound nbtTagCompound) {
        this.diarrheaManager.setDiarrheaCounter(nbtTagCompound.getInteger("diarrheaCounter"));
        this.huntManager.hunt_cap = nbtTagCompound.getBoolean("UsedTotemOfHunt");
        this.huntManager.hunt_counter = nbtTagCompound.getInteger("TotemDyingCounter");
        this.drunkManager.setDrunk_duration(nbtTagCompound.getInteger("DrunkDuration"));
        this.miscManager.setKnowledgeTotemCounter(nbtTagCompound.getByte("KnowledgeTotemUsed"));
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public MiscManager getMiscManager() {
        return miscManager;
    }

    public FeastManager getFeastManager() {
        return feastManager;
    }

    public DrunkManager getDrunkManager() {
        return drunkManager;
    }

    public HuntManager getHuntManager() {
        return huntManager;
    }

    public DiarrheaManager getDiarrheaManager() {
        return diarrheaManager;
    }

    public WaterManager getWaterManager() {
        return waterManager;
    }

    public EnchantmentManager getEnchantmentManager() {
        return enchantmentManager;
    }

    public CurseManager getCurseManager() {
        return curseManager;
    }
}
