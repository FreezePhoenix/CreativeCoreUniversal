package team.creative.creativecore.common.util.math.matrix;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;
import team.creative.creativecore.common.util.math.base.Axis;
import team.creative.creativecore.common.util.math.box.BoxCorner;
import team.creative.creativecore.common.util.math.box.OBB;
import team.creative.creativecore.common.util.math.vec.Vec3d;

public interface IVecOrigin {
    
    double offX();
    
    double offY();
    
    double offZ();
    
    double rotX();
    
    double rotY();
    
    double rotZ();
    
    double offXLast();
    
    double offYLast();
    
    double offZLast();
    
    double rotXLast();
    
    double rotYLast();
    
    double rotZLast();
    
    boolean isRotated();
    
    void offX(double value);
    
    void offY(double value);
    
    void offZ(double value);
    
    void off(double x, double y, double z);
    
    void rotX(double value);
    
    void rotY(double value);
    
    void rotZ(double value);
    
    void rot(double x, double y, double z);
    
    Vec3d center();
    
    void setCenter(Vec3d vec);
    
    Matrix3 rotation();
    
    Matrix3 rotationInv();
    
    Vec3d translation();
    
    void tick();
    
    IVecOrigin getParent();
    
    default double translationCombined(Axis axis) {
        return translation().get(axis);
    }
    
    default void onlyRotateWithoutCenter(Vec3d vec) {
        rotation().transform(vec);
    }
    
    default BlockPos transformPointToWorld(BlockPos pos) {
        Vec3d vec = new Vec3d(pos);
        transformPointToWorld(vec);
        return vec.toBlockPos();
    }
    
    default BlockPos transformPointToFakeWorld(BlockPos pos) {
        Vec3d vec = new Vec3d(pos);
        transformPointToFakeWorld(vec);
        return vec.toBlockPos();
    }
    
    default void transformPointToWorld(Vec3d vec) {
        vec.sub(center());
        rotation().transform(vec);
        vec.add(center());
        
        vec.add(translation());
    }
    
    default void transformPointToFakeWorld(Vec3d vec) {
        vec.sub(translation());
        
        vec.sub(center());
        rotationInv().transform(vec);
        vec.add(center());
    }
    
    default Vector3d transformPointToWorld(Vector3d vec) {
        Vec3d real = new Vec3d(vec);
        transformPointToWorld(real);
        return new Vector3d(real.x, real.y, real.z);
    }
    
    default Vector3d transformPointToFakeWorld(Vector3d vec) {
        Vec3d real = new Vec3d(vec);
        transformPointToFakeWorld(real);
        return new Vector3d(real.x, real.y, real.z);
    }
    
    default Vec3 transformPointToWorld(Vec3 vec) {
        Vec3d real = new Vec3d(vec);
        transformPointToWorld(real);
        return new Vec3(real.x, real.y, real.z);
    }
    
    default Vec3 transformPointToFakeWorld(Vec3 vec) {
        Vec3d real = new Vec3d(vec);
        transformPointToFakeWorld(real);
        return new Vec3(real.x, real.y, real.z);
    }
    
    default AABB getAxisAlignedBox(AABB box) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;
        
        for (int i = 0; i < BoxCorner.values().length; i++) {
            Vec3d vec = BoxCorner.values()[i].get(box);
            
            transformPointToWorld(vec);
            
            minX = Math.min(minX, vec.x);
            minY = Math.min(minY, vec.y);
            minZ = Math.min(minZ, vec.z);
            maxX = Math.max(maxX, vec.x);
            maxY = Math.max(maxY, vec.y);
            maxZ = Math.max(maxZ, vec.z);
        }
        
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    default OBB getOrientatedBox(AABB box) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;
        
        for (int i = 0; i < BoxCorner.values().length; i++) {
            Vec3d vec = BoxCorner.values()[i].get(box);
            
            transformPointToFakeWorld(vec);
            
            minX = Math.min(minX, vec.x);
            minY = Math.min(minY, vec.y);
            minZ = Math.min(minZ, vec.z);
            maxX = Math.max(maxX, vec.x);
            maxY = Math.max(maxY, vec.y);
            maxZ = Math.max(maxZ, vec.z);
        }
        
        return new OBB(this, minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    @Environment(EnvType.CLIENT)
    default void setupRenderingInternal(PoseStack matrixStack, Entity entity, float partialTicks) {
        double rotX = rotXLast() + (rotX() - rotXLast()) * partialTicks;
        double rotY = rotYLast() + (rotY() - rotYLast()) * partialTicks;
        double rotZ = rotZLast() + (rotZ() - rotZLast()) * partialTicks;
        
        double offX = offXLast() + (offX() - offXLast()) * partialTicks;
        double offY = offYLast() + (offY() - offYLast()) * partialTicks;
        double offZ = offZLast() + (offZ() - offZLast()) * partialTicks;
        
        Vec3d rotationCenter = center();
        
        matrixStack.translate(offX, offY, offZ);
        
        matrixStack.translate(rotationCenter.x, rotationCenter.y, rotationCenter.z);
        
        // TODO USE PROPER MATRIX ROTATIOn
        GL11.glRotated(rotX, 1, 0, 0);
        GL11.glRotated(rotY, 0, 1, 0);
        GL11.glRotated(rotZ, 0, 0, 1);
        
        matrixStack.translate(-rotationCenter.x, -rotationCenter.y, -rotationCenter.z);
    }
    
    @Environment(EnvType.CLIENT)
    default void setupRendering(PoseStack matrixStack, Entity entity, float partialTicks) {
        setupRenderingInternal(matrixStack, entity, partialTicks);
    }
    
    default boolean hasChanged() {
        return offXLast() != offX() || offYLast() != offY() || offZLast() != offZ() || rotXLast() != rotX() || rotYLast() != rotY() || rotZLast() != rotZ();
    }
    
}
