package iDiamondhunter.morebows.entities;

import static iDiamondhunter.morebows.MoreBows.ARROW_TYPE_FROST;
import static iDiamondhunter.morebows.MoreBows.ARROW_TYPE_NOT_CUSTOM;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/** This entity is a custom arrow. A large portion of logic around these arrows is handled in the MoreBows class with SubscribeEvents. */
public final class CustomArrow extends EntityArrow implements IEntityAdditionalSpawnData {

    /** Whether this arrow is actually critical or not. */
    public boolean crit = false;
    /** How many ticks this arrow has been in the ground for. -1 is used to indicate that the arrow has not yet hit the ground. */
    private byte inTicks = -1;
    /** The type of this arrow. In an ideal world, this would be final, but this is not an ideal world. See readSpawnData. */
    public byte type = ARROW_TYPE_NOT_CUSTOM;

    /**
     * Don't use this.
     * TODO I think I can't remove these constructors, but I'm not sure.
     *
     * @param a used in super construction
     */
    public CustomArrow(World a) {
        super(a);
    }

    /**
     * Don't use this.
     * TODO I think I can't remove these constructors, but I'm not sure.
     *
     * @param a used in super construction
     * @param b used in super construction
     * @param c used in super construction
     * @param d used in super construction
     */
    public CustomArrow(World a, double b, double c, double d) {
        super(a, b, c, d);
    }

    /**
     * Don't use this.
     * TODO I think I can't remove these constructors, but I'm not sure.
     *
     * @param a used in super construction
     * @param b used in super construction
     * @param c used in super construction
     * @param d used in super construction
     * @param e used in super construction
     */
    public CustomArrow(World a, EntityLivingBase b, EntityLivingBase c, float d, float e) {
        super(a, b, c, d, e);
    }

    /**
     * Don't use this.
     * TODO I think I can't remove these constructors, but I'm not sure.
     *
     * @param a used in super construction
     * @param b used in super construction
     * @param c used in super construction
     */
    public CustomArrow(World a, EntityLivingBase b, float c) {
        super(a, b, c);
    }

    /**
     * A constructor that gives the CustomArrow an ArrowType.
     *
     * @param a    used in super construction
     * @param b    used in super construction
     * @param c    used in super construction
     * @param type the type of arrow
     */
    public CustomArrow(World a, EntityLivingBase b, float c, byte type) {
        super(a, b, c);
        this.type = type;
        /**
         * TODO Possibly implement this
         *
         * <pre>
         * if (type == ARROW_TYPE_FROST) { // I'm not sure it makes sense for a frost arrow to be on fire, but I don't think people care about it that much, and the frost bow is a bit under powered as is...
         *     this.extinguish();
         * }
         * </pre>
         */
    }

    /** This may not accurately return whether an arrow is critical or not. This is to hide crit particle trails, when a custom arrow has a custom particle trail. */
    @Override
    public boolean getIsCritical() {
        return type == ARROW_TYPE_FROST ? false : super.getIsCritical();
        /**
         * Obviously, you're just a bad shot :D
         * This is an awful hack to prevent the vanilla crit particles from displaying for frost arrows.
         * The vanilla code to display the arrow particle trail is buried deep inside onUpdate,
         * and the only other options I have are to:
         * - intercept the particles with packets,
         * - intercept the particles with events (not feasible from what I can tell),
         * - ASM it out,
         * - or perform some ridiculous wrapping around the World to intercept the method to spawn particles.
         * Instead of doing that, I just prevent anything from ever knowing that it's crited,
         * and instead I wrap around the event when the arrow attacks something. See onLivingAttackEvent() for the details,
         * but the TLDR is that I cancel the attack and start a new one with the crit taken into account.
         * This allows the entity to take the crit into account when deciding if it's damaged or not.
         * This is probably the lesser of these evils.
         */
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (type == ARROW_TYPE_FROST) {
            /**
             * Hack to determine when the arrow has hit the ground. inGround is a private field.
             * Access transformers can be used for this, but they're are annoying to deal with and they aren't always safe.
             * However, instead we can take advantage of the fact that arrowShake is always set to 7 after an arrow has hit the ground.
             * inGround is used to store this information.
             */
            if (arrowShake == 7) {
                inTicks = 0;
                canBePickedUp = 0;

                /* Shrinks the size of the frost arrow if it's in the ground and the mod is in old rendering mode */
                if (worldObj.isRemote && iDiamondhunter.morebows.MoreBows.oldFrostArrowRendering) {
                    setSize(0.1F, 0.1F);
                }
            }

            if (inTicks > -1) {
                inTicks++;

                if (inTicks <= 2) {
                    worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
                }

                /**
                 * Behavior of older versions of More Bows
                 * TODO Possibly implement this
                 *
                 * <pre>
                 * if (Block.isEqualTo(test, Blocks.water)) {
                 *     this.worldObj.setBlockMetadataWithNotify(tempThingX, tempThingY, tempThingZ, Block.getIdFromBlock(Blocks.ice), 3);
                 * }
                 * </pre>
                 */
                if (inTicks <= 30) {
                    worldObj.spawnParticle("splash", posX, posY - 0.3D, posZ, 0.0D, 0.0D, 0.0D);
                }

                /** Responsible for adding snow layers on top the block the arrow hits, or "freezing" the water it's in by setting the block to ice. */
                if (inTicks == 64) {
                    /*
                     * TODO Verify that this is the right block!
                     * Also, why does this sometimes set multiple blocks? It's the correct behavior of the original mod, but it's concerning...
                     */
                    final int floorPosX = MathHelper.floor_double(posX);
                    final int floorPosY = MathHelper.floor_double(posY);
                    final int floorPosZ = MathHelper.floor_double(posZ);
                    final Block inBlock = worldObj.getBlock(floorPosX, floorPosY, floorPosZ);

                    /*
                     * Possibly unused code?
                     * if (Block.isEqualTo(this.field_145790_g, Blocks.snow)) {
                     */

                    /** TODO Possibly implement incrementing snow layers. */
                    if (inBlock == Blocks.air) {
                        worldObj.setBlock(floorPosX, floorPosY, floorPosZ, Blocks.snow_layer);
                    }

                    if (inBlock == Blocks.water) {
                        /*
                         * TODO Check if the earlier event or this one is the correct one.
                         * Also: bouncy arrow on ice, a bit like stone skimming? Could be cool.
                         */
                        worldObj.setBlock(floorPosX, floorPosY, floorPosZ, Blocks.ice);
                    }
                }

                if (inTicks >= 64) {
                    setDead();
                }
            } else if (crit) {
                for (int i = 0; i < 4; ++i) {
                    worldObj.spawnParticle("splash", posX + ((motionX * i) / 4.0D), posY + ((motionY * i) / 4.0D), posZ + ((motionZ * i) / 4.0D), -motionX, -motionY + 0.2D, -motionZ);
                }
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        inTicks = tag.getByte("inTicks");
        crit = tag.getBoolean("crit");
        type = tag.getByte("type");
    }

    public void readSpawnData(ByteBuf data) {
        crit = data.readBoolean();
        inTicks = data.readByte();

        /* Shrinks the size of the frost arrow if it's in the ground and the mod is in old rendering mode */
        if ((inTicks != -1) && worldObj.isRemote && iDiamondhunter.morebows.MoreBows.oldFrostArrowRendering) {
            setSize(0.1F, 0.1F);
        }

        type = data.readByte();
        /** See NetHandlerPlayClient.handleSpawnObject (line 414). */
        final Entity shooter = worldObj.getEntityByID(data.readInt());

        if (shooter instanceof EntityLivingBase) {
            shootingEntity = shooter;
        }
    }

    @Override
    public void setIsCritical(boolean crit) {
        super.setIsCritical(this.crit = crit);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setByte("inTicks", inTicks);
        tag.setBoolean("crit", crit);
        tag.setByte("type", type);
    }

    public void writeSpawnData(ByteBuf data) {
        data.writeBoolean(crit);
        data.writeByte(inTicks);
        data.writeByte(type);
        data.writeInt(shootingEntity != null ? shootingEntity.getEntityId() : -1);
    }

}
