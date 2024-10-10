package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.items.impl.*;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.item.curios.AffinityRing;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.item.weapons.StaffTier;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Rarity;
import org.w3c.dom.Attr;

import java.util.ArrayList;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);
    public static final DeferredHolder<Item, DevilsFinger> DEVILS_FINGER;
    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
    public static final DeferredHolder<Item, JudgementScale> JUDGEMENT_SCALE;
    public static final DeferredHolder<Item, DeathAmulet> DEATH_AMULET;
    public static final DeferredHolder<Item, LichCrown> LICH_CROWN;
    public static final DeferredHolder<Item, LichHand> LICH_HAND;
    public static final DeferredHolder<Item, StaffOfPower> STAFF_OF_POWER;
//    public static final DeferredHolder<Item, DeathAmulet> HERMIT_EYE;


    public static final DeferredHolder<Item, CurioBaseItem> ARCHMAGE_SPELLBOOK;
    //    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
//    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
//    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;

    public static final StaffTier TIER_STAFF_OF_POWER = new StaffTier(10,2,
            new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.4, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE )
    );

    static {

        /*ACE*/
        DEVILS_FINGER = ITEMS.register("devils_finger", () -> new DevilsFinger(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        /*AMON*/
        MAGICIANS_MONOCLE = ITEMS.register("magicians_monocle", () -> new MagicianMonocle(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        /*CATAS*/
        JUDGEMENT_SCALE = ITEMS.register("judgement_scale", () -> new JudgementScale(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        /*KILLAGER*/
        DEATH_AMULET = ITEMS.register("death_amulet", () -> new DeathAmulet(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        /*ENDER*/
        LICH_CROWN = ITEMS.register("emperor_crown", () -> new LichCrown(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        /*TAR*/
        LICH_HAND = ITEMS.register("strength_hand", () -> new LichHand(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        /*AMA*/
        //HERMIT_EYE = ITEMS.register("hermit_eye", () -> new DeathAmulet(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));


        STAFF_OF_POWER = ITEMS.register("staff_of_power", () -> {
            return new StaffOfPower(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(TIER_STAFF_OF_POWER)));
        });
/*
     public static final DeferredHolder<Item, Item> EVOKER_SPELL_BOOK = ITEMS.register("evoker_spell_book", () -> new UniqueSpellBook(
            new SpellDataRegistryHolder[]{
                    new SpellDataRegistryHolder(SpellRegistry.FANG_STRIKE_SPELL, 6),
                    new SpellDataRegistryHolder(SpellRegistry.FANG_WARD_SPELL, 4),
                    new SpellDataRegistryHolder(SpellRegistry.SUMMON_VEX_SPELL, 4)},
            7).withSpellbookAttributes(new AttributeContainer(AttributeRegistry.EVOCATION_SPELL_POWER, .10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE))
    );
*
* */
        ARCHMAGE_SPELLBOOK = ITEMS.register("archmage_spellbook", () -> new UniqueSpellBook(
                new SpellDataRegistryHolder[]{
                        new SpellDataRegistryHolder(SpellRegistry.FIREBALL_SPELL, 5),
                        new SpellDataRegistryHolder(SpellRegistry.LIGHTNING_BOLT_SPELL, 10)},
                13).withSpellbookAttributes(new AttributeContainer(AttributeRegistry.MAX_MANA, 300, AttributeModifier.Operation.ADD_VALUE), new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
        );
//        ARCHMAGE_SPELLBOOK = ITEMS.register("archmage_spellbook", () -> new ArchMageSpellBook(15, new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
    }


}
