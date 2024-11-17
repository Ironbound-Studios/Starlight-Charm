package com.c446.ironbound_artefacts.entities.simulacrum;

import com.c446.ironbound_artefacts.datagen.Tags;
import com.c446.ironbound_artefacts.registries.IBEntitiesReg;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.SupportMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SimulacrumEntity extends NeutralWizard implements IMagicSummon, SupportMob {
    private PlayerInfo playerInfo = null;
    private Player player = null;
    public float quality = 1F;

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
        System.out.println("current quality : " +quality);
        this.quality = quality;
        //this.playerInfo = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(this.getSummoner().getUUID());
    }

    public SimulacrumEntity(Level pLevel) {
        this(IBEntitiesReg.SIMULACRUM.get(), pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        var attr = Player.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 30D)
                .add(Attributes.MOVEMENT_SPEED, 1);
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
        if (pEntity instanceof Player player) {
            return player.equals(this.getSummoner());
        }
        if (this.getSummoner() != null) {
            return pEntity.isAlliedTo(this.getSummoner());
        }
        return false;
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
            this.playerInfo = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(player.getUUID());
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
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.POOF, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            discard();
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

    public List<AbstractSpell> getSupportSpells(List<AbstractSpell> spells, Player player) {
        System.out.println("getting support spells");
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
        this.goalSelector.removeAllGoals(a -> a instanceof WizardAttackGoal || a instanceof WizardSupportGoal<?>);

        if (this.getSummoner() instanceof Player player) {
            this.goalSelector.addGoal(1, new WizardAttackGoal(this, 1.25f, 25, 80)
                    .setSpells(
                            getOffensiveSpellsFromList(simpleGetSpells(player), player),
                            getDefensiveSpells(simpleGetSpells(player), player),
                            getMovementSpells(simpleGetSpells(player), player),
                            getUtilSpells(simpleGetSpells(player), player)
                    ).setSpellQuality(this.quality * 0.75f, this.quality)
            );
            System.out.println("min quality : " +this.quality * 0.75 + "max quality : "+this.quality);
            this.goalSelector.addGoal(2, new WizardSupportGoal<SimulacrumEntity>(this, 1.25f, 100, 180)
                    .setSpells(
                            getDefensiveSpells(simpleGetSpells(player), player), (getUtilSpells(simpleGetSpells(player), player))
                    ).setSpellQuality(this.quality * 0.75f, this.quality)
            );
        }
    }

    @Override
    public double getAttributeValue(Holder<Attribute> pAttribute) {
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

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GenericFollowOwnerGoal(this, this::getSummoner, 1f, 15, 5, false, 25));
        //this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, LivingEntity.class, 6.0F, 1.0, 1.2, this::isAngryAt));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1f, false));
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

    private LivingEntity helpTarget;

    @Override
    public @Nullable LivingEntity getSupportTarget() {
        if (this.getBoundingBox().inflate(20).intersects(this.getSummoner().getBoundingBox())) {
            this.helpTarget = this.getSummoner();
            return getSummoner();
        } else if (this.helpTarget == null) {
            AtomicReference<LivingEntity> target = new AtomicReference<>();
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(20)).forEach(
                    a -> {
                        if (a.isAlliedTo(this.getSummoner())) {
                            target.set(a);
                        }
                    }
            );
            this.helpTarget = target.get();
            return target.get();
        }
        return this.helpTarget;
    }

    @Override
    public void setSupportTarget(LivingEntity target) {
        this.helpTarget = target;
    }
}
