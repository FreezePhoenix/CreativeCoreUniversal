package team.creative.creativecore.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Inventory;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.gui.ConfigGuiLayer;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.gui.IScaleableGuiScreen;
import team.creative.creativecore.common.gui.integration.ContainerIntegration;
import team.creative.creativecore.common.gui.integration.ContainerScreenIntegration;
import team.creative.creativecore.common.gui.integration.GuiEventHandler;
import team.creative.creativecore.common.gui.integration.GuiScreenIntegration;
import team.creative.creativecore.common.gui.style.GuiStyle;

public class CreativeCoreClient {
    
    private static final Minecraft mc = Minecraft.getInstance();
    
    public static void registerClientConfig(String modid) {
        // NOOP
    }
    
    public static float getDeltaFrameTime() {
        if (mc.isPaused())
            return 1.0F;
        return mc.getDeltaFrameTime();
    }
    
    public static void commands(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection dedicated) {
        dispatcher.register(Commands.literal("cmdclientconfig").executes((CommandContext<CommandSourceStack> x) -> {
            try {
                GuiEventHandler.queueScreen(new GuiScreenIntegration(new ConfigGuiLayer(CreativeConfigRegistry.ROOT, Side.CLIENT)));
            } catch(Error e) {
                e.printStackTrace();
            }
            return 0;
        }));
    }

    public static void init() {
        ClientTickEvent.CLIENT_PRE.register(GuiEventHandler::onTick);
        MenuRegistry.registerScreenFactory(CreativeCore.GUI_CONTAINER.get(), (ContainerIntegration container, Inventory inventory, Component p_create_3_) -> {
            return new ContainerScreenIntegration(container, inventory);
        });
        CommandRegistrationEvent.EVENT.register(CreativeCoreClient::commands);
        ClientTickEvent.CLIENT_PRE.register(CreativeCoreClient::clientTick);;
    }
    
    public static void clientTick(Minecraft client) {
        if (client.screen instanceof IScaleableGuiScreen scaleableGuiScreen)
            scaleableGuiScreen.clientTick();
    }
}
