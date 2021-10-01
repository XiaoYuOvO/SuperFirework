package net.xiaoyu233.superfirework.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import net.xiaoyu233.superfirework.entity.SuperFireworkEntity;

import java.util.UUID;
import java.util.function.Supplier;

public class SPacketSuperFireworkSpawn {
    private int entityId;
    private UUID uniqueId;
    private double x;
    private double y;
    private double z;
    private int speedX;
    private int speedY;
    private int speedZ;
    private int pitch;
    private int yaw;
    private EntityType<?> type;
    private int data;
    private ItemStack fireworkItem;

    public SPacketSuperFireworkSpawn() {
    }

    public SPacketSuperFireworkSpawn(int entityId, UUID uuid, double xPos, double yPos, double zPos, float pitch, float yaw, EntityType<?> entityType, int entityData, Vector3d speedVector,ItemStack fireworkItem) {
        this.entityId = entityId;
        this.uniqueId = uuid;
        this.x = xPos;
        this.y = yPos;
        this.z = zPos;
        this.pitch = MathHelper.floor(pitch * 256.0F / 360.0F);
        this.yaw = MathHelper.floor(yaw * 256.0F / 360.0F);
        this.type = entityType;
        this.data = entityData;
        this.speedX = (int)(MathHelper.clamp(speedVector.x, -3.9D, 3.9D) * 8000.0D);
        this.speedY = (int)(MathHelper.clamp(speedVector.y, -3.9D, 3.9D) * 8000.0D);
        this.speedZ = (int)(MathHelper.clamp(speedVector.z, -3.9D, 3.9D) * 8000.0D);
        this.fireworkItem = fireworkItem;
    }

    public SPacketSuperFireworkSpawn(SuperFireworkEntity entity) {
        this(entity, 0);
    }

    public SPacketSuperFireworkSpawn(SuperFireworkEntity entityIn, int typeIn) {
        this(entityIn.getEntityId(), entityIn.getUniqueID(), entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ(), entityIn.rotationPitch, entityIn.rotationYaw, entityIn.getType(), typeIn, entityIn.getMotion(),entityIn.getFireworkItem());
    }

    public SPacketSuperFireworkSpawn(PacketBuffer packetBuffer) {
        this.readPacketData(packetBuffer);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) {
        this.entityId = buf.readVarInt();
        this.uniqueId = buf.readUniqueId();
        this.type = Registry.ENTITY_TYPE.getByValue(buf.readVarInt());
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.pitch = buf.readByte();
        this.yaw = buf.readByte();
        this.data = buf.readInt();
        this.speedX = buf.readShort();
        this.speedY = buf.readShort();
        this.speedZ = buf.readShort();
        this.fireworkItem = buf.readItemStack();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) {
        buf.writeVarInt(this.entityId);
        buf.writeUniqueId(this.uniqueId);
        buf.writeVarInt(Registry.ENTITY_TYPE.getId(this.type));
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeByte(this.pitch);
        buf.writeByte(this.yaw);
        buf.writeInt(this.data);
        buf.writeShort(this.speedX);
        buf.writeShort(this.speedY);
        buf.writeShort(this.speedZ);
        buf.writeItemStack(fireworkItem);
    }

    @OnlyIn(Dist.CLIENT)
    public int getEntityID() {
        return this.entityId;
    }

    @OnlyIn(Dist.CLIENT)
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @OnlyIn(Dist.CLIENT)
    public double getX() {
        return this.x;
    }

    @OnlyIn(Dist.CLIENT)
    public double getY() {
        return this.y;
    }

    @OnlyIn(Dist.CLIENT)
    public double getZ() {
        return this.z;
    }

    @OnlyIn(Dist.CLIENT)
    public double func_218693_g() {
        return (double)this.speedX / 8000.0D;
    }

    @OnlyIn(Dist.CLIENT)
    public double func_218695_h() {
        return (double)this.speedY / 8000.0D;
    }

    @OnlyIn(Dist.CLIENT)
    public double func_218692_i() {
        return (double)this.speedZ / 8000.0D;
    }

    @OnlyIn(Dist.CLIENT)
    public int getPitch() {
        return this.pitch;
    }

    @OnlyIn(Dist.CLIENT)
    public int getYaw() {
        return this.yaw;
    }

    @OnlyIn(Dist.CLIENT)
    public EntityType<?> getType() {
        return this.type;
    }

    @OnlyIn(Dist.CLIENT)
    public int getData() {
        return this.data;
    }

    public static class Handler {

        public static boolean onMessage(SPacketSuperFireworkSpawn message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(new Runnable() {
                @Override
                public void run() {
                    double d0 = message.getX();
                    double d1 = message.getY();
                    double d2 = message.getZ();
                    ClientWorld world = Minecraft.getInstance().world;
                    SuperFireworkEntity entity = new SuperFireworkEntity(world, d0, d1, d2, ItemStack.EMPTY);
                    int i = message.getEntityID();
                    entity.setPacketCoordinates(d0, d1, d2);
                    entity.moveForced(d0, d1, d2);
                    entity.rotationPitch = (float)(message.getPitch() * 360) / 256.0F;
                    entity.rotationYaw = (float)(message.getYaw() * 360) / 256.0F;
                    entity.setEntityId(i);
                    entity.setItem(message.fireworkItem);
                    entity.setUniqueId(message.getUniqueId());
                    world.addEntity(i,entity);

//					chunkAt.getBiomeArray()[(message.pos.getZ() & 15) << 4 | (message.pos.getX() & 15)] = message.biomeId;

//					world.markBlockRangeForRenderUpdate(message.pos, message.pos.up(255));
                }
            });

            return true;
        }
    }
}
