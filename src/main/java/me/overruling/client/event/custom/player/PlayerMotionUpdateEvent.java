package me.overruling.client.event.custom.player;

import me.overruling.client.event.custom.CustomEvent;

public class PlayerMotionUpdateEvent extends CustomEvent {
    public PlayerMotionUpdateEvent(Era era) { super(); setEra(era); }
}