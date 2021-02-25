package me.htrewrite.client;

import me.htrewrite.client.clickgui.ClickGuiScreen;
import me.htrewrite.client.command.CommandManager;
import me.htrewrite.client.event.CEventProcessor;
import me.htrewrite.client.event.EventProcessor;
import me.htrewrite.client.manager.FriendManager;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleManager;
import me.htrewrite.exeterimports.keybind.KeybindManager;
import me.zero.alpine.fork.bus.EventBus;
import me.zero.alpine.fork.bus.EventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import org.lwjgl.opengl.Display;

@Mod(modid = HTRewrite.MOD_ID, name = HTRewrite.NAME, version = HTRewrite.VERSION)
public class HTRewrite {
    public static final String MOD_ID = "htrewrite";
    public static final String NAME = "HT+Rewrite";
    public static final String VERSION = "a1.0";

    public static final EventBus EVENT_BUS = new EventManager();

    public static HTRewrite INSTANCE;
    public static Logger logger;

    private KeybindManager keybindManager;
    private FriendManager friendManager;
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

        friendManager = new FriendManager();

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
        friendManager.configUtils.save();
        for(Module module : getModuleManager().getModules())
            module.save();
        commandManager.configUtils.save();
    }

    public KeybindManager getKeybindManager() { return keybindManager; }
    public FriendManager getFriendManager() { return friendManager; }
    public ModuleManager getModuleManager() { return moduleManager; }
    public CommandManager getCommandManager() { return commandManager; }

    public ClickGuiScreen getClickGuiScreen() { return clickGuiScreen; }
}