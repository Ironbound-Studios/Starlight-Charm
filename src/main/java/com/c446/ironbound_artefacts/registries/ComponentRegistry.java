package com.c446.ironbound_artefacts.registries;


import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.components.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;
//
public class ComponentRegistry {
    private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(IronsSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String pName, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
        return COMPONENTS.register(pName, () -> pBuilder.apply(DataComponentType.builder()).build());
    }

    private static final StreamCodec<ByteBuf, KillCounterComponent> KILL_COUNT = StreamCodec.composite(
            ByteBufCodecs.INT,KillCounterComponent::killCount,
            KillCounterComponent::new
    );

    public static final Codec<KillCounterComponent> KILLCOUNT_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("count").forGetter(KillCounterComponent::killCount)
    ).apply(builder, KillCounterComponent::new));

    private static final StreamCodec<ByteBuf, GenericUUIDComponent> UUID_COMPONENT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,GenericUUIDComponent::uuid,
            GenericUUIDComponent::new
    );

    public static final Codec<GenericUUIDComponent> UUID_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("uuid").forGetter(GenericUUIDComponent::uuid)
    ).apply(builder, GenericUUIDComponent::new));

    private static final StreamCodec<ByteBuf, HermitDataComponent> STREAM_HERMIT_EYE_COMPONENT_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, HermitDataComponent::xPos,
            ByteBufCodecs.INT, HermitDataComponent::yPos,
            ByteBufCodecs.INT, HermitDataComponent::zPos,
            ByteBufCodecs.STRING_UTF8, HermitDataComponent::playerUUID,
            ByteBufCodecs.BOOL, HermitDataComponent::isEmpty,
            ByteBufCodecs.INT, HermitDataComponent::invID,
            HermitDataComponent::new
    );

    private static final StreamCodec<ByteBuf, EternalDurabilityComponent> ETERNAL_DURABILITY_COMPONENT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, EternalDurabilityComponent::isApplied,
            EternalDurabilityComponent::new
    );

    private static final StreamCodec<ByteBuf, UniversalPositionComponent> UNIVERSAL_POS = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, UniversalPositionComponent::x,
            ByteBufCodecs.DOUBLE, UniversalPositionComponent::y,
            ByteBufCodecs.DOUBLE, UniversalPositionComponent::z,
            ByteBufCodecs.STRING_UTF8, UniversalPositionComponent::dimension,
            UniversalPositionComponent::new
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<HermitDataComponent>> HERMIT_SCROLL_FORGE_COMPONENT = COMPONENTS.registerComponentType("hermit_component", builder -> builder.networkSynchronized(STREAM_HERMIT_EYE_COMPONENT_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UniversalPositionComponent>> UNIVERSAL_POS_COMPONENT = COMPONENTS.registerComponentType("phylactery", builder -> builder.networkSynchronized(UNIVERSAL_POS));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EternalDurabilityComponent>> ETERNAL_DURA_COMPONENT = COMPONENTS.registerComponentType("eternal_dura_component", builder -> builder.networkSynchronized(ETERNAL_DURABILITY_COMPONENT_STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<KillCounterComponent>> KILL_COUNT_COMPONENT = COMPONENTS.registerComponentType("kill_count", builder -> builder.networkSynchronized(KILL_COUNT).persistent(KILLCOUNT_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GenericUUIDComponent>> UUID_DATA_COMPONENT = COMPONENTS.registerComponentType("uuid_component", builder -> builder.networkSynchronized(UUID_COMPONENT_STREAM_CODEC).persistent(UUID_CODEC));
}
