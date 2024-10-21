package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.effects.IronboundMobEffect;
import com.c446.ironbound_artefacts.effects.TimeStopEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class EffectsRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);
    public static final DeferredHolder<MobEffect, MobEffect> VOID_POISON = EFFECTS.register("void_poison", () -> new IronboundMobEffect(MobEffectCategory.BENEFICIAL, rgbToInt(120,0,200)));
    public static final DeferredHolder<MobEffect, MobEffect> TIME_STOP;

    static {
        TIME_STOP = EFFECTS.register("time_frozen", () -> new TimeStopEffect(MobEffectCategory.BENEFICIAL, rgbToInt(120, 0, 200))
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, IronboundArtefact.prefix("time_stop_speed_mod"), -1d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    public static int rgbToInt(int red, int green, int blue) {
    return ((red << 16) | (green << 8) | blue);
}
}
