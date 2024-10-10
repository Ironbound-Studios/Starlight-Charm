package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Arrays;
import java.util.List;

public class ArchMageSpellBook extends UniqueSpellBook {


    public ArchMageSpellBook(SpellDataRegistryHolder[] spellDataRegistryHolders) {
        super(spellDataRegistryHolders);
    }

    public ArchMageSpellBook(SpellDataRegistryHolder[] spellDataRegistryHolders, int additionalSlots) {
        super(spellDataRegistryHolders, additionalSlots);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, TooltipContext context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {

        super.appendHoverText(itemStack, context, lines, flag);
    }

    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (IronboundArtefact.ContributorUUIDS.CONTRIBUTOR_LIST.contains(player.getStringUUID()) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        //if (slotContext.entity().getStringUUID())
        if (canEntityUseItem(slotContext.entity())) {
            var copy = stack.copy();
            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.WOLOLO_SPELL.get().getSpellId(), 1));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        } else {
            var copy = stack.copy();
            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.none().getSpellId(), 0));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        }
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
