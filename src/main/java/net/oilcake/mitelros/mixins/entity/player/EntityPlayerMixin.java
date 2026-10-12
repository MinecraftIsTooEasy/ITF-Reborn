package net.oilcake.mitelros.mixins.entity.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.oilcake.mitelros.extension.PlayerExtension;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import net.oilcake.mitelros.mixin.interfaces.ITFFoodStats;
import net.oilcake.mitelros.potion.PotionExtend;
import net.oilcake.mitelros.status.EnchantmentManager;
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

import java.util.List;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements ICommandSender, ITFEntityPlayer {
    public EntityPlayerMixin(World par1World) {
        super(par1World);
    }

    @Shadow
    public InventoryPlayer inventory;

    @Shadow
    public PlayerCapabilities capabilities;

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/Entity;attackEntityFrom(Lnet/minecraft/Damage;)Lnet/minecraft/EntityDamageResult;"))
    private void thresher(Entity target, CallbackInfo ci) {
        if (onServer() && target instanceof EntityLivingBase entity_living_base) {
            this.itf$playerExtension.getEnchantmentManager().thresh(entity_living_base);
        }
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/Entity;onMeleeAttacked(Lnet/minecraft/EntityLivingBase;Lnet/minecraft/EntityDamageResult;)V"))
    private void explosion(Entity target, CallbackInfo ci, @Local float damage) {
        this.itf$playerExtension.getEnchantmentManager().destroy(target);
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;heal(FLnet/minecraft/EnumEntityFX;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void sweep(Entity target, CallbackInfo ci, boolean critical, float damage, int knockback, boolean was_set_on_fire, int fire_aspect, EntityDamageResult result, boolean target_was_harmed, int stunning) {
        this.itf$playerExtension.getEnchantmentManager().sweep(target, damage);
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

    @Unique
    private PlayerExtension itf$playerExtension = new PlayerExtension((EntityPlayer) (Object) this);

    @Override
    public PlayerExtension itf$getPlayerExtension() {
        return this.itf$playerExtension;
    }

    @Inject(method = "onLivingUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;onLivingUpdate()V", shift = At.Shift.AFTER))
    private void injectTick(CallbackInfo ci) {
        this.itf$playerExtension.tick();
    }

    @Inject(method = "addExperience(IZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/FoodStats;setNutrition(IZ)V"))
    private void updateWater(int amount, boolean suppress_healing, boolean suppress_sound, CallbackInfo ci) {
        this.itf$AddWater(0);
    }

    @ModifyVariable(method = "addExperience(IZZ)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int mending(int amount) {
        return this.itf$playerExtension.getEnchantmentManager().onAddingEXP(amount);
    }

    @Inject(method = "readEntityFromNBT", at = @At("HEAD"))
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        this.itf$playerExtension.readNBT(par1NBTTagCompound);
    }

    @Inject(method = "writeEntityToNBT", at = @At("HEAD"))
    private void writeMine(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        this.itf$playerExtension.saveNBT(par1NBTTagCompound);
    }

    @Inject(method = "checkForArmorAchievements", at = @At("HEAD"))
    private void itfArmorAchievements(CallbackInfo ci) {// TODO [Optional] add itf metal to wearAllPlateArmor check
        this.itf$playerExtension.getMiscManager().wolfFurCheck();
        this.itf$playerExtension.getMiscManager().iceChunkCheck();
    }

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void nickelCheck(Damage damage, CallbackInfoReturnable<EntityDamageResult> cir) {
        float nickel_coverage = MathHelper.clamp_float(this.itf$playerExtension.getMiscManager().getNickelArmorCoverage(), 0.0F, 1.0F);
        if (damage.getResponsibleEntity() instanceof EntityGelatinousCube) {
            if (nickel_coverage >= 0.999F)
                cir.setReturnValue(null);
            damage.scaleAmount(1.0F - nickel_coverage);
        }
    }

    @Unique
    public List<Curse> itf$GetActiveCurses() {
        return this.itf$playerExtension.getCurseManager().getActiveCurses();
    }

    @Unique
    public void itf$SetActiveCurses(List<Curse> curses) {
        this.itf$playerExtension.getCurseManager().setActiveCurses(curses);
    }

    @Override
    public void itf$ClearCurses() {
        this.itf$playerExtension.getCurseManager().clearCurses();
    }

    @Override
    public void itf$LearnCurseEffect(Curse curse) {
        this.itf$playerExtension.getCurseManager().learnCurseEffect(curse);
    }

    @Inject(method = "learnCurseEffect", at = @At("HEAD"), cancellable = true)
    private void onLearnCurseEffect(CallbackInfo ci) {
        if (this.itf$playerExtension.getCurseManager().onLearnCurseEffect()) {
            ci.cancel();
        }
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean hasCurse(Curse curse, boolean learn_effect_if_so) {
        return this.itf$playerExtension.getCurseManager().hasCurse(curse, learn_effect_if_so);
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
