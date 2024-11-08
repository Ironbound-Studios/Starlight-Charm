package com.c446.ironbound_artefacts.simulacrum;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import com.c446.ironbound_artefacts.IronboundArtefact;

@SuppressWarnings("unchecked")
@EventBusSubscriber(value = Dist.CLIENT, modid = IronboundArtefact.MODID, bus = EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientHandler {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(IBEntitiesReg.SIMULACRUM.get(), SimulacrumRenderer::new);
    }
}
