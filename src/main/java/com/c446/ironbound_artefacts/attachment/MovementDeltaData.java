package com.c446.ironbound_artefacts.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class MovementDeltaData implements INBTSerializable<CompoundTag> {
    public float x = 0;
    public float y = 0;
    public float z = 0;

    public void setVec(Vec3 vec3){
        this.x= (float) vec3.x;
        this.y= (float) vec3.x;
        this.z= (float) vec3.x;
    }

    public Vec3 getVec(){
        return new Vec3(this.x, this.y, this.z);
    }

    public void setVec(float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        var tag = new CompoundTag();
        tag.putFloat("x", this.x);
        tag.putFloat("y", this.y);
        tag.putFloat("z", this.z);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        this.x = nbt.getFloat("x");
        this.y = nbt.getFloat("y");
        this.z = nbt.getFloat("z");
    }
}
