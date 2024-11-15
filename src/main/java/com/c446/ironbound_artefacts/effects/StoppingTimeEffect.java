package com.c446.ironbound_artefacts.effects;

import com.c446.ironbound_artefacts.ironbound_spells.spells.TimeStopSpell;
import com.c446.ironbound_artefacts.registries.CustomSpellRegistry;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.EffectCure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class StoppingTimeEffect extends IronboundMobEffect {
    public StoppingTimeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    protected StoppingTimeEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity caster, int spellLevel) {
        System.out.println("time stopping effect triggered");
        var timeStopSpell = (TimeStopSpell) CustomSpellRegistry.TIME_STOP.get();
        var area = timeStopSpell.getBoundingBox(caster, spellLevel);
        if (caster.level() instanceof ServerLevel level) {
            var entities = level.getEntities(caster, area);
            boolean finishSoon = Objects.requireNonNull(caster.getEffect(EffectsRegistry.TIME_STOP_CASTER)).endsWithin(1);
            if (!finishSoon) {
                for (Entity e : entities) {
                    if (e instanceof LivingEntity l) {
                        l.addEffect(new MobEffectInstance(EffectsRegistry.TIME_STOP, (int) (timeStopSpell.getTickDuration(spellLevel, caster)), 0, true, true));
                    } else {
                        e.setDeltaMovement(0, 0, 0);
                        e.setNoGravity(true);
                    }
                }
            } else {
                for (Entity e : entities) {
                    e.setNoGravity(false);
                }
            }


        }


        return super.applyEffectTick(caster, spellLevel);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        // Leave this empty to prevent milk or curatives from clearing
    }
}
