package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.items.BaseCurios;
import com.c446.ironbound_artefacts.items.DevilsFinger;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.spells.eldritch.EldritchBlastSpell;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);

    public static final DeferredHolder<Item, DevilsFinger> DEVILS_FINGER;

    static {

        AffinityData DEVILS_FINGER_DATA = new AffinityData(SpellRegistry.ELDRITCH_BLAST_SPELL.get().getSpellId(), 3);
        DEVILS_FINGER = ITEMS.register("devils_finger", ()->new DevilsFinger(new Item.Properties().rarity(Rarity.EPIC).component(ComponentRegistry.AFFINITY_COMPONENT, DEVILS_FINGER_DATA)));

    }

}
