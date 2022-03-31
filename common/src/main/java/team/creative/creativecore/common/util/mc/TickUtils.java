package team.creative.creativecore.common.util.mc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.Level;
import team.creative.creativecore.client.CreativeCoreClient;

public class TickUtils {
    @Environment(EnvType.CLIENT)
    private static float getDeltaFrameTimeClient() {
        return CreativeCoreClient.getDeltaFrameTime();
    }
    
    public static float getDeltaFrameTime(Level level) {
        if (level.isClientSide)
            return getDeltaFrameTimeClient();
        return 1.0F;
    }
    
}
