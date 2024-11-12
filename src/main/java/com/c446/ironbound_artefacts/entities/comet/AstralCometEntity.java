package com.c446.ironbound_artefacts.entities.comet;

import io.redspace.ironsspellbooks.entity.spells.fireball.MagicFireball;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class AstralCometEntity extends MagicFireball {
    public AstralCometEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void trailParticles() {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, this.getX(), this.getY(), this.getZ(), 10,0,0,0,0);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {

    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }
}
