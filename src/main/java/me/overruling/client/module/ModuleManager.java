package me.overruling.client.module;

import me.overruling.client.module.modules.gui.ClickGuiModule;
import me.overruling.client.module.modules.render.FullbrightModule;

import java.util.ArrayList;

public class ModuleManager {
    private ArrayList<Module> modules;

    public ModuleManager() {
        this.modules = new ArrayList<Module>();
    }

    public void setModules() {
        /* Combat */

        /* Exploits */

        /* Miscellaneous */

        /* Movement */

        /* Render */
        modules.add(new FullbrightModule());
        /* World */

        /* Gui */
        modules.add(new ClickGuiModule());
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
    public ArrayList<Module> getModulesByCategory(ModuleType moduleType) {
        ArrayList<Module> modules = new ArrayList<Module>();
        for(Module module : getModules())
            if(module.getCategory() == moduleType)
                modules.add(module);
        return modules;
    }
    public Module getModuleByClass(Class<? extends Module> moduleClass) {
        for(Module module : getModules())
            if(module.getClass() == moduleClass)
                return module;
        return null;
    }
    public Module getModuleByLowercaseName(String moduleName) {
        for(Module module : getModules())
            if(module.getName().equalsIgnoreCase(moduleName))
                return module;
        return null;
    }
}