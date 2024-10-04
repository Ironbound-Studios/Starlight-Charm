package com.c446.ironbound_artefacts;

import com.c446.ironbound_artefacts.registries.ModSetup;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(IronboundArtefact.MODID)
public class IronboundArtefact {
    public static final String MODID = "ironbounds_artefacts";
    private static final Logger LOGGER = LogManager.getLogger();

    public IronboundArtefact(IEventBus modEventBus, ModContainer modContainer) {
        ModSetup.register(modEventBus);
//        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);

        modEventBus.addListener(this::setup);
    }

    public static ResourceLocation prefix(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public void setup(final FMLCommonSetupEvent event) {
//        event.enqueueWork(ArsNouveauRegistry::postInit);
    }
}
