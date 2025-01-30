package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.items.UserDependantCurios;
//imports
import com.google.common.collect.Multimap;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;



public class StarLightCharm extends UserDependantCurios {
    public StarLightCharm(Properties p) {
        super(p);
        }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
     var map=
        super.getAttributeModifiers(slotContext, id, stack);
        map.put(AttributeRegistry.BLOOD_SPELL_POWER, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        map.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(id, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        map.put(AASpells.Attributes.WIND_SPELL_POWER, new AttributeModifier(id, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return map;
    }
}



