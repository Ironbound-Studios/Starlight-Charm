package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.attachment.IBSpellCasterData;
import com.c446.ironbound_artefacts.registries.AttachmentRegistry;
import com.c446.ironbound_artefacts.simulacrum.SimulacrumEntity;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

import static com.c446.ironbound_artefacts.registries.AttachmentRegistry.GENERIC_CASTING_DATA;

public class SimulacrumSpell extends AbstractSpell {

    private final ResourceLocation spellId = IronboundArtefact.prefix("wish");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(3600)
            .build();

    public SimulacrumSpell() {
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof Player player && level instanceof ServerLevel serverLevel){
            var simulacrum = new SimulacrumEntity(serverLevel, player);
            simulacrum.setUUID(UUID.randomUUID());
            var data = (player.hasData(GENERIC_CASTING_DATA)) ? (player.getData(GENERIC_CASTING_DATA)):(new IBSpellCasterData());
            data.killSimulacrum(serverLevel);
            data.setSimulacrumUUID(simulacrum.getUUID());

            simulacrum.setPos(Utils.raycastForEntity(serverLevel, player, 3, true).getLocation());

            player.setData(GENERIC_CASTING_DATA, data);
            
            serverLevel.addFreshEntity(simulacrum);
        }





        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
