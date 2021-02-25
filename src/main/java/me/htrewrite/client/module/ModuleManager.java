package me.htrewrite.client.module;

import me.htrewrite.client.module.modules.combat.AutoCityModule;
import me.htrewrite.client.module.modules.combat.AutoTotemModule;
import me.htrewrite.client.module.modules.combat.InstantBurrowModule;
import me.htrewrite.client.module.modules.exploit.AntiHungerModule;
import me.htrewrite.client.module.modules.exploit.PacketCancellerModule;
import me.htrewrite.client.module.modules.gui.ClickGuiModule;
import me.htrewrite.client.module.modules.gui.HUDModule;
import me.htrewrite.client.module.modules.gui.NotificationsModule;
import me.htrewrite.client.module.modules.miscellaneous.*;
import me.htrewrite.client.module.modules.movement.AutoWalkModule;
import me.htrewrite.client.module.modules.movement.ElytraFlyModule;
import me.htrewrite.client.module.modules.render.CityESPModule;
import me.htrewrite.client.module.modules.render.FullbrightModule;
import me.htrewrite.client.module.modules.render.ShulkerPreviewModule;
import me.htrewrite.client.module.modules.world.AutoTunnelModule;
import me.htrewrite.client.module.modules.world.EntityLoggerModule;
import me.htrewrite.client.module.modules.world.LawnmowerModule;

import java.util.ArrayList;

public class ModuleManager {
    private ArrayList<Module> modules;

    public ModuleManager() {
        this.modules = new ArrayList<Module>();
    }

    public void setModules() {
        /* Combat */
        modules.add(new AutoCityModule());
        modules.add(new AutoTotemModule());
        modules.add(new InstantBurrowModule());
        /* Exploits */
        modules.add(new AntiHungerModule());
        modules.add(new PacketCancellerModule());
        /* Miscellaneous */
        // modules.add(new AutoHappyMealModule()); /* CUMMING VERY HARD */ \\
        modules.add(new AutoQMainModule());
        modules.add(new ChatModule());
        modules.add(new MiddleClickModule());
        modules.add(new MobOwnerModule());
        modules.add(new XCarryModule());
        /* Movement */
        modules.add(new AutoWalkModule());
        modules.add(new ElytraFlyModule());
        /* Render */
        modules.add(new CityESPModule());
        modules.add(new FullbrightModule());
        modules.add(new ShulkerPreviewModule());
        /* World */
        modules.add(new AutoTunnelModule());
        modules.add(new EntityLoggerModule());
        modules.add(new LawnmowerModule());
        /* Gui */
        modules.add(new ClickGuiModule());
        modules.add(new HUDModule());
        modules.add(new NotificationsModule());
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