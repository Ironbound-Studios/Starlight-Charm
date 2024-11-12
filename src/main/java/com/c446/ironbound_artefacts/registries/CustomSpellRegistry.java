package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.ironbound_spells.spells.SimulacrumSpell;
import com.c446.ironbound_artefacts.ironbound_spells.spells.TimeStopSpell;
import com.c446.ironbound_artefacts.ironbound_spells.spells.WishSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class CustomSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.REGISTRY, MODID);

    public static final Supplier<AbstractSpell> TIME_STOP;
    public static final Supplier<AbstractSpell> WISH;
    public static final Supplier<AbstractSpell> SIMULACRUM;

    private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    static{
        TIME_STOP = registerSpell(new TimeStopSpell());
        WISH = registerSpell(new WishSpell());
        SIMULACRUM = registerSpell(new SimulacrumSpell());
    }

}
