package com.c446.ironbound_artefacts.items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class JudgementScale extends UserDependantCurios{

    public JudgementScale(Properties p) {
        super(p);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.CATMOTH) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        int multiplier = 1;
        if (canEntityUseItem(slotContext.entity())) {
            multiplier = 2;
        }
        var modifiers = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(IronboundArtefact.prefix("judgement_scale"), 0.25 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(IronboundArtefact.prefix("judgement_scale"), -0.25 * 1/multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return modifiers;
    }
}
