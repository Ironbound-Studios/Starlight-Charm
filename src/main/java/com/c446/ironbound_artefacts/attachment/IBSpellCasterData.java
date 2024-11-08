package com.c446.ironbound_artefacts.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class IBSpellCasterData implements INBTSerializable<CompoundTag> {
    UUID simulacrumUUID = null;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        tag.putUUID("simulacrum_uuid", this.simulacrumUUID);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.simulacrumUUID = nbt.getUUID("simulacrum_uuid");
    }

    public void killSimulacrum(ServerLevel level) {
        var entity = level.getEntity(this.simulacrumUUID);
        if (entity != null) {
            entity.discard();
        }
        this.simulacrumUUID = null;
    }

    public void setSimulacrumUUID(UUID simulacrumUUID) {
        this.simulacrumUUID = simulacrumUUID;
    }

    public UUID getSimulacumUUID() {
        return this.simulacrumUUID;
    }
}
