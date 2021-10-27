package me.htrewrite.exeterimports.mcapi.settings;

public class IntegerSetting extends ValueSetting<Double> {
    public IntegerSetting(String label, String[] aliases, Double value, Double minimum, Double maximum) {
        super(label, aliases, value, minimum, maximum);
    }
    public IntegerSetting(String label, Double value, Double minimum, Double maximum) {
        super(label, value, minimum, maximum);
    }
    public IntegerSetting(String label, String[] aliases, Double value) {
        super(label, aliases, value);
    }

    @Override
    public void setValue(Number value) {
        super.setValue(value.intValue());
    }
}