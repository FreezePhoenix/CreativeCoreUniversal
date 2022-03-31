package team.creative.creativecore.common.gui.sync;

import java.util.function.Consumer;

import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.common.gui.GuiControl;
import team.creative.creativecore.common.gui.GuiLayer;
import team.creative.creativecore.common.gui.packet.ControlSyncPacket;
import team.creative.creativecore.common.gui.sync.GuiSyncHolder.GuiSyncHolderLayer;

public class GuiSyncLocal<T extends Tag> extends GuiSync<GuiLayer, T> {
    
    private final Consumer<T> consumer;
    
    GuiSyncLocal(GuiSyncHolder holder, String name, Consumer<T> consumer) {
        super(holder, name);
        this.consumer = consumer;
    }
    
    @Override
    public void receive(GuiLayer layer, T tag) {
        this.consumer.accept(tag);
    }
    
    public void send(T tag) {
        GuiControl control = ((GuiSyncHolderLayer) holder).parent;
        if (control.isClient())
            CreativeCore.NETWORK.sendToServer(new ControlSyncPacket(control, this, tag));
        else
            CreativeCore.NETWORK.sendToClient(new ControlSyncPacket(control, this, tag), (ServerPlayer) control.getPlayer());
    }
    
}
