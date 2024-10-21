package com.c446.ironbound_artefacts;

import com.c446.ironbound_artefacts.effects.TimeStopEffect;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

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
        this.baseManaCost = 500;
        this.manaCostPerLevel = 250;
        this.castTime = 300;
    }

    private final ResourceLocation spellId = IronboundArtefact.prefix("time_stop");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks(getTickDuration(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(15 + 5 * getSpellPower(spellLevel, caster), 1))
        );
    }


    public int getTickDuration(int spellLevel, @NonNull LivingEntity caster) {
        return (int) (5 * this.getSpellPower(spellLevel, caster));
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


    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        List<Entity> entityList = level.getEntities(entity, new AABB(0, 0, 0, 0, 0, 0).inflate(15 + 5 * getSpellPower(spellLevel, entity)));
        for (Entity e : entityList) {
            if (e instanceof LivingEntity l && !(l.equals(entity))) {
                l.addEffect(new MobEffectInstance(EffectsRegistry.TIME_STOP, this.getTickDuration(spellLevel, entity), 0, true, true));
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
