package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.NeoForgeConfig;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.common.CuriosConfig;

import java.util.concurrent.CompletableFuture;

public class WishDuplicableTagsProvider extends IntrinsicHolderTagsProvider<Item> {
    public WishDuplicableTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
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
    }
}
