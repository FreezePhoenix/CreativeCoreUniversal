package team.creative.creativecore.fabric;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.creative.creativecore.*;
import team.creative.creativecore.common.config.event.ConfigEventHandler;
import team.creative.creativecore.common.config.gui.ClientSyncGuiLayer;
import team.creative.creativecore.common.config.gui.ConfigGuiLayer;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.config.sync.ConfigurationChangePacket;
import team.creative.creativecore.common.config.sync.ConfigurationClientPacket;
import team.creative.creativecore.common.config.sync.ConfigurationPacket;
import team.creative.creativecore.common.gui.handler.GuiCreator;
import team.creative.creativecore.common.gui.handler.GuiCreator.GuiCreatorBasic;
import team.creative.creativecore.common.gui.integration.ContainerIntegration;
import team.creative.creativecore.common.gui.packet.ControlSyncPacket;
import team.creative.creativecore.common.gui.packet.LayerClosePacket;
import team.creative.creativecore.common.gui.packet.LayerOpenPacket;
import team.creative.creativecore.common.gui.packet.OpenGuiPacket;
import team.creative.creativecore.common.network.CreativeNetwork;
import team.creative.creativecore.common.util.argument.StringArrayArgumentType;

import java.util.OptionalLong;

public class CreativeCoreFabric implements ModInitializer {
    public CreativeCoreFabric() {
        ServerLifecycleEvents.SERVER_STARTING.register(this::server);
    }

    private void server(MinecraftServer server) {
        CreativeCore.server(server);
    }

    @Override
    public void onInitialize() {
        CreativeCore.init();
    }
}
