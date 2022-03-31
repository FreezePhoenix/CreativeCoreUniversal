package team.creative.creativecore.fabric;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.level.LevelAccessor;
import team.creative.creativecore.client.ClientLoader;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeLoaderImpl {
	public static void registerDisplayTest(Supplier<String> suppliedVersion, BiPredicate<String, Boolean> remoteVersionTest) {}

	public static String ignoreServerNetworkConstant() {
		return "";
	}

	public static void registerClient(ClientLoader loader) {
		CommandRegistrationCallback.EVENT.register((dispatcher, server) -> {
			if (!server)
				loader.registerClientCommands(dispatcher);
		});
	}

	public static void registerClientTick(Runnable run) {
		ClientTickEvents.END_CLIENT_TICK.register(x -> run.run());
	}

	public static void registerClientRender(Runnable run) {
		HudRenderCallback.EVENT.register((matrix, partialTicks) -> run.run());
	}

	public static void registerLoadLevel(Consumer<LevelAccessor> consumer) {
		ServerWorldEvents.LOAD.register((server, level) -> consumer.accept(level));
	}

	public static <T> void registerListener(Consumer<T> consumer) {}

	public static void registerClientStarted(Runnable run) {
		ClientLifecycleEvents.CLIENT_STARTED.register(x -> run.run());
	}
}
