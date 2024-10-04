package com.c446.ironbound_artefacts.items;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public abstract class BaseCurios extends BaseItem implements ICurioItem {
    public BaseCurios(Properties p) {
        super(p);
    }

    public BaseCurios(Properties p, boolean showEnch) {
        super(p, showEnch);
    }
}
/*
* TDL :
- JUDGEMENT : Golden Scale : Grants increased armor at the price of less armor
- HERMIT : Hermit's Staff : Has got an imbued level 10 Telekinesis spell
- DEVIL : Devil's Ring : Grants Eldritch spell power
- EMPEROR : Emperor's Crown : Grants access to "Summon Greater Undead", summoning guards that scale up to diamond in function of spell power.
- DEATH : Death Necklace : Grants a great Vitality Bonus.
- TOWER : Tower Resident Amulet : Grants a large Mana Pool (scales with max mana).
- MAGICIAN : Magician's monocle : Grants a large pool of mana. It might be able to increases the range of spells.
- SUN : Solar Splendor (necklace) : Grants Fire school spell power.
- THE FOOL : TBD
* */