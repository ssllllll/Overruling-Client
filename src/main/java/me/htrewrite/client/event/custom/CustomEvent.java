package me.htrewrite.client.event.custom;

import me.htrewrite.client.Wrapper;
import me.zero.alpine.fork.event.type.Cancellable;

public class CustomEvent extends Cancellable {
    private final float partialTicks;
    private Era era;

    public CustomEvent(float partialTicks) {
        this.partialTicks = partialTicks;
        era = Era.PRE;
    }
    public CustomEvent() { this(Wrapper.getMC().getRenderPartialTicks()); }

    public Era getEra() { return era; }
    public void setEra(Era era) { this.era = era; }
    public float getPartialTicks() { return partialTicks; }

    public enum Era {
        PRE, POST
    }
}