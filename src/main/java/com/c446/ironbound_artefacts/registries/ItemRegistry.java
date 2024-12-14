package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.items.ForsakenDreams;
import com.c446.ironbound_artefacts.items.armor.arcane_weave.ArcaneWeaveItem;
import com.c446.ironbound_artefacts.items.impl.*;
import com.c446.ironbound_artefacts.items.impl.lore_items.*;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.SpellSlotUpgradeItem;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffTier;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

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
    public static final DeferredHolder<Item, StaffOfMagi> STAFF_OF_MAGI;
    public static final DeferredHolder<Item, HermitEye> HERMIT_EYE;
    public static final DeferredHolder<Item, LoversStopwatch> STOPWATCH;
    //public static final DeferredHolder<Item, ForsakenDreams> DREAMS;
    public static final DeferredHolder<Item, CurioBaseItem> MAGIC_DEFENSE_RING = ITEMS.register("magic_protection_ring", () -> new CurioBaseItem(new Item.Properties()).withAttributes(Curios.RING_SLOT, new AttributeContainer(AttributeRegistry.SPELL_RESIST, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
    public static final DeferredHolder<Item, CurioBaseItem> PROTECTION_RING = ITEMS.register("protection_ring", () -> new CurioBaseItem(new Item.Properties()).withAttributes(Curios.RING_SLOT, new AttributeContainer(Attributes.ARMOR, 5, AttributeModifier.Operation.ADD_VALUE)));
    public static final DeferredHolder<Item, EvasionCloak> ARCANE_PROTECTION_CLOAK = ITEMS.register("cloak_of_evasion", () -> new EvasionCloak(new Item.Properties()));
    public static final DeferredHolder<Item, ElvenChains> ELVEN_CHAINS = ITEMS.register("elven_chains", () -> new ElvenChains(new Item.Properties()));
    public static final DeferredHolder<Item, RingOfThreeWishes> THREE_WISHES = ITEMS.register("three_wishes", () -> new RingOfThreeWishes(new Item.Properties()));

    public static final DeferredHolder<Item, CurioBaseItem> ARCHMAGE_SPELLBOOK;
    public static final DeferredHolder<Item, AmuletOfHolding> AMULET_OF_HOLDING;
    //public static final DeferredHolder<Item, AmuletOfMana> AMULET_OF_MANA;
    public static final DeferredHolder<Item, Item> GREATER_SPELL_SLOT_UPGRADE;
    //public static final DeferredHolder<Item, DeckOfAllThings> DECK_OF_ALL_THINGS;

    public static final DeferredHolder<Item, ArcaneWeaveItem> WEAVE_HELMET = ITEMS.register("arcane_weave_helmet", () -> new ArcaneWeaveItem(Type.HELMET, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(ArmorItem.Type.HELMET.getDurability(55))));
    public static final DeferredHolder<Item, ArcaneWeaveItem> WEAVE_CHEST_PLATE = ITEMS.register("arcane_weave_chestplate", () -> new ArcaneWeaveItem(Type.CHESTPLATE,

            new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(ArmorItem.Type.HELMET.getDurability(55))));
    public static final DeferredHolder<Item, ArcaneWeaveItem> WEAVE_LEGGINGS = ITEMS.register("arcane_weave_leggings", () -> new ArcaneWeaveItem(Type.LEGGINGS, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(ArmorItem.Type.HELMET.getDurability(55))));

    public static final DeferredHolder<Item, ArcaneWeaveItem> WEAVE_BOOTS = ITEMS.register("arcane_weave_boots", () -> new ArcaneWeaveItem(Type.BOOTS, ItemPropertiesHelper.equipment().rarity(Rarity.RARE).stacksTo(1).durability(ArmorItem.Type.HELMET.getDurability(55))));

    public static final StaffTier TIER_STAFF_OF_MAGI = new StaffTier(3, -0.5F, new AttributeContainer(AttributeRegistry.MAX_MANA, 250, AttributeModifier.Operation.ADD_VALUE), new AttributeContainer(AttributeRegistry.SPELL_RESIST, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final StaffTier TIER_STAFF_OF_POWER = new StaffTier(5F, -0.5F, new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer(AttributeRegistry.MANA_REGEN, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    static {
        /*ACE*/
        DEVILS_FINGER = ITEMS.register("devils_finger", () -> new DevilsFinger(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        /*AMON*/
        MAGICIANS_MONOCLE = ITEMS.register("magicians_monocle", () -> new MagicianMonocle(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        /*AMA*/
        HERMIT_EYE = ITEMS.register("hermit_eye", () -> new HermitEye(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        /*CATAS*/
        JUDGEMENT_SCALE = ITEMS.register("judgement_scale", () -> new JudgementScale(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
        /*KILLAGER*/
        DEATH_AMULET = ITEMS.register("death_amulet", () -> new DeathAmulet(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        /*ENDER*/
        LICH_CROWN = ITEMS.register("emperor_crown", () -> new LichCrown(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        /*TAR*/
        LICH_HAND = ITEMS.register("strength_hand", () -> new LichHand(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        STOPWATCH = ITEMS.register("stopwatch", () -> new LoversStopwatch(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        //DREAMS = ITEMS.register("forsaken_dreams", () -> new ForsakenDreams(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

        GREATER_SPELL_SLOT_UPGRADE = ITEMS.register("greater_spell_slot_upgrade", () -> new SpellSlotUpgradeItem(15));

        AMULET_OF_HOLDING = ITEMS.register("amulet_of_spell_storing", () -> new AmuletOfHolding(new Item.Properties().component(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(4, true, false, false))));
        //AMULET_OF_MANA = ITEMS.register("amulet_of_mana_storing", () -> new AmuletOfMana(new Item.Properties().durability(1500)));

        STAFF_OF_POWER = ITEMS.register("staff_of_power", () -> new StaffOfPower(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(TIER_STAFF_OF_POWER))));

        STAFF_OF_MAGI = ITEMS.register("staff_of_magi", () -> new StaffOfMagi(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(TIER_STAFF_OF_MAGI))));

        ARCHMAGE_SPELLBOOK = ITEMS.register("archmage_spellbook", () -> new ArchMageSpellBook(1).withSpellbookAttributes(new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)));

        //DECK_OF_ALL_THINGS = ITEMS.register("deck_of_many_things", () -> new DeckOfAllThings(new Item.Properties().rarity(Rarity.RARE)));
    }


}
