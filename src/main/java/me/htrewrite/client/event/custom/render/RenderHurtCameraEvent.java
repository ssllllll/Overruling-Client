package me.htrewrite.client.event.custom.render;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderHurtCameraEvent extends Event {
    public float Ticks;

    public void EventRenderHurtCameraEffect(float p_Ticks)
    {
        Ticks = p_Ticks;
    }
}
