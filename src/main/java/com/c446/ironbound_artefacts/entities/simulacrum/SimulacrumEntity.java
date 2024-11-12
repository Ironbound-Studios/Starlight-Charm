package com.c446.ironbound_artefacts.entities.simulacrum;

import com.c446.ironbound_artefacts.datagen.Tags;
import com.c446.ironbound_artefacts.registries.IBEntitiesReg;
import com.c446.ironbound_artefacts.registries.AttachmentRegistry;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimulacrumEntity extends AbstractSpellCastingMob implements IMagicSummon {
    /**
     * The owner can NEVER be something other than a player.
     * The simulacrum is meant to be a player-only spell due to how it "gathers" spells.
     * If you wish to add an entity to it, I recommend sending me a message on discord, with the code to make it happen...
     *
     * @param pLevel : the server level to add the simulacrum to.
     * @author : disc : @clement446
     */
    public SimulacrumEntity(Level pLevel) {
        this(IBEntitiesReg.SIMULACRUM.get(), pLevel);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Player.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 30D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
    }

    @Override
    public double getAttributeValue(Holder<Attribute> pAttribute) {
        if (this.player != null){
            return player.getAttributeValue(pAttribute);
        }
        else {
            return createAttributes().build().getValue(pAttribute);
        }
    }

    public SimulacrumEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setUUID(UUID.randomUUID());
    }

    private PlayerInfo playerInfo = null;
    private @NotNull Player player;

    protected UUID summonerUUID;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SimulacrumEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public void setSummoner(Player player) {
        this.player = player;
        this.summonerUUID = player.getUUID();
    }

    public boolean isSlim() {
        return this.playerInfo.getSkin().model().equals(PlayerSkin.Model.SLIM);
    }
    @Override
    public LivingEntity getSummoner() {
        if (this.player == null) {
            System.out.println("player is/was null !!! Why is this happening !!!!!!");
            //this.discard();
        }
        System.out.println("player is/was not null. The good ending :3c");
        return this.player;
    }

    @Override
    public void onUnSummon() {
        if (this.player.hasData(AttachmentRegistry.GENERIC_CASTING_DATA)) {
            var data = this.player.getData(AttachmentRegistry.GENERIC_CASTING_DATA);
            data.setSimulacrumUUID(null);
            player.setData(AttachmentRegistry.GENERIC_CASTING_DATA, data);
        }
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        IMagicSummon.super.onAntiMagic(playerMagicData);
    }

    @Override
    public boolean shouldIgnoreDamage(DamageSource damageSource) {
        return IMagicSummon.super.shouldIgnoreDamage(damageSource);
    }

    @Override
    public boolean isAlliedHelper(Entity entity) {
        return IMagicSummon.super.isAlliedHelper(entity);
    }

    @Override
    public void onDeathHelper() {
        IMagicSummon.super.onDeathHelper();
    }

    @Override
    public void onRemovedHelper(Entity entity, DeferredHolder<MobEffect, SummonTimer> holder) {
        IMagicSummon.super.onRemovedHelper(entity, holder);
    }

    @Override
    public void equip(@NotNull EquipmentTable pEquipmentTable, @NotNull LootParams pParams) {
        super.equip(pEquipmentTable, pParams);
    }

    @Override
    public void equip(@NotNull ResourceKey<LootTable> pEquipmentLootTable, @NotNull LootParams pParams, Map<EquipmentSlot, Float> pSlotDropChances) {
        super.equip(pEquipmentLootTable, pParams, pSlotDropChances);
    }

    @Override
    public void equip(@NotNull ResourceKey<LootTable> pEquipmentLootTable, @NotNull LootParams pParams, long pSeed, Map<EquipmentSlot, Float> pSlotDropChances) {
        super.equip(pEquipmentLootTable, pParams, pSeed, pSlotDropChances);
    }

    @Override
    public @Nullable EquipmentSlot resolveSlot(@NotNull ItemStack pStack, @NotNull List<EquipmentSlot> pExcludedSlots) {
        return super.resolveSlot(pStack, pExcludedSlots);
    }

    public List<SpellSelectionManager.SelectionOption> getspelllist(Player player) {
        var spells = new SpellSelectionManager(player);
        return spells.getAllSpells();
    }

    public ArrayList<AbstractSpell> simpleGetSpells(Player player) {
        var spells = this.getspelllist(player);
        ArrayList<AbstractSpell> listSpells = new ArrayList<>();
        spells.forEach(a -> {
            if (a.spellData.getSpell() != SpellRegistry.none()) {
                listSpells.add(a.spellData.getSpell());
            }
        });
        return listSpells;
    }

    public List<AbstractSpell> getOffensiveSpellsFromList(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();

        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.OFFENSIVE_SPELL)) {
                    list.add(spell);
                }
            });
        }
        return list;
    }

    public List<AbstractSpell> getDefensiveSpells(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();

        for (var spell : spells) {
            if (player.level().holderOrThrow(Tags.SpellTags.DEFENSIVE_SPELL.registry()).value().containsKey(spell.getSpellResource())) {
                list.add(spell);
            }
        }
        return list;
    }

    public List<AbstractSpell> getMovementSpells(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();

        for (var spell : spells) {
            if (player.level().holderOrThrow(Tags.SpellTags.MOUVEMENT_SPELL.registry()).value().containsKey(spell.getSpellResource())) {
                list.add(spell);
            }
        }
        return list;
    }

    public List<AbstractSpell> getUtilSpells(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();

        for (var spell : spells) {
            if (player.level().holderOrThrow(Tags.SpellTags.UTILITY_SPELL.registry()).value().containsKey(spell.getSpellResource())) {
                list.add(spell);
            }
        }
        return list;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        if (this.getSummoner() instanceof Player player) {
            this.goalSelector.addGoal(1, new WizardAttackGoal(this, 1.25f, 50, 75)
                    .setSpells(
                            getOffensiveSpellsFromList(simpleGetSpells(player), player),
                            getDefensiveSpells(simpleGetSpells(player), player),
                            getMovementSpells(simpleGetSpells(player), player),
                            getUtilSpells(simpleGetSpells(player), player)
                    )
            );
        }

        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
    }

    public ResourceLocation getSkinTextureLocation() {
        PlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultTexture() : networkplayerinfo.getSkin().texture();
    }

    protected UUID getOwnerUUID() {
        return this.summonerUUID;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    protected PlayerInfo getPlayerInfo() {
        if (this.getSummoner() == null) {
            this.playerInfo = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(getOwnerUUID());
        }
        return this.playerInfo;
    }


}
