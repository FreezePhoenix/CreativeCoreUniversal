package team.creative.creativecore.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.Mixin;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VertexFormat.class)
public interface VertexFormatAccessor {
    @Accessor
    IntList getOffsets();
}
