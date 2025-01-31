package com.c446.ironbound_artefacts.entities.archmage;

import com.c446.ironbound_artefacts.datagen.Tags;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArchmageEntity extends AbstractSpellCastingMob implements Enemy {
    protected ArchmageEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HashMap<TagKey<AbstractSpell>, List<AbstractSpell>> getSpellsFor(SchoolType schoolType) {
        List<AbstractSpell> simpleSpells = new ArrayList<>();
        List<AbstractSpell> barrageSpells = new ArrayList<>();
        List<AbstractSpell> singleSpells = new ArrayList<>();
        for (var thing : SpellRegistry.REGISTRY.entrySet()) {
            var key = SpellRegistry.REGISTRY.getHolderOrThrow(thing.getKey());
            var spell = thing.getValue();
            if (spell.getSchoolType().equals(schoolType)) {
                if (key.is(Tags.SpellTags.ARCHMAGE_ALLOWED_SPELL)   ) {
                    simpleSpells.add(spell);
                    // add
                } else if (key.is(Tags.SpellTags.ARCHMAGE_BARRAGE_SPELL)) {
                    barrageSpells.add(spell);
                } else if (key.is(Tags.SpellTags.ARCHMAGE_SINGLE_SPELL)) {
                    singleSpells.add(spell);
                }
            }
        }
        HashMap<TagKey<AbstractSpell>, List<AbstractSpell>> spells = new HashMap<>();
        spells.put(Tags.SpellTags.ARCHMAGE_BARRAGE_SPELL, barrageSpells);
        spells.put(Tags.SpellTags.ARCHMAGE_SINGLE_SPELL, singleSpells);
        spells.put(Tags.SpellTags.ARCHMAGE_ALLOWED_SPELL, simpleSpells);
        return spells;
    }

    public void setSchoolGoal(){
//        this.goalSelector.addGoal(1,);
    }

    protected void registerGoals() {
        this.goalSelector.removeAllGoals((goal)->true);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, true));



    //DeadKingBoss
    }

}
