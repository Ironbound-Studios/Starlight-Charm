package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;


@EventBusSubscriber(modid = IronboundArtefact.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AttributeRegistry {
    public static final HashMap<DeferredHolder<Attribute, Attribute>, UUID> UUIDS = new HashMap<DeferredHolder<Attribute, Attribute>, UUID>();
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, IronboundArtefact.MODID);

    public static final DeferredHolder<Attribute, Attribute> VOID_DAMAGE_ATTRIBUTE = registerAttribute("ironbounds_artefacts.attribute.void_damage", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 1024.0D).setSyncable(true), UUID.fromString("6be02cbc-852c-49ea-bf33-906be7f97374"));


    public static DeferredHolder<Attribute, Attribute> registerAttribute(String name, Function<String, Attribute> attribute, UUID uuid) {
        DeferredHolder<Attribute, Attribute> registryObject = ATTRIBUTES.register(name, () -> attribute.apply(name));
        UUIDS.put(registryObject, uuid);
        return registryObject;
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().stream().filter(e -> e == EntityType.PLAYER).forEach(e -> {
            ATTRIBUTES.getEntries().forEach((v) -> {
                event.add(e, v);
            });
        });
    }

}
