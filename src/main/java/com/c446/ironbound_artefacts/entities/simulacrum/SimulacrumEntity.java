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
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.archevoker.ArchevokerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.priest.PriestEntity;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.ListTag;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.scores.Team;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
;

public class SimulacrumEntity extends NeutralWizard implements IMagicSummon {
    private PlayerInfo playerInfo = null;
    private Player player = null;
    public float quality = 0F;
    protected ListTag attributes = new ListTag();

    @Override
    public boolean isAngryAt(LivingEntity pTarget) {
        if (pTarget instanceof Player player && player.equals(this.getSummoner())) {
            return false;
        }
        return super.isAngryAt(pTarget);
    }

    /**
     * The owner can NEVER be something other than a player.
     * The simulacrum is meant to be a player-only spell due to how it "gathers" spells.
     * If you wish to add an entity to it, I recommend sending me a message on discord, with the code to make it happen...
     *
     * @param pLevel : the server level to add the simulacrum to.
     * @param player : the owner of the Summon.
     */
    public SimulacrumEntity(Level pLevel, @NotNull Player player, float quality) {
        this(IBEntitiesReg.SIMULACRUM.get(), pLevel);
        setSummoner(player);
        this.quality = quality;
        this.playerInfo = new PlayerInfo(player.getGameProfile(), false);

        this.attributes = player.getAttributes().save();

        this.setItemSlot(EquipmentSlot.FEET, this.player.getItemBySlot(EquipmentSlot.FEET));
        this.setItemSlot(EquipmentSlot.LEGS, this.player.getItemBySlot(EquipmentSlot.LEGS));
        this.setItemSlot(EquipmentSlot.CHEST, this.player.getItemBySlot(EquipmentSlot.CHEST));
        this.setItemSlot(EquipmentSlot.HEAD, this.player.getItemBySlot(EquipmentSlot.HEAD));
        this.setItemSlot(EquipmentSlot.MAINHAND, this.player.getItemBySlot(EquipmentSlot.MAINHAND));
        this.setItemSlot(EquipmentSlot.OFFHAND, this.player.getItemBySlot(EquipmentSlot.OFFHAND));

        this.setDropChance(EquipmentSlot.FEET, 0);
        this.setDropChance(EquipmentSlot.LEGS, 0);
        this.setDropChance(EquipmentSlot.CHEST, 0);
        this.setDropChance(EquipmentSlot.HEAD, 0);
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
        this.setDropChance(EquipmentSlot.OFFHAND, 0);
    }

    public SimulacrumEntity(Level pLevel) {
        this(IBEntitiesReg.SIMULACRUM.get(), pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        var attr = Player.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 30D)
                .add(Attributes.MOVEMENT_SPEED, .25)

                ;
        for (var attribute : BuiltInRegistries.ATTRIBUTE.registryKeySet()) {
            if (!attr.hasAttribute(BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(attribute))) {
                var holder = BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(attribute);
                attr.add(holder, holder.value().getDefaultValue());
            }
        }
        return attr;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof Player player){
            return player.equals(this.getSummoner());
        }
        return pEntity.isAlliedTo(this.getSummoner());
    }

    @Override
    public double getAttributeValue(@NotNull Holder<Attribute> pAttribute) {
        if (this.getSummoner() != null) {
            if (this.getSummoner().getAttributes().hasAttribute(pAttribute)) {
                return this.getSummoner().getAttributeValue(pAttribute);
            } else {
                return BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(Objects.requireNonNull(pAttribute.getKey())).value().getDefaultValue();
            }
        } else {
            return createAttributes().build().getValue(pAttribute);
        }
    }

    /**
     * Used for registring.
     * Don't use otherwise...
     */
    public SimulacrumEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setUUID(UUID.randomUUID());
        if (this.getOwnerUUID().isPresent()) {
            this.setSummoner(level().getPlayerByUUID(this.getOwnerUUID().get()));
        }
    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SimulacrumEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public void setSummoner(Player player) {
        if (player != null) {
            this.getEntityData().set(OWNER_UUID, Optional.of(player.getUUID()));
            this.player = player;
            this.registerWizardGoals();
        }
    }

    public boolean isSlim() {
        if (this.getPlayerInfo() == null) {
            return false;
        } else {
            return Objects.requireNonNull(this.getPlayerInfo()).getSkin().model().equals(PlayerSkin.Model.SLIM);
        }
    }

    @Override
    public Player getSummoner() {
        if (this.player == null) {
            //System.out.println("isClient : " + this.level().isClientSide + "player is/was null !!! Why is this happening !!!!!!");
            if (level().getPlayerByUUID(this.getOwnerUUID().get()) == null) {
                //System.out.println("isClient : " + this.level().isClientSide + "player UUID is absent from the player list.");
            } else {
                this.setSummoner(level().getPlayerByUUID(this.getOwnerUUID().get()));
            }
            //this.discard();
        } else {
            //System.out.println("isClient : " + this.level().isClientSide + "player is/was not null. The good ending :3c");
            return this.player;
        }
        return this.level().getPlayerByUUID(this.getOwnerUUID().get());
    }

    @Override
    public void onUnSummon() {
        if (this.getSummoner().hasData(AttachmentRegistry.GENERIC_CASTING_DATA)) {
            var data = this.getSummoner().getData(AttachmentRegistry.GENERIC_CASTING_DATA);
            data.setSimulacrumUUID(null);
            getSummoner().setData(AttachmentRegistry.GENERIC_CASTING_DATA, data);
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
        System.out.println("getting spells spells");
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
        System.out.println("getting offensive spells");
        var list = new ArrayList<AbstractSpell>();

        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.OFFENSIVE_SPELL)) {
                    list.add(spell);
                    System.out.println(spell.getSpellName());
                }
            });
        }
        return list;
    }

    public List<AbstractSpell> getDefensiveSpells(List<AbstractSpell> spells, Player player) {
        System.out.println("getting defensive spells");
        var list = new ArrayList<AbstractSpell>();

        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.DEFENSIVE_SPELL)) {
                    list.add(spell);
                    System.out.println(spell.getSpellName());
                }
            });
        }
        return list;
    }

    public List<AbstractSpell> getMovementSpells(List<AbstractSpell> spells, Player player) {
        System.out.println("getting movement spells");
        var list = new ArrayList<AbstractSpell>();

        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.MOUVEMENT_SPELL)) {
                    list.add(spell);
                    System.out.println(spell.getSpellName());
                }
            });
        }
        return list;
    }

    public List<AbstractSpell> getUtilSpells(List<AbstractSpell> spells, Player player) {
        System.out.println("getting utility spells");
        var list = new ArrayList<AbstractSpell>();

        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.UTILITY_SPELL)) {
                    list.add(spell);
                    System.out.println(spell.getSpellName());
                }
            });
        }
        return list;
    }

    protected void registerWizardGoals() {
        this.goalSelector.removeAllGoals(a -> {
            return a instanceof WizardAttackGoal;
        });

        if (this.getSummoner() instanceof Player player) {
            this.goalSelector.addGoal(1, new WizardAttackGoal(this, 1.25f, 25, 80)
                    .setSpells(
                            getOffensiveSpellsFromList(simpleGetSpells(player), player),
                            getDefensiveSpells(simpleGetSpells(player), player),
                            getMovementSpells(simpleGetSpells(player), player),
                            getUtilSpells(simpleGetSpells(player), player)
                    )
            );
        }


    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 3f, 15, 5, false, 25));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isHostileTowards));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (mob) -> mob instanceof Enemy && !(mob instanceof Creeper)));
        this.targetSelector.addGoal(2, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(5, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
    }

    public ResourceLocation getSkinTextureLocation() {
        PlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultTexture() : networkplayerinfo.getSkin().texture();
    }


    protected Optional<UUID> getOwnerUUID() {
        return this.entityData.get(OWNER_UUID);
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    protected PlayerInfo getPlayerInfo() {
        if (this.getSummoner() == null && this.getEntityData().get(OWNER_UUID).isPresent() && this.getOwnerUUID().isPresent()) {
            var uuid = this.getOwnerUUID().get();
            this.playerInfo = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(uuid);
            System.out.println("player info was missing.");
        }
        return this.playerInfo;
    }
}
