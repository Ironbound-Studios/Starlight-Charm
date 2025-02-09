package com.c446.ironbound_artefacts.entities.electromancer;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class ElectromancerEntity extends AbstractSpellCastingMob {
    protected ElectromancerEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 100;
    }

    public boolean isHostileTowards(LivingEntity e){
        return this.isAlliedTo(e);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, (new WizardAttackGoal(this, (double) 1.25F, 25, 50)).setSpells(
                List.of(SpellRegistry.BALL_LIGHTNING_SPELL.get(),
                        SpellRegistry.BALL_LIGHTNING_SPELL.get(),
                        SpellRegistry.BALL_LIGHTNING_SPELL.get(),
                        SpellRegistry.LIGHTNING_BOLT_SPELL.get(),
                        SpellRegistry.LIGHTNING_LANCE_SPELL.get()),
                List.of(),
                List.of(
                        SpellRegistry.BURNING_DASH_SPELL.get()),
                List.of()
        ).setDrinksPotions().setSpellQuality(0.75f,1f));
        this.goalSelector.addGoal(3, new PatrolNearLocationGoal(this, 30.0F, (double) 0.75F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isHostileTowards));
    }
}
