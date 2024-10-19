package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.components.HermitComponentData;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ComponentRegistry {
    private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(IronsSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String pName, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
        return COMPONENTS.register(pName, () -> pBuilder.apply(DataComponentType.builder()).build());
    }

    public static final StreamCodec<ByteBuf, HermitComponentData> STREAM_HERMIT_EYE_COMPONENT_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, HermitComponentData::xPos,
            ByteBufCodecs.INT, HermitComponentData::yPos,
            ByteBufCodecs.INT, HermitComponentData::zPos,
            ByteBufCodecs.STRING_UTF8, HermitComponentData::playerUUID,
            ByteBufCodecs.BOOL, HermitComponentData::isEmpty,
            ByteBufCodecs.INT, HermitComponentData::invID,
            HermitComponentData::new
    );

    public static double Somme(int... ints) {
        double sum = 0;
        for (int i : ints) {
            sum += i;
        }
        return sum;
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<HermitComponentData>> HERMIT_SCROLL_FORGE_COMPONENT = COMPONENTS.registerComponentType("hermit_component", hermitEyeComponentBuilder -> hermitEyeComponentBuilder.networkSynchronized(STREAM_HERMIT_EYE_COMPONENT_CODEC));
}
