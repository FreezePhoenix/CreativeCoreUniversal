package team.creative.creativecore.client.render.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IRenderChunkSupplier {
    
    public Object getRenderChunk(Level level, BlockPos pos);
    
}
