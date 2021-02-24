package me.htrewrite.client.module;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.event.custom.module.ModuleToggleEvent;
import me.htrewrite.client.util.ConfigUtils;
import me.htrewrite.exeterimports.mcapi.interfaces.Labeled;
import me.htrewrite.exeterimports.mcapi.settings.*;
import me.zero.alpine.fork.listener.Listenable;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class Module implements Listenable, Labeled {
    private final String name;
    private final String description;
    private final ModuleType category;
    private final ArrayList<Setting> options;

    public final Minecraft mc;
    public final HTRewrite htRewrite;

    private int key;
    private boolean enabled;
    private ConfigUtils configUtils;

    public Module(String name, String description, ModuleType category, int default_key) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.options = new ArrayList<>();
        this.mc = Minecraft.getMinecraft();
        this.htRewrite = HTRewrite.INSTANCE;
        this.key = default_key;
        this.configUtils = new ConfigUtils(name, "modules");
    }

    @Override public String getLabel() { return name; }

    public String getName() { return name; }
    public String getDesc() { return description; }
    public void setKey(int key) { this.key = key; }
    public int getKey() { return key; }
    public ModuleType getCategory() { return category; }
    public ArrayList<Setting> getOptions() { return options; }

    public boolean isEnabled() { return enabled; }
    public void toggle() {
        enabled = !enabled;

        if(enabled) onEnable();
        else onDisable();
    }

    public void onEnable() {
        HTRewrite.EVENT_BUS.subscribe(this);
        HTRewrite.EVENT_BUS.post(new ModuleToggleEvent(this, true));
    }
    public void onDisable() {
        HTRewrite.EVENT_BUS.post(new ModuleToggleEvent(this, false));
        HTRewrite.EVENT_BUS.unsubscribe(this);
    }

    public void addOption(Setting setting) { options.add(setting); }
    public void endOption() {
        if(!configUtils.getFile().exists() || configUtils.getJSON().isEmpty()) save(); else {
            ArrayList<Object> arrayList = new ArrayList<Object>();
            arrayList.add(configUtils.get("enabled"));
            arrayList.add(configUtils.get("key"));
            for(Setting setting : getOptions())
                arrayList.add(configUtils.get(setting.getLabel()));
            boolean succ = false;
            for(Object object : arrayList)
                if(object == null)
                    succ = true;
            if(succ) save();
            arrayList.clear();

            enabled = (boolean)configUtils.get("enabled");
            key = (Integer)configUtils.get("key");
            for(Setting setting : getOptions()) {
                if(setting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting)setting;
                    modeSetting.setValue((int)configUtils.get(modeSetting.getLabel()));
                } else if(setting instanceof StringSetting) {
                    StringSetting stringSetting = (StringSetting)setting;
                    stringSetting.setValue((String)configUtils.get(stringSetting.getLabel()));
                } else if(setting instanceof ValueSetting) {
                    ValueSetting valueSetting = (ValueSetting)setting;
                    valueSetting.setValue(((Number)configUtils.get(valueSetting.getLabel())).doubleValue());
                } else if(setting instanceof ToggleableSetting) {
                    ToggleableSetting toggleableSetting = (ToggleableSetting)setting;
                    toggleableSetting.setEnabled((boolean)configUtils.get(toggleableSetting.getLabel()));
                }
            }
        }
    }

    public Setting getOptionByLowercaseName(String optionName) {
        for(Setting setting : getOptions())
            if(setting.getLabel().equalsIgnoreCase(optionName))
                return setting;
        return null;
    }
    public String getMeta() { return ""; }

    public void save() {
        configUtils.set("enabled", enabled);
        configUtils.set("key", key);
        for(Setting setting : getOptions()) {
            if(setting instanceof ModeSetting) {
                ModeSetting modeSetting = (ModeSetting)setting;
                configUtils.set(modeSetting.getLabel(), modeSetting.getI());
            } else if(setting instanceof StringSetting) {
                StringSetting stringSetting = (StringSetting)setting;
                configUtils.set(stringSetting.getLabel(), stringSetting.getValue());
            } else if(setting instanceof ValueSetting) {
                ValueSetting valueSetting = (ValueSetting)setting;
                configUtils.set(valueSetting.getLabel(), valueSetting.getValue());
            } else if(setting instanceof ToggleableSetting) {
                ToggleableSetting toggleableSetting = (ToggleableSetting)setting;
                configUtils.set(toggleableSetting.getLabel(), toggleableSetting.isEnabled());
            }
        }
        configUtils.save();
    }
}