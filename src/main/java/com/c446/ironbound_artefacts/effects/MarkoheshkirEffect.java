package com.c446.ironbound_artefacts.effects;

import com.c446.ironbound_artefacts.registries.CustomSpellRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class MarkoheshkirEffect extends IronboundMobEffect  {
    public MarkoheshkirEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public MarkoheshkirEffect addAttributeModifier(Holder<Attribute> pAttribute, ResourceLocation pId, double pAmount, AttributeModifier.Operation pOperation) {
        return (MarkoheshkirEffect) super.addAttributeModifier(pAttribute, pId, pAmount, pOperation);
    }
}