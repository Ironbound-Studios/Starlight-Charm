package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.effects.IronboundMobEffect;
import com.c446.ironbound_artefacts.registries.AttributeRegistry;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.spells.fire.FireballSpell;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static com.c446.ironbound_artefacts.registries.AttributeRegistry.INSIGHT;

@AutoSpellConfig
public class TimeStopSpell extends AbstractSpell {

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(3600)
            .build();

    public TimeStopSpell() {
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.baseManaCost = 700;
        this.manaCostPerLevel = 300;
        this.castTime = 300;
    }



    private final ResourceLocation spellId = IronboundArtefact.prefix("time_stop");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks((float) getTickDuration(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(this.getAOE(spellLevel, caster), 1))
        );
    }


    public double getTickDuration(int spellLevel, @NonNull LivingEntity caster) {
        return 20 * (spellLevel + caster.getAttributeValue(INSIGHT) * 0.5);
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        return player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.AMON);
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    public float getAOE(int spellLevel, LivingEntity entity) {
        return (10 * spellLevel + getSpellPower(spellLevel, entity));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        List<Entity> entityList = level.getEntities(entity, getInflated(spellLevel, entity));
        for (Entity e : entityList) {
            if (e instanceof LivingEntity l && !(l.equals(entity))) {
                l.addEffect(new MobEffectInstance(EffectsRegistry.TIME_STOP, (int) (this.getTickDuration(spellLevel, entity)), 0, true, true));
            }
        }
        entity.addEffect(new MobEffectInstance(EffectsRegistry.TIME_STOP_CASTER, (int) this.getTickDuration(spellLevel, entity), 0));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public @NotNull AABB getInflated(int spellLevel, LivingEntity entity) {
        return new AABB(0, 0, 0, 0, 0, 0).inflate(getAOE(spellLevel, entity));
    }
}