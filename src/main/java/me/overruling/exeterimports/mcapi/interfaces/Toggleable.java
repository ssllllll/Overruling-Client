package me.overruling.exeterimports.mcapi.interfaces;

public interface Toggleable {
    boolean isEnabled();

    void setEnabled(boolean flag);

    void toggle();
}