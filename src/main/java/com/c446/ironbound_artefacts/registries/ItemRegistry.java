package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.items.DevilsFinger;
import com.c446.ironbound_artefacts.items.JudgementScale;
import com.c446.ironbound_artefacts.items.MagicianMonocle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);

    public static final DeferredHolder<Item, DevilsFinger> DEVILS_FINGER;
    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
    public static final DeferredHolder<Item, JudgementScale> JUDGEMENT_SCALE;
    public static final DeferredHolder<Item, JudgementScale> DEATH_AMULET;
//    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
//    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;
//    public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE;

    static {
        DEVILS_FINGER = ITEMS.register("devils_finger", () -> new DevilsFinger(new Item.Properties().fireResistant().stacksTo(1)));
        MAGICIANS_MONOCLE = ITEMS.register("magicians_monocle", () -> new MagicianMonocle(new Item.Properties().fireResistant().stacksTo(1)));
        JUDGEMENT_SCALE = ITEMS.register("judgement_scale", () -> new JudgementScale(new Item.Properties().fireResistant().stacksTo(1)));
//        DEATH_AMULET = ITEMS.register("death_amulet", () -> new JudgementScale(new Item.Properties().fireResistant().stacksTo(1)));

    }

}
