package me.htrewrite.client.event.custom.networkmanager;

import me.htrewrite.client.event.custom.CustomEvent;
import net.minecraft.network.Packet;

public class NetworkPacketEvent extends CustomEvent {
    private Packet packet;

    public NetworkPacketEvent(Packet packet) {
        super();
        this.packet = packet;
    }

    public Packet getPacket() { return packet; }
}