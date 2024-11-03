package com.c446.ironbound_artefacts.effects;

import com.c446.ironbound_artefacts.ironbound_spells.spells.TimeStopSpell;
import com.c446.ironbound_artefacts.registries.CustomSpellRegistry;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class StoppingTimeEffect extends IronboundMobEffect {
    public StoppingTimeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    protected StoppingTimeEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        var area = ((TimeStopSpell) CustomSpellRegistry.TIME_STOP.get()).getInflated(amplifier, livingEntity);
        if (livingEntity.level() instanceof ServerLevel level) {
            var entities = level.getEntities(livingEntity, area);
            boolean finishSoon = Objects.requireNonNull(livingEntity.getEffect(EffectsRegistry.TIME_STOP_CASTER)).endsWithin(1);
            if (!finishSoon) {
                for (Entity e : entities) {
                    e.setDeltaMovement(0, 0, 0);
                    e.setNoGravity(true);
                }
            } else {
                for (Entity e: entities){
                    e.setNoGravity(false);
                }
            }


        }


        return super.applyEffectTick(livingEntity, amplifier);
    }


}
