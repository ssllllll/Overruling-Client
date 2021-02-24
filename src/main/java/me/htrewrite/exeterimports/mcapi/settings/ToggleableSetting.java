package me.htrewrite.exeterimports.mcapi.settings;

import me.htrewrite.exeterimports.mcapi.interfaces.Toggleable;

import java.util.function.Predicate;

public class ToggleableSetting extends Setting implements Toggleable {
    private boolean enabled;

    public ToggleableSetting(String label, String[] aliases, boolean value) {
        super(label, aliases);
        this.enabled = value;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    @Override
    public void toggle() {
        setEnabled(!isEnabled());
    }

    private Predicate<Boolean> visibility;
    public ToggleableSetting setVisibility(Predicate<Boolean> visibility) { this.visibility = visibility; return this; }
    public boolean isVisible() {
        if(visibility == null)
            return true;
        return visibility.test(this.isEnabled());
    }
}