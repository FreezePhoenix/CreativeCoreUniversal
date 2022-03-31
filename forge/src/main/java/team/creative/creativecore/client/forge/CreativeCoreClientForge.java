package team.creative.creativecore.client.forge;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.Side;
import team.creative.creativecore.client.CreativeCoreClient;
import team.creative.creativecore.client.render.model.CreativeModelLoader;
import team.creative.creativecore.client.render.model.CreativeRenderBlock;
import team.creative.creativecore.client.render.model.CreativeRenderItem;
import team.creative.creativecore.common.config.gui.ConfigGuiLayer;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.config.holder.ICreativeConfigHolder;
import team.creative.creativecore.common.gui.integration.GuiScreenIntegration;
import team.creative.creativecore.common.gui.style.GuiStyle;
import team.creative.creativecore.common.util.registry.FilteredHandlerRegistry;

public class CreativeCoreClientForge {

	public static final FilteredHandlerRegistry<Item, CreativeRenderItem> RENDERED_ITEMS = new FilteredHandlerRegistry<>(
			null);
	public static final FilteredHandlerRegistry<Block, CreativeRenderBlock> RENDERED_BLOCKS = new FilteredHandlerRegistry<>(
			null);
	private static final ItemColor ITEM_COLOR = (stack, tint) -> tint;
	private static final Minecraft mc = Minecraft.getInstance();

	public static void registerClientConfig(String modid) {
		ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class, () -> new ConfigGuiFactory((a, b) -> {
			ICreativeConfigHolder holder = CreativeConfigRegistry.ROOT.followPath(modid);
			if (holder != null && !holder.isEmpty(Side.CLIENT))
				return new GuiScreenIntegration(new ConfigGuiLayer(holder, Side.CLIENT));
			return null;
		}));
	}

	public static void registerBlocks(CreativeRenderBlock renderer, Block... blocks) {
		for (int i = 0; i < blocks.length; i++)
			RENDERED_BLOCKS.register(blocks[i], renderer);
	}

	public static void registerBlock(CreativeRenderBlock renderer, Block block) {
		RENDERED_BLOCKS.register(block, renderer);
	}

	public static void registerItem(CreativeRenderItem renderer, Item item) {
		RENDERED_ITEMS.register(item, renderer);
		mc.getItemColors().register(ITEM_COLOR, item);
	}

	public static float getDeltaFrameTime() {
		if (mc.isPaused())
			return 1.0F;
		return mc.getDeltaFrameTime();
	}

	public static void init(FMLClientSetupEvent event) {
		GuiStyle.reload();
		ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();

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
		CreativeCoreClient.init();
	}

	public static void modelEvent(ModelRegistryEvent event) {
		ModelLoaderRegistry.registerLoader(
				new ResourceLocation(CreativeCore.MODID, "rendered"),
				new CreativeModelLoader()
		);
	}
}
