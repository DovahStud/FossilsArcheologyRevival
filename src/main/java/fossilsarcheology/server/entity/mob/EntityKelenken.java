package fossilsarcheology.server.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import fossilsarcheology.server.entity.EntityPrehistoric;
import fossilsarcheology.server.entity.ai.DinoAIAttackOnCollide;
import fossilsarcheology.server.entity.ai.DinoAIEatFeeders;
import fossilsarcheology.server.entity.ai.DinoAIEatItems;
import fossilsarcheology.server.entity.ai.DinoAIFollowOwner;
import fossilsarcheology.server.entity.ai.DinoAIHunt;
import fossilsarcheology.server.entity.ai.DinoAILookIdle;
import fossilsarcheology.server.entity.ai.DinoAIRiding;
import fossilsarcheology.server.entity.ai.DinoAIWander;
import fossilsarcheology.server.entity.ai.DinoAIWatchClosest;
import fossilsarcheology.server.enums.EnumPrehistoric;
import fossilsarcheology.server.enums.EnumPrehistoricAI;

public class EntityKelenken extends EntityPrehistoric {

	public EntityKelenken(World world) {
		super(world, EnumPrehistoric.Kelenken, 1, 6, 10, 40, 0.25, 0.35);
		this.getNavigator().setAvoidsWater(true);
		this.getNavigator().setCanSwim(true);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new DinoAIRiding(this, 1.0F));
		this.tasks.addTask(4, new DinoAIAttackOnCollide(this, 2.0D, false));
		this.tasks.addTask(5, new DinoAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new DinoAIEatFeeders(this, 1));
        this.tasks.addTask(6, new DinoAIEatItems(this, 1));
        this.tasks.addTask(7, new DinoAIWander(this, 1.0D));
		this.tasks.addTask(8, new DinoAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new DinoAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(4, new DinoAIHunt(this, 20, false));
		this.setActualSize(1.4F, 2.5F);
		this.nearByMobsAllowed = 3;
		minSize = 0.4F;
		maxSize = 1.4F;
		teenAge = 4;
		developsResistance = true;
		breaksBlocks = false;
		this.ridingY = 2.4F;
		this.pediaScale = 25F;
	}

	@Override
	public int getAttackLength() {
		return 25;
	}

	@Override
	public void setSpawnValues() {
	}

	@Override
	public EnumPrehistoricAI.Activity aiActivityType() {

		return EnumPrehistoricAI.Activity.DIURINAL;
	}

	@Override
	public EnumPrehistoricAI.Attacking aiAttackType() {

		return EnumPrehistoricAI.Attacking.STOMP;
	}

	@Override
	public EnumPrehistoricAI.Climbing aiClimbType() {

		return EnumPrehistoricAI.Climbing.NONE;
	}

	@Override
	public EnumPrehistoricAI.Following aiFollowType() {

		return EnumPrehistoricAI.Following.AGRESSIVE;
	}

	@Override
	public EnumPrehistoricAI.Jumping aiJumpType() {

		return EnumPrehistoricAI.Jumping.BASIC;
	}

	@Override
	public EnumPrehistoricAI.Response aiResponseType() {

		return EnumPrehistoricAI.Response.TERITORIAL;
	}

	@Override
	public EnumPrehistoricAI.Stalking aiStalkType() {

		return EnumPrehistoricAI.Stalking.NONE;
	}

	@Override
	public EnumPrehistoricAI.Taming aiTameType() {

		return EnumPrehistoricAI.Taming.IMPRINTING;
	}

	@Override
	public EnumPrehistoricAI.Untaming aiUntameType() {

		return EnumPrehistoricAI.Untaming.STARVE;
	}

	@Override
	public EnumPrehistoricAI.Moving aiMovingType() {

		return EnumPrehistoricAI.Moving.WALK;
	}

	@Override
	public EnumPrehistoricAI.WaterAbility aiWaterAbilityType() {

		return EnumPrehistoricAI.WaterAbility.NONE;
	}

	@Override
	public boolean doesFlock() {
		return false;
	}

	@Override
	public Item getOrderItem() {
		return Items.bone;
	}

	@Override
	public int getAdultAge() {
		return 8;
	}

	@Override
	public float getMaleSize() {
		return 1.2F;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();        
		if (this.getAnimation() == ATTACK_ANIMATION && this.getAnimationTick() == 15 && this.getAttackTarget() != null) {
			this.attackEntityAsMob(this.getAttackTarget());
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (this.getAttackBounds().intersectsWith(entity.boundingBox)) {
			if (this.getAnimation() == NO_ANIMATION) {
				this.setAnimation(ATTACK_ANIMATION);
				return false;
			}
			if (this.getAnimation() == ATTACK_ANIMATION && this.getAnimationTick() == 15) {
				IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.attackDamage);
				boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) iattributeinstance.getAttributeValue());
				if (entity.ridingEntity != null) {
					if (entity.ridingEntity == this) {
						entity.mountEntity(null);
					}
				}
				return flag;
			}
		}
		return false;
	}

	public int getMaxHunger() {
		return 75;
	}
	
	@Override
	protected String getLivingSound() {
		return "fossil:terror_bird_living";
	}

	@Override
	protected String getHurtSound() {
		return "fossil:terror_bird_hurt";
	}

	@Override
	protected String getDeathSound() {
		return "fossil:terror_bird_death";
	}

	@Override
	public float getSoundPitch(){
		return super.getSoundPitch() * 0.5F;
	}

	@Override
	public boolean canBeRidden() {
		return true;
	}
}
