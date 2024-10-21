package com.c446.ironbound_artefacts.effects;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class TimeStopEffect extends MagicMobEffect {
    public TimeStopEffect(MobEffectCategory category, int color) {
        super(category, color);

    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.setDeltaMovement(0,0,0);
        return super.applyEffectTick(livingEntity, amplifier);
    }
}
