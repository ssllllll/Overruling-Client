package me.htrewrite.exeterimports.mcapi.settings;

import java.util.function.Predicate;

public class StringSetting extends Setting {
    private String value;

    public StringSetting(String label, String[] aliases, String value) {
        super(label, aliases);
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value.replaceAll("\"", "");
    }

    public String getValue() {
        return value;
    }

    private Predicate<String> visibility;
    public StringSetting setVisibility(Predicate<String> visibility) { this.visibility = visibility; return this; }
    public boolean isVisible() {
        if(visibility == null)
            return true;
        return visibility.test(this.getValue());
    }
}