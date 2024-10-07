package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.internal.NeoForgeDamageTypeTagsProvider;

public class DamageSourcesReg {
    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, IronboundArtefact.prefix(name));
    }

    public static final ResourceKey<DamageType> VOID_DAMAGE = register("void_damage");
}
