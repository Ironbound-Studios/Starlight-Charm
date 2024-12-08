package com.c446.ironbound_artefacts.items.impl.titles;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class Paragon extends GenericTitleItem{
    public Paragon(Properties properties, Holder<Attribute> attributeHolder) {
        super(properties);
        this.attr = attributeHolder;
    }

    public Holder<Attribute> attr;

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributes = super.getAttributeModifiers(slotContext, id, stack);
         attributes.put(attr, new AttributeModifier(IronboundArtefact.prefix("paragon_school_boost"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return attributes;
    }
}
