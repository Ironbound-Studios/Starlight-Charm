package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import dev.shadowsoffire.apothic_attributes.api.ALObjects;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class FalseLifeSpell extends AbstractSpell {
    public FalseLifeSpell() {
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 300;
        this.manaCostPerLevel = 50;
        this.castTime = 100;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(6)
            .setCooldownSeconds(600)
            .build();

    private final ResourceLocation spellId = IronboundArtefact.prefix("false_life");

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, (int) (spellLevel * 20 * this.getSpellPower(spellLevel, entity)), this.baseSpellPower + this.spellPowerPerLevel, false, false));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
