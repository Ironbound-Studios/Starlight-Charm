package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class ModSetup {
    public static void register(IEventBus eventBus) {
        ItemRegistry.ITEMS.register(eventBus);
        Attributes.ATTRIBUTES.register(eventBus);
        //ArmorMaterials.MATERIALS.register(eventBus);
        //ModCreativeTabs.CREATIVE_MOB_TABS.register(eventBus);
        //ModIngredientTypeRegistry.INGREDIENT_TYPES.register(eventBus);
    }
    public ModSetup(IEventBus modEventBus, ModContainer modContainer) {
        ModSetup.register(modEventBus);
        //modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);

        modEventBus.addListener(this::setup);
    }

    public static ResourceLocation prefix(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public void setup(final FMLCommonSetupEvent event) {
        // DO OTHER MODS CONFIG
    }

}

