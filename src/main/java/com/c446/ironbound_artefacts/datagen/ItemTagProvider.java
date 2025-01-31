package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.ITEM, future, item -> item.builtInRegistryHolder().key(), IronboundArtefact.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Tags.ItemTags.WISH_DUPLICABLE)
                .add(ItemRegistry.ARCANE_ESSENCE.get())
                .add(ItemRegistry.CINDER_ESSENCE.get())
                .add(Items.GOLD_INGOT)
                .add(Items.IRON_INGOT)
                .add(Items.REDSTONE)
                .add(Items.AMETHYST_CLUSTER)
        ;
        tag(net.neoforged.neoforge.common.Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS)
                .add(com.c446.ironbound_artefacts.registries.ItemRegistry.DREAMS.get());
    }
}
