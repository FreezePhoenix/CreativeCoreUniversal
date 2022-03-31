package team.creative.creativecore.common.network;

import dev.architectury.networking.NetworkChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.function.Supplier;

public class CreativeNetwork {
	public final ResourceLocation CHANNEL;
	private final HashMap<Class<? extends CreativePacket>, CreativeNetworkPacket> packetTypes = new HashMap<>();
	private final HashMap<Class<? extends CreativePacket>, ResourceLocation> packetTypeChannels = new HashMap<>();
	private final Logger logger;

	private final NetworkChannel instance;
	private int id = 0;

	public CreativeNetwork(String version, Logger logger, ResourceLocation location) {
		this.logger = logger;
		this.CHANNEL = location;
		this.logger.debug("Created network " + location + "");
		instance = NetworkChannel.create(location);
	}

	@Environment(EnvType.CLIENT)
	private static Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	public <T extends CreativePacket> void registerType(Class<T> classType, Supplier<T> supplier) {
		CreativeNetworkPacket<T> handler = new CreativeNetworkPacket<>(classType, supplier);
		instance.register(classType, (message, buffer) -> {
			handler.write(message, buffer);
		}, (buffer) -> {
			return handler.read(buffer);
		}, (message, ctx) -> {
			ctx.get().queue(() -> message.execute(ctx.get().getPlayer() == null
			                                ? getClientPlayer()
			                                : ctx.get().getPlayer()));
		});
		packetTypes.put(classType, handler);
		id++;
	}

	public CreativeNetworkPacket getPacketType(Class<? extends CreativePacket> clazz) {
		return packetTypes.get(clazz);
	}

	public void sendToServer(CreativePacket message) {
		instance.sendToServer(message);
	}

	public void sendToClient(CreativePacket message, ServerPlayer player) {
		instance.sendToPlayer(player, message);
	}

	public void sendToClientAll(MinecraftServer server, CreativePacket message) {
		instance.sendToPlayers(server.getPlayerList().getPlayers(), message);
	}
}
