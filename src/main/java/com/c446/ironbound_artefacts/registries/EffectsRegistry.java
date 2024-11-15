package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.effects.IronboundMobEffect;
import com.c446.ironbound_artefacts.effects.TimeStopEffect;
import com.c446.ironbound_artefacts.effects.StoppingTimeEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class EffectsRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);
    public static final DeferredHolder<MobEffect, IronboundMobEffect.VoidPoison> VOID_POISON = EFFECTS.register("void_poison", () -> new IronboundMobEffect.VoidPoison(MobEffectCategory.BENEFICIAL, rgbToInt(120, 0, 200)));
    public static final DeferredHolder<MobEffect, StoppingTimeEffect> TIME_STOP_CASTER = EFFECTS.register("stopping_time", () -> new StoppingTimeEffect(MobEffectCategory.BENEFICIAL, rgbToInt(120, 0, 200)));

    public static final DeferredHolder<MobEffect, TimeStopEffect> TIME_STOP = EFFECTS.register("time_frozen", () -> new TimeStopEffect(MobEffectCategory.BENEFICIAL, rgbToInt(120, 0, 200)).addAttributeModifier(Attributes.MOVEMENT_SPEED, IronboundArtefact.prefix("time_stop_speed_mod"), -1d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));


    public static int rgbToInt(int red, int green, int blue) {
        return ((red << 16) | (green << 8) | blue);
    }
}
