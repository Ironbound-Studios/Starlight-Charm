package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.ender.StarfallSpell;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModModelsProvider extends ItemModelProvider {
    public ModModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, IronboundArtefact.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ItemRegistry.PROTECTION_RING.get());
        basicItem(ItemRegistry.MAGIC_DEFENSE_RING.get());
        basicItem(ItemRegistry.ELVEN_CHAINS.get());
        basicItem(ItemRegistry.ARCANE_PROTECTION_CLOAK.get());
        basicItem(ItemRegistry.THREE_WISHES.get());
        basicItem(ItemRegistry.AMULET_OF_HOLDING.get());
        //basicItem(ItemRegistry.AMULET_OF_MANA.get());
        basicItem(ItemRegistry.WEAVE_HELMET.get());
        basicItem(ItemRegistry.WEAVE_CHEST_PLATE.get());
        basicItem(ItemRegistry.WEAVE_LEGGINGS.get());
        basicItem(ItemRegistry.WEAVE_BOOTS.get());
    }
}
