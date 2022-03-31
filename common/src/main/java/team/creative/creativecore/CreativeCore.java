package team.creative.creativecore;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.platform.Platform;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
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
import java.util.function.Supplier;

public class CreativeCore {
	public static final String MODID = "creativecore";
	public static final Logger LOGGER = LogManager.getLogger(CreativeCore.MODID);
	public static final CreativeNetwork NETWORK = new CreativeNetwork(
			"1.0",
			LOGGER,
			new ResourceLocation(CreativeCore.MODID, "main")
	);
	public static final CreativeCoreConfig CONFIG = new CreativeCoreConfig();
	public static final ResourceLocation FAKE_WORLD_LOCATION = new ResourceLocation(MODID, "fake");
	public static ResourceKey<Level> FAKE_DIMENSION_NAME = ResourceKey.create(
			Registry.DIMENSION_REGISTRY,
			FAKE_WORLD_LOCATION
	);
	public static final GuiCreatorBasic CONFIG_OPEN = GuiCreator
			.register("config", new GuiCreatorBasic((player, nbt) -> new ConfigGuiLayer(CreativeConfigRegistry.ROOT, Side.SERVER)));
	public static final GuiCreatorBasic CLIENT_CONFIG_OPEN = GuiCreator
			.register("clientconfig", new GuiCreatorBasic((player, nbt) -> new ClientSyncGuiLayer(CreativeConfigRegistry.ROOT)));
	public static ConfigEventHandler CONFIG_HANDLER;
	public static DimensionType FAKE_DIMENSION;

	public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MODID));
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(MODID, Registry.MENU_REGISTRY);
	public static RegistrySupplier<MenuType<ContainerIntegration>> GUI_CONTAINER = MENUS.register(new ResourceLocation(
			MODID, "container"), () -> {
		return MenuRegistry.of((int windowId, Inventory playerInv) -> {
			return new ContainerIntegration(windowId, playerInv.player);
		});
	});

	public static void server(MinecraftServer server) {
		server.getCommands()
		      .getDispatcher()
		      .register(Commands.literal("cmdconfig").executes((CommandContext<CommandSourceStack> x) -> {
				  try {
					  CONFIG_OPEN.open(new CompoundTag(), x.getSource().getPlayerOrException());
				  } catch(Exception e) {
					  e.printStackTrace();
				  } catch(Error e) {
					  e.printStackTrace();
				  }
			      return 0;
		      }));
	}

	public static void init() {
		MENUS.register();
		NETWORK.registerType(ConfigurationChangePacket.class, ConfigurationChangePacket::new);
		NETWORK.registerType(ConfigurationClientPacket.class, ConfigurationClientPacket::new);
		NETWORK.registerType(ConfigurationPacket.class, ConfigurationPacket::new);
		NETWORK.registerType(LayerClosePacket.class, LayerClosePacket::new);
		NETWORK.registerType(LayerOpenPacket.class, LayerOpenPacket::new);
		NETWORK.registerType(OpenGuiPacket.class, OpenGuiPacket::new);
		NETWORK.registerType(ControlSyncPacket.class, ControlSyncPacket::new);
		CONFIG_HANDLER = new ConfigEventHandler(Platform.getConfigFolder().toFile(), LOGGER);
		FAKE_DIMENSION = DimensionType.create(
				OptionalLong
						.empty(),
				true,
				false,
				false,
				false,
				1,
				false,
				true,
				true,
				false,
				false,
				-64,
				384,
				384,
				BlockTags.INFINIBURN_OVERWORLD,
				DimensionType.OVERWORLD_EFFECTS,
				0.0F
		);
		ArgumentTypes.register(
				"names",
				StringArrayArgumentType.class,
				new EmptyArgumentSerializer<>(() -> StringArrayArgumentType.stringArray())
		);
	}
}
