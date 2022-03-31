package team.creative.creativecore.client.fabric;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
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
import team.creative.creativecore.client.CreativeCoreClient;
import team.creative.creativecore.common.config.gui.ConfigGuiLayer;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.gui.IScaleableGuiScreen;
import team.creative.creativecore.common.gui.integration.ContainerIntegration;
import team.creative.creativecore.common.gui.integration.ContainerScreenIntegration;
import team.creative.creativecore.common.gui.integration.GuiEventHandler;
import team.creative.creativecore.common.gui.integration.GuiScreenIntegration;
import team.creative.creativecore.common.gui.style.GuiStyle;

public class CreativeCoreClientFabric implements ClientModInitializer {
    private static final Minecraft mc = Minecraft.getInstance();
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvent.CLIENT_STARTED.register(minecraft -> {
            GuiStyle.reload();
            ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) minecraft.getResourceManager();

            reloadableResourceManager.registerReloadListener(new SimplePreparableReloadListener() {
                @Override
                protected Object prepare(ResourceManager p_10796_, ProfilerFiller p_10797_) {
                    return GuiStyle.class; // No idea
                }

                @Override
                protected void apply(Object p_10793_, ResourceManager p_10794_, ProfilerFiller p_10795_) {
                    GuiStyle.reload();
                }
            });
        });
        CreativeCoreClient.init();
    }
}
