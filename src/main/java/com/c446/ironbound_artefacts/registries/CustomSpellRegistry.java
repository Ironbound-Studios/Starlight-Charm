package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.ironbound_spells.spells.*;
import com.c446.ironbound_artefacts.items.impl.lore_items.StaffOfMagi;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class CustomSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.REGISTRY, MODID);

    public static final Supplier<AbstractSpell> TIME_STOP;
    public static final Supplier<AbstractSpell> WISH;
    public static final Supplier<AbstractSpell> DIVINE_BOON;
    public static final Supplier<AbstractSpell>  KERESHKA_FAVOR;
    //public static final Supplier<AbstractSpell> SIMULACRUM;
    //public static final Supplier<AbstractSpell> FASLSE_LIFE;

    private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    static{
        TIME_STOP = registerSpell(new TimeStopSpell());
        WISH = registerSpell(new WishSpell());
        DIVINE_BOON = registerSpell(new DivineGiftSpell());
        KERESHKA_FAVOR = registerSpell(new StaffOfMagi.KereshkaFavor());
        //SIMULACRUM = registerSpell(new SimulacrumSpell());
        //FASLSE_LIFE = registerSpell(new FalseLifeSpell());
    }

}
