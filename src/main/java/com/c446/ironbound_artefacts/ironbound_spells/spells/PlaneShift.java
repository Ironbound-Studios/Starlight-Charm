package com.c446.ironbound_artefacts.ironbound_spells.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.resources.ResourceLocation;

@AutoSpellConfig
public class PlaneShift extends AbstractSpell {

    @Override
    public ResourceLocation getSpellResource() {
        return SchoolRegistry.ENDER_RESOURCE;
    }

    public static final DefaultConfig config = new DefaultConfig().setCooldownSeconds(20*100).setMaxLevel(1).setMinRarity(SpellRarity.LEGENDARY).build();

    @Override
    public DefaultConfig getDefaultConfig() {
        return config;
    }

    public PlaneShift(){
        config.allowCrafting=true;
        config.
    }

    @Override
    public CastType getCastType() {
        return null;
    }
}
