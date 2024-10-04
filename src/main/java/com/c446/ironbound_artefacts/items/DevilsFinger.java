package com.c446.ironbound_artefacts.items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.AffinityRing;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Objects;

public class DevilsFinger extends AffinityRing {

    public DevilsFinger(Properties p) {
        super(p);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        int multiplier = 1;
        if (slotContext.entity() instanceof Player player && Objects.equals(player.getUUID().toString(), "34e0c700-66dd-4932-8e0b-0083076609d5")) {
            multiplier = 2;
        }
        Multimap<Holder<Attribute>, AttributeModifier> attributeMap = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        attributeMap.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("devil_ring"), 0.2 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributeMap.put(AttributeRegistry.HOLY_SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("devil_ring"), -0.3 * 1 / multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributeMap.put(AttributeRegistry.HOLY_MAGIC_RESIST, new AttributeModifier(IronboundArtefact.prefix("devil_ring"), -0.3 * 1 / multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return attributeMap;
    }


}
