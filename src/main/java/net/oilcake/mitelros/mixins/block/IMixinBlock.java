package net.oilcake.mitelros.mixins.block;

import net.minecraft.Block;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface IMixinBlock {
    @Invoker("dropXpOnBlockBreak")
    void invokeDropXpOnBlockBreak(World par1World, int par2, int par3, int par4, int par5);
}
