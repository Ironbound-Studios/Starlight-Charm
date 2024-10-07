package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.effects.IronboundMobEffect;
import io.redspace.ironsspellbooks.effect.PlanarSightEffect;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class EffectsRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);
    public static final DeferredHolder<MobEffect, MobEffect> VOID_POISON = EFFECTS.register("planar_sight", () -> new IronboundMobEffect(MobEffectCategory.BENEFICIAL, rgbToInt(120,0,200)));

public static int rgbToInt(int red, int green, int blue) {
    return ((red << 16) | (green << 8) | blue);
}
}
