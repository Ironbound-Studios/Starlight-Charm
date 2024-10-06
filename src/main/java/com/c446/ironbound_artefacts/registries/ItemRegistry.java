package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.items.impl.*;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.curios.AffinityRing;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Rarity;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);

    public static final DeferredHolder<Item, DevilsFinger> DEVILS_FINGER;
    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
    public static final DeferredHolder<Item, JudgementScale> JUDGEMENT_SCALE;
    public static final DeferredHolder<Item, DeathAmulet> DEATH_AMULET;


    public static final DeferredHolder<Item, CurioBaseItem> ARCHMAGE_SPELLBOOK;
    //    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
//    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
//    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;

    static {

        DEVILS_FINGER = ITEMS.register("devils_finger", () -> new DevilsFinger(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        MAGICIANS_MONOCLE = ITEMS.register("magicians_monocle", () -> new MagicianMonocle(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        JUDGEMENT_SCALE = ITEMS.register("judgement_scale", () -> new JudgementScale(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        DEATH_AMULET = ITEMS.register("death_amulet", () -> new DeathAmulet(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

        ARCHMAGE_SPELLBOOK = ITEMS.register("archmage_spellbook", () -> new ArchMageSpellBook(8, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    }


}
