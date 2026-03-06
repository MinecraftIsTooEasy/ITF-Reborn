package net.oilcake.mitelros.enchantment;

import net.minecraft.CreativeTabs;
import net.minecraft.Enchantment;
import net.minecraft.EnumRarity;
import net.minecraft.Item;

public class EnchantmentMelting extends Enchantment {
    protected EnchantmentMelting(int id, EnumRarity rarity, int difficulty) {
        super(id, rarity, difficulty);
    }

    @Override
    public boolean canApplyTogether(Enchantment par1Enchantment) {
        return (super.canApplyTogether(par1Enchantment) && par1Enchantment.effectId != Enchantments.enchantmentAbsorb.effectId);
    }

    @Override
    public String getNameSuffix() {
        return "melting";
    }

    @Override
    public boolean canEnchantItem(Item item) {
        return (item instanceof net.minecraft.ItemPickaxe && !(item instanceof net.minecraft.ItemWarHammer));
    }

    @Override
    public boolean isOnCreativeTab(CreativeTabs creativeModeTab) {
        return (creativeModeTab == CreativeTabs.tabTools);
    }
}
