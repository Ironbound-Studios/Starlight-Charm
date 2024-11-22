package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumEntity;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SimulacrumSpell extends AbstractSpell {

    private final ResourceLocation spellId = IronboundArtefact.prefix("simulacrum");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).setMaxLevel(3).setCooldownSeconds(3600).build();

    public SimulacrumSpell() {
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 500;
        this.manaCostPerLevel = 500;
        this.castTime = 0;
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        return false;
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
        return (float) Math.max(0.1, 1f - (float) Utils.softCapFormula(super.getSpellPower(spellLevel, sourceEntity)));
    }

    public void doSpell(ServerLevel world, int spellLevel, Player player, CastSource castSource, MagicData playerMagicData) {
        if (player == null) {
            return;
        }
        float radius = 1.5f + .185f * spellLevel;
        for (int i = 0; i < spellLevel; i++) {
            var simulacrum = new SimulacrumEntity(world, player, this.getSpellPower(spellLevel, player));

            System.out.println(player);
                /*simulacrum.setItemSlot(EquipmentSlot.FEET, player.getItemBySlot(EquipmentSlot.FEET));
                simulacrum.setItemSlot(EquipmentSlot.LEGS, player.getItemBySlot(EquipmentSlot.LEGS));
                simulacrum.setItemSlot(EquipmentSlot.CHEST, player.getItemBySlot(EquipmentSlot.CHEST));
                simulacrum.setItemSlot(EquipmentSlot.HEAD, player.getItemBySlot(EquipmentSlot.HEAD));
                */

            simulacrum.setItemSlot(EquipmentSlot.MAINHAND, player.getItemBySlot(EquipmentSlot.MAINHAND));
            simulacrum.setItemSlot(EquipmentSlot.OFFHAND, player.getItemBySlot(EquipmentSlot.OFFHAND));

            simulacrum.setDropChance(EquipmentSlot.FEET, 0);
            simulacrum.setDropChance(EquipmentSlot.LEGS, 0);
            simulacrum.setDropChance(EquipmentSlot.CHEST, 0);
            simulacrum.setDropChance(EquipmentSlot.HEAD, 0);
            simulacrum.setDropChance(EquipmentSlot.MAINHAND, 0);
            simulacrum.setDropChance(EquipmentSlot.OFFHAND, 0);

            var yrot = 6.281f / spellLevel * i + player.getYRot() * Mth.DEG_TO_RAD;
            Vec3 spawn = Utils.moveToRelativeGroundLevel(world, player.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 0, radius * Mth.sin(yrot))), 10);
            simulacrum.setPos(spawn.x, spawn.y, spawn.z);
            simulacrum.setYRot(player.getYRot());
            simulacrum.setOldPosAndRot();
            //var event = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(entity, simulacrum, this.spellId, spellLevel));
            world.addFreshEntity(simulacrum);
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (level instanceof ServerLevel world) {
            if (entity instanceof Player player) {
                doSpell(world, spellLevel, player, castSource, playerMagicData);
            } else if (entity instanceof SimulacrumEntity simulacrum) {
                doSpell(world, spellLevel, simulacrum.getSummoner(), castSource, playerMagicData);
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
