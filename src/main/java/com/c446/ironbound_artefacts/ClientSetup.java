package com.c446.ironbound_artefacts;

import com.c446.ironbound_artefacts.registries.ItemRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

//@EventBusSubscriber(modid=IronboundArtefact.MODID, bus=EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClientSetup {

    /*@SubscribeEvent
    public static void ClientEventSetup(final FMLClientSetupEvent event){
        event.enqueueWork(()->{
            CuriosRendererRegistry.register(
                    ItemRegistry.FC.get(),
                    null
            );
        });
    }*/
}
