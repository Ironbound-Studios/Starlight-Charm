package com.c446.ironbound_artefacts.items;

import net.minecraft.world.entity.Entity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public abstract class UserDependantCurios extends BaseItem implements ICurioItem {
    public UserDependantCurios(Properties p) {
        super(p);
    }

    public UserDependantCurios(Properties p, boolean showEnch) {
        super(p, showEnch);
    }

    public boolean canEntityUseItem(Entity entity){
        return false;
    }
}
/*
* DONE :
- MAGICIAN : Magician's monocle : Grants a large pool of mana. It might be able to increases the range of spells.
- DEVIL : Devil's Finger : Grants Eldritch spell power
* TDL :
- JUDGEMENT : Golden Scale : Grants increased armor at the price of less armor
- HERMIT : Recluse's Eye : EFFECTS TO BE DONE
- EMPEROR : Emperor's Crown : Grants access to "Summon Greater Undead", summoning guards that scale up to diamond in function of spell power.
- DEATH : Death Necklace : Grants "void damage" that ignores armor and protection.
- TOWER : Tower Resident Amulet : Grants a large Mana Pool (scales with max mana).
- SUN : Solar Splendor (necklace) : Grants Fire school spell power.
- THE FOOL : TBD
* */