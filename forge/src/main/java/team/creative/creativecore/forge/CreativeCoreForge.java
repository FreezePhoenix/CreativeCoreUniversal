package team.creative.creativecore.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.client.forge.CreativeCoreClientForge;
import team.creative.creativecore.common.gui.style.GuiStyle;

@Mod(value = CreativeCore.MODID)
public class CreativeCoreForge {
    public CreativeCoreForge() {
        EventBuses.registerModEventBus(CreativeCore.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.addListener(this::server);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::client));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(
                CreativeCoreClientForge::modelEvent));
        CreativeCore.init();
    }
    @OnlyIn(Dist.CLIENT)
    private void client(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(CreativeCoreClientForge.class);
        CreativeCoreClientForge.init(event);
        ModLoadingContext.get()
                .registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
    
    private void server(final ServerStartingEvent event) {
        CreativeCore.server(event.getServer());
    }
}
