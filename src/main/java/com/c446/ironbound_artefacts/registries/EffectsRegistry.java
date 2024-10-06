package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class EffectsRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);
//    public static final DeferredHolder<MobEffect, MobEffect> VOID_POISON = DeferredHolder.create(IronboundArtefact.prefix("death_touched"), () -> new com.c446.ironbound_artefacts.effects.IronboundMobEffect())
}
