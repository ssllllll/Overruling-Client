package me.overruling.client;

import me.overruling.client.clickgui.ClickGuiScreen;
import me.overruling.client.command.CommandManager;
import me.overruling.client.event.CEventProcessor;
import me.overruling.client.event.EventProcessor;
import me.overruling.client.module.Module;
import me.overruling.client.module.ModuleManager;
import me.overruling.exeterimports.keybind.KeybindManager;
import me.zero.alpine.fork.bus.EventBus;
import me.zero.alpine.fork.bus.EventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = Overruling.MOD_ID, name = Overruling.NAME, version = Overruling.VERSION)
public class Overruling {
    public static final String MOD_ID = "overruling";
    public static final String NAME = "Overruling";
    public static final String VERSION = "b0.1";

    public static final EventBus EVENT_BUS = new EventManager();

    public static Overruling INSTANCE;
    public static Logger logger;

    private KeybindManager keybindManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;

    private ClickGuiScreen clickGuiScreen;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        INSTANCE = this;

        logger = event.getModLog();
        logger.info("preInit");

        Display.setTitle(NAME + " " + VERSION);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("init");

        keybindManager = new KeybindManager();

        moduleManager = new ModuleManager();
        moduleManager.setModules();
        for(Module module : moduleManager.getModules())
            if(module.isEnabled())
                module.onEnable();

        commandManager = new CommandManager();

        clickGuiScreen = new ClickGuiScreen();

        MinecraftForge.EVENT_BUS.register(new EventProcessor());

        EVENT_BUS.subscribe(new CEventProcessor());
    }

    public void saveEverything() {
        for(Module module : getModuleManager().getModules())
            module.save();
    }

    public KeybindManager getKeybindManager() { return keybindManager; }
    public ModuleManager getModuleManager() { return moduleManager; }
    public CommandManager getCommandManager() { return commandManager; }

    public ClickGuiScreen getClickGuiScreen() { return clickGuiScreen; }
}