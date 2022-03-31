package team.creative.creativecore.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.ByteBuffer;

@Mixin(BufferBuilder.class)
public interface BufferBuilderAccessor {
    @Accessor
    ByteBuffer getBuffer();
    
    @Accessor
    void setBuffer(ByteBuffer b);
    
    @Accessor
    int getNextElementByte();
    
    @Accessor
    int getVertices();
    
    @Accessor
    void setVertices(int i);
    
    @Accessor
    VertexFormat getFormat();
    
    @Invoker
    void invokeEnsureCapacity(int i);
}
