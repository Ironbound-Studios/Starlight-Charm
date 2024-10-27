package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.DataAttachmentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MAX_MANA;

@AutoSpellConfig
public class WishSpell extends AbstractSpell {
    private final ResourceLocation spellId = IronboundArtefact.prefix("wish");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(3600)
            .build();

    public WishSpell() {
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 250;
        this.manaCostPerLevel = 250;
        this.castTime = 60;
    }

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
        return CastType.INSTANT;
    }

    @Override
    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {
        return spellLevel;
    }

    protected void consumeOneFromStack(ItemStack stack) {
        stack.setCount(stack.getCount() - 1);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        ItemStack offHand = entity.getOffhandItem();
        // MANA REGEN POT
        if (offHand.getItem().equals(ItemRegistry.ARCANE_ESSENCE.get())) {
            entity.getData(DataAttachmentRegistry.MAGIC_DATA).addMana((float) entity.getAttributeValue(MAX_MANA) * this.getSpellPower(spellLevel, entity) * .25F);
            consumeOneFromStack(offHand);
        }
        else if (offHand.getItem().equals(Items.PHANTOM_MEMBRANE)) {
            entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 120, (int) (1 / 2 * this.getSpellPower(spellLevel, entity))));
            consumeOneFromStack(offHand);
        }
        else if (offHand.getItem().equals(Items.MAGMA_CREAM)) {
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120, (int) (1 / 2 * this.getSpellPower(spellLevel, entity))));
            consumeOneFromStack(offHand);
        }
        else if (offHand.getItem().equals(Items.GOLD_BLOCK)) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, (int) (1 / 2 * this.getSpellPower(spellLevel, entity))));
            consumeOneFromStack(offHand);

        }
        else if (offHand.getItem().equals(Items.GHAST_TEAR)) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, (int) (1 / 2 * this.getSpellPower(spellLevel, entity))));
            consumeOneFromStack(offHand);
        }
        else if (offHand.has(ComponentRegistry.SPELL_CONTAINER)) {
            var spells = offHand.get(ComponentRegistry.SPELL_CONTAINER).getActiveSpells();
            var num = level.random.nextIntBetweenInclusive(0, spells.size());
            spells.get(num).getSpell().castSpell(level, spells.get(num).getLevel(), (ServerPlayer) entity, castSource, true);
        }
        else {
            AtomicBoolean isFocus = new AtomicBoolean(false);
            ArrayList<AbstractSpell> schoolSpells;
            AtomicReference<SchoolType> school = new AtomicReference<>();
            SchoolRegistry.REGISTRY.keySet().forEach(a -> {
                if (SchoolRegistry.getSchool(a).isFocus(offHand)) {
                    isFocus.set(true);
                    school.set(SchoolRegistry.getSchool(a));
                }
            });
            ArrayList<AbstractSpell> spells = new ArrayList<>();
            SpellRegistry.REGISTRY.keySet().forEach(a -> {
                if (SpellRegistry.getSpell(a).getSchoolType().equals(school.get())) {
                    spells.add(SpellRegistry.getSpell(a));
                }
            });
            super.onCast(level, spellLevel, entity, castSource, playerMagicData);
            return;
        }

        entity.addEffect(new MobEffectInstance(EffectsRegistry.VOID_POISON, 1 ,2));

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
