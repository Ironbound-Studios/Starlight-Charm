package com.c446.ironbound_artefacts.items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Objects;

public class MagicianMonocle extends UserDependantCurios {
    public MagicianMonocle(Properties p) {
        super(p);
    }
    public MagicianMonocle(Properties p, boolean showEnch) {
        super(p, showEnch);
    }
    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.AMON) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        int multiplier = 1;
        if (canEntityUseItem(slotContext.entity())) {
            multiplier = 2;
        }
        var attributeModifier = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        attributeModifier.put(AttributeRegistry.SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("magicians_monocle"), 0.2 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return attributeModifier;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (canEntityUseItem(slotContext.entity()) && slotContext.entity().tickCount % 10 == 0){
            var copy = stack.copy();
            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.MAGIC_MISSILE_SPELL.get().getSpellId(), 3));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a->a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        }
    }
}
