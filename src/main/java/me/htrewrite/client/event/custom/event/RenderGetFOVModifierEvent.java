package me.htrewrite.client.event.custom.event;

import me.htrewrite.client.event.custom.CustomEvent;

public class RenderGetFOVModifierEvent extends CustomEvent {
    public float partialTicks;
    public boolean useFOVSetting;
    private float FOV;

    public RenderGetFOVModifierEvent(float partialTicks, boolean useFOVSetting) {
        super();
        this.partialTicks = partialTicks;
        this.useFOVSetting = useFOVSetting;
    }

    public void setFOV(float FOV) { this.FOV = FOV; }
    public float getFOV() { return FOV; }
}