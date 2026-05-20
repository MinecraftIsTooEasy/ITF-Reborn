package net.oilcake.mitelros.mixins.entity.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import net.oilcake.mitelros.mixin.interfaces.ITFFoodStats;
import net.oilcake.mitelros.network.ITFNetwork;
import net.oilcake.mitelros.network.packets.C2SLearnCurseEffect;
import net.oilcake.mitelros.network.packets.S2CUpdateCurses;
import net.oilcake.mitelros.potion.PotionExtend;
import net.oilcake.mitelros.status.*;
import net.oilcake.mitelros.util.CurseExtend;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements ICommandSender, ITFEntityPlayer {
    public EntityPlayerMixin(World par1World) {
        super(par1World);
    }

    @Shadow
    public InventoryPlayer inventory;

    @Unique
    public DiarrheaManager diarrheaManager = new DiarrheaManager(ReflectHelper.dyCast(this));

    @Shadow
    public PlayerCapabilities capabilities;

    @Unique
    private final HuntManager huntManager = new HuntManager(ReflectHelper.dyCast(this));

    @Override
    public HuntManager itf$GetHuntManager() {
        return huntManager;
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/Entity;attackEntityFrom(Lnet/minecraft/Damage;)Lnet/minecraft/EntityDamageResult;"))
    private void thresher(Entity target, CallbackInfo ci) {
        if (onServer() && target instanceof EntityLivingBase entity_living_base) {
            this.enchantmentManager.thresh(entity_living_base);
        }
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/Entity;onMeleeAttacked(Lnet/minecraft/EntityLivingBase;Lnet/minecraft/EntityDamageResult;)V"))
    private void explosion(Entity target, CallbackInfo ci, @Local float damage) {
        this.enchantmentManager.destroy(target);
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;heal(FLnet/minecraft/EnumEntityFX;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void sweep(Entity target, CallbackInfo ci, boolean critical, float damage, int knockback, boolean was_set_on_fire, int fire_aspect, EntityDamageResult result, boolean target_was_harmed, int stunning) {
        this.enchantmentManager.sweep(target, damage);
    }

    @ModifyExpressionValue(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Ljava/lang/Math;random()D"))
    private double stun(double original) {
        return original * 0.5D;
    }

    @ModifyArg(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;addPotionEffect(Lnet/minecraft/PotionEffect;)V"))
    private PotionEffect stun(PotionEffect par1PotionEffect) {
        return new PotionEffect(PotionExtend.stunning.id, par1PotionEffect.getDuration(), par1PotionEffect.getAmplifier());
    }

    @WrapWithCondition(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;triggerAchievement(Lnet/minecraft/StatBase;)V"))
    private boolean strict(EntityPlayer instance, StatBase par1StatBase, @Local float damage) {
        return damage >= 40.0F;
    }

    @Inject(method = "onDeath(Lnet/minecraft/DamageSource;)V", at = @At("TAIL"))
    public void onDeath(DamageSource par1DamageSource, CallbackInfo callbackInfo) {
        if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            EnchantmentManager.vanish(this.inventory);
        }
    }

    @Unique
    public int itf$GetWater() {
        return ((ITFFoodStats) this.foodStats).itf$GetWater();
    }

    @Unique
    public int itf$AddWater(int water) {
        return ((ITFFoodStats) this.foodStats).itf$AddWater(water);
    }

    @Unique
    public void itf$DecreaseWaterServerSide(float hungerWater) {
        if (!this.capabilities.isCreativeMode && !this.capabilities.disableDamage)
            ((ITFFoodStats) this.foodStats).itf$DecreaseWaterServerSide(hungerWater);
    }

    @ModifyReturnValue(method = "hasFoodEnergy", at = @At("RETURN"))
    private boolean needWater(boolean original) {
        return original && this.itf$GetWater() != 0;
    }

    @Shadow
    protected FoodStats foodStats;

    @Shadow
    public abstract ItemStack getHeldItemStack();
    
    @Shadow
    public boolean is_cursed;
    @Shadow
    public int curse_id;
    @Shadow
    public boolean curse_effect_known;
    
    @Shadow
    public abstract void sendPacket(Packet packet);
    
    @Unique
    private MiscManager miscManager = new MiscManager(ReflectHelper.dyCast(this));
    @Unique
    private EnchantmentManager enchantmentManager = new EnchantmentManager(ReflectHelper.dyCast(this));

    public MiscManager itf_GetMiscManager() {
        return miscManager;
    }

    @Unique
    private WaterManager waterManager = new WaterManager(ReflectHelper.dyCast(this));

    @Inject(method = "onLivingUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;onLivingUpdate()V", shift = At.Shift.AFTER))
    private void injectTick(CallbackInfo ci) {
        if (!this.worldObj.isRemote) {
            this.miscManager.update();
            this.diarrheaManager.update();
            this.huntManager.update();
            this.waterManager.update();
            this.drunkManager.update();
        }
        this.feastManager.achievementCheck();
        this.enchantmentManager.update();
    }

    @Inject(method = "addExperience(IZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/FoodStats;setNutrition(IZ)V"))
    private void updateWater(int amount, boolean suppress_healing, boolean suppress_sound, CallbackInfo ci) {
        this.itf$AddWater(0);
    }

    @ModifyVariable(method = "addExperience(IZZ)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int mending(int amount) {
        return this.enchantmentManager.onAddingEXP(amount);
    }

    @Unique
    private FeastManager feastManager = new FeastManager(ReflectHelper.dyCast(this));

    @Override
    public FeastManager itf$GetFeastManager() {
        return feastManager;
    }

    @Unique
    private DrunkManager drunkManager = new DrunkManager(ReflectHelper.dyCast(this));

    @Override
    public DrunkManager itf$GetDrunkManager() {
        return drunkManager;
    }

    @Inject(method = "readEntityFromNBT", at = @At("HEAD"))
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        this.diarrheaManager.setDiarrheaCounter(par1NBTTagCompound.getInteger("diarrheaCounter"));
        this.huntManager.hunt_cap = par1NBTTagCompound.getBoolean("UsedTotemOfHunt");
        this.huntManager.hunt_counter = par1NBTTagCompound.getInteger("TotemDyingCounter");
        this.drunkManager.setDrunk_duration(par1NBTTagCompound.getInteger("DrunkDuration"));
        this.miscManager.setKnowledgeTotemCounter(par1NBTTagCompound.getByte("KnowledgeTotemUsed"));
    }

    @Inject(method = "writeEntityToNBT", at = @At("HEAD"))
    private void writeMine(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        par1NBTTagCompound.setInteger("diarrheaCounter", this.diarrheaManager.getDiarrheaCounter());
        par1NBTTagCompound.setBoolean("UsedTotemOfHunt", this.huntManager.hunt_cap);
        par1NBTTagCompound.setInteger("TotemDyingCounter", this.huntManager.hunt_counter);
        par1NBTTagCompound.setInteger("DrunkDuration", this.drunkManager.getDrunk_duration());
        par1NBTTagCompound.setByte("KnowledgeTotemUsed", this.miscManager.getKnowledgeTotemCounter());
    }

    @Inject(method = "checkForArmorAchievements", at = @At("HEAD"))
    private void itfArmorAchievements(CallbackInfo ci) {// TODO [Optional] add itf metal to wearAllPlateArmor check
        this.miscManager.wolfFurCheck();
        this.miscManager.iceChunkCheck();
    }

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void nickelCheck(Damage damage, CallbackInfoReturnable<EntityDamageResult> cir) {
        float nickel_coverage = MathHelper.clamp_float(this.miscManager.getNickelArmorCoverage(), 0.0F, 1.0F);
        if (damage.getResponsibleEntity() instanceof EntityGelatinousCube) {
            if (nickel_coverage >= 0.999F)
                cir.setReturnValue(null);
            damage.scaleAmount(1.0F - nickel_coverage);
        }
    }

    @Unique
    private final List<Curse> itf$activeCurses = new ArrayList<>();

    @Unique
    public List<Curse> itf$GetActiveCurses() {
        if (this.worldObj != null && this.worldObj.isRemote) {
            if (!this.is_cursed) {
                this.itf$activeCurses.clear();
            } else if (this.curse_id > 0 && this.itf$activeCurses.isEmpty()) {
                Curse curse = this.itf$CreateLocalCurse(this.curse_id, this.curse_effect_known);
                if (curse != null) {
                    this.itf$activeCurses.add(curse);
                }
            }
            if (this.curse_id > 0) {
                boolean curseFound = false;
                for (Curse curse : this.itf$activeCurses) {
                    if (curse.id == this.curse_id) {
                        curse.effect_known = this.curse_effect_known;
                        curseFound = true;
                        break;
                    }
                }
                if (!curseFound) {
                    Curse curse = this.itf$CreateLocalCurse(this.curse_id, this.curse_effect_known);
                    if (curse != null) {
                        this.itf$activeCurses.add(curse);
                    }
                }
            }
        }
        return this.itf$activeCurses;
    }

    @Unique
    public void itf$SetActiveCurses(List<Curse> curses) {
        this.itf$activeCurses.clear();
        HashSet<Integer> added = new HashSet<>();
        int maxCurseCount = this.worldObj != null && this.worldObj.isRemote ? curses.size() : CurseExtend.getMaxCurseCount();
        for (Curse curse : curses) {
            if (this.itf$activeCurses.size() >= maxCurseCount) {
                break;
            }
            if (added.add(curse.id)) {
                this.itf$activeCurses.add(curse);
            }
        }
        if (this.itf$activeCurses.isEmpty()) {
            is_cursed = false;
            curse_id = 0;
            curse_effect_known = false;
        } else {
            is_cursed = true;
            curse_id = this.itf$activeCurses.get(0).id;
            curse_effect_known = this.itf$activeCurses.get(0).effect_known;
        }
    }

    public boolean itf$HasCurse(Curse curse, boolean learnEffectIfSo) {
        for (Curse c : this.itf$GetActiveCurses()) {
            if (c.id == curse.id) {
                if (learnEffectIfSo && !c.effect_known) {
                    itf$LearnCurseEffect(c);
                }
                return true;
            }
        }
        return false;
    }

    @Unique
    public void itf$LearnCurseEffect(Curse curse) {
        Curse activeCurse = this.itf$getActiveCurse(curse.id);
        if (activeCurse != null) {
            curse = activeCurse;
        }
        if (!curse.effect_known) {
            curse.effect_known = true;
            curse.effect_has_already_been_learned = true;
            if (this.worldObj.isRemote) {
                ITFNetwork.sendToServer(new C2SLearnCurseEffect(curse.id));
            } else if ((Object) this instanceof ServerPlayer player) {
                ITFNetwork.sendToClient(player, new S2CUpdateCurses(this.itf$GetActiveCurses()));
            }
        }
    }

    @Inject(method = "learnCurseEffect", at = @At("HEAD"), cancellable = true)
    private void onLearnCurseEffect(CallbackInfo ci) {
        List<Curse> activeCurses = this.itf$GetActiveCurses();
        if (activeCurses.isEmpty()) {
            return;
        }
        for (Curse c : activeCurses) {
            if (!c.effect_known) {
                itf$LearnCurseEffect(c);
            }
        }
        ci.cancel();
    }

    @Unique
    public void removeCurse(Curse curse) {
        List<Curse> remaining = new ArrayList<>(this.itf$activeCurses);
        remaining.removeIf(c -> c.id == curse.id);
        this.itf$SetActiveCurses(remaining);
    }

    @Unique
    public void itf$ClearCurses() {
        this.itf$SetActiveCurses(List.of());
    }

    @Unique
    private Curse itf$getActiveCurse(int curseId) {
        for (Curse activeCurse : this.itf$GetActiveCurses()) {
            if (activeCurse.id == curseId) {
                return activeCurse;
            }
        }
        return null;
    }

    @Unique
    private Curse itf$CreateLocalCurse(int curseId, boolean effectKnown) {
        if (curseId <= 0 || curseId >= Curse.cursesList.length || Curse.cursesList[curseId] == null) {
            return null;
        }
        Curse curse = new Curse(
                this.getEntityName(),
                this.getUniqueID(),
                Curse.cursesList[curseId],
                0L,
                true,
                effectKnown
        );
        curse.effect_has_already_been_learned = effectKnown;
        return curse;
    }
    
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean hasCurse(Curse curse, boolean learn_effect_if_so) {
        return this.itf$HasCurse(curse, learn_effect_if_so);
    }
    
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onCurseRealized(int curse_id) {
        if (curse_id == Curse.cannot_wear_armor.id && !this.worldObj.isRemote && this.inventory.dropAllArmor()) {
            this.itf$LearnCurseEffect(Curse.cursesList[curse_id]);
        }
    }
}
