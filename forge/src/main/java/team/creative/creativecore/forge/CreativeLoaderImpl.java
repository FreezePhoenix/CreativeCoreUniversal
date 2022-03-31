package team.creative.creativecore.forge;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import team.creative.creativecore.client.ClientLoader;

public class CreativeLoaderImpl {
    public static void registerDisplayTest(Supplier<String> suppliedVersion, BiPredicate<String, Boolean> remoteVersionTest) {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(suppliedVersion, remoteVersionTest));
    }
    public static String ignoreServerNetworkConstant() {
        return NetworkConstants.IGNORESERVERONLY;
    }
    public static void registerClient(ClientLoader loader) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent x) -> loader.onInitializeClient());
        MinecraftForge.EVENT_BUS.addListener((RegisterClientCommandsEvent x) -> loader.registerClientCommands(x.getDispatcher()));
    }
    public static void registerClientTick(Runnable run) {
        MinecraftForge.EVENT_BUS.addListener((ClientTickEvent x) -> {
            if (x.phase == Phase.START)
                run.run();
        });
    }
    public static void registerClientRender(Runnable run) {
        MinecraftForge.EVENT_BUS.addListener((RenderTickEvent x) -> {
            if (x.phase == Phase.END)
                run.run();
        });
    }
    public static void registerLoadLevel(Consumer<LevelAccessor> consumer) {
        MinecraftForge.EVENT_BUS.addListener((WorldEvent.Load x) -> consumer.accept(x.getWorld()));
    }
    public static void registerListener(Consumer consumer) {
        MinecraftForge.EVENT_BUS.addListener(consumer);
    }

    public static void registerClientStarted(Runnable run) {
        run.run();
    }
    
}
