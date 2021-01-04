package me.overruling.client.event.custom.networkmanager;

import me.overruling.client.event.custom.CustomEvent;
import net.minecraft.network.Packet;

public class NetworkPacketEvent extends CustomEvent {
    private Packet packet;

    public NetworkPacketEvent(Packet packet) {
        super();
        this.packet = packet;
    }

    public Packet getPacket() { return packet; }
}