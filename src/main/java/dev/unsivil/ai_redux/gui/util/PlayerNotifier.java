package dev.unsivil.ai_redux.gui.util;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.NotificationUtil;


public class PlayerNotifier {
    private final PacketHandler handler;
    private String iconId = "Deco_Kweebec_Plush";
    
    public PlayerNotifier(PlayerRef ref) {
        this.handler = ref.getPacketHandler();
    }
    
    public PlayerNotifier setIconID(String id) {
        this.iconId = id;
        return this;
    }
    
    public PlayerNotifier sendNotification(Message primary, Message secondary) {
        NotificationUtil.sendNotification(handler,
            primary, secondary);
        return this;
    }
    
    public PlayerNotifier sendIconNotification(Message primary, Message secondary) {
        NotificationUtil.sendNotification(handler,
            primary, secondary,
            new ItemStack(iconId, 1).toPacket());
        return this;
    }
}
