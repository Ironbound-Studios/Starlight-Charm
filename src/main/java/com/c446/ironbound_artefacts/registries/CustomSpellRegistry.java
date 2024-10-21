package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.TimeStopSpell;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class CustomSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.REGISTRY, MODID);

    public static final Supplier<AbstractSpell> TIME_STOP;

    private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    static{
        TIME_STOP = registerSpell(new TimeStopSpell());
    }

}
