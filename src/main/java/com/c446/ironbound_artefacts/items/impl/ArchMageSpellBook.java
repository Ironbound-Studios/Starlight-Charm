package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class ArchMageSpellBook extends SpellBook {
    public ArchMageSpellBook() {
        this(1);
    }

    public ArchMageSpellBook(int maxSpellSlots) {
        this(maxSpellSlots, ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public ArchMageSpellBook(int maxSpellSlots, Item.Properties pProperties) {
        super(maxSpellSlots, pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, TooltipContext context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, context, lines, flag);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {


        //component(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.LIGHTNING_BOLT_SPELL.get().getSpellId(), 3))
        super.curioTick(slotContext, stack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributes = super.getAttributeModifiers(slotContext, id, stack);
        attributes.put(AttributeRegistry.CAST_TIME_REDUCTION, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.MANA_REGEN, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.MAX_MANA, new AttributeModifier(id, 300, AttributeModifier.Operation.ADD_VALUE));
        return attributes;
    }


}
