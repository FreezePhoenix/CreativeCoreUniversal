package team.creative.creativecore;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.LevelAccessor;
import team.creative.creativecore.client.ClientLoader;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeLoader {
	@ExpectPlatform
	public static void registerDisplayTest(Supplier<String> suppliedVersion, BiPredicate<String, Boolean> remoteVersionTest) {
	}

	@ExpectPlatform
	public static String ignoreServerNetworkConstant() {
		return "";
	}
	@ExpectPlatform
	public static void registerClient(ClientLoader loader) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerClientTick(Runnable run) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerClientRender(Runnable run) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerLoadLevel(Consumer<LevelAccessor> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T> void registerListener(Consumer<T> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerClientStarted(Runnable run) {
		throw new AssertionError();
	}

}
