package com.c446.ironbound_artefacts.items.armor.archmagi_weave;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.armor.arcane_weave.ArcaneWeaveItem;
import com.c446.ironbound_artefacts.items.armor.arcane_weave.ArcaneWeaveModel;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ArchmagiWeaveModel extends DefaultedItemGeoModel<ArchmagiWeaveItem> {

    public ArchmagiWeaveModel() {
        super(IronboundArtefact.prefix(""));
    }

    @Override
    public ResourceLocation getModelResource(ArchmagiWeaveItem object) {


        return IronboundArtefact.prefix("geo/archmage_weave.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ArchmagiWeaveItem object) {
        return IronboundArtefact.prefix("textures/armor/archmage_weave.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ArchmagiWeaveItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "animations/wizard_armor_animation.json");
    }
}