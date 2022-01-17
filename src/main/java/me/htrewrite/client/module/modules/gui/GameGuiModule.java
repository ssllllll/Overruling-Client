package me.htrewrite.client.module.modules.gui;

import me.htrewrite.client.Wrapper;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;

public class GameGuiModule extends Module {
    public static final ToggleableSetting fakeStash = new ToggleableSetting("FakeStash", false);

    public GameGuiModule() {
        super("GameGui", "Modifies the GameGUI", ModuleType.Gui, 0);
        addOption(fakeStash);
        endOption();
    }

    @EventHandler
    private Listener<GuiOpenEvent> guiOpenEventListener = new Listener<>(event -> {
        if(!(event.getGui() instanceof GuiContainer && fakeStash.isEnabled() && !(event.getGui() instanceof GuiContainerCreative)))
            return;
        GuiContainer gui = (GuiContainer) event.getGui();
        if(Wrapper.getPlayer() != null)
            Wrapper.getPlayer().sendMessage(new TextComponentString(gui.inventorySlots.inventorySlots.size() + ""));
        if(gui.inventorySlots.inventorySlots.size() == 90) {
            new Thread(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }

                gui.inventorySlots.getSlot(0).putStack(new ItemStack(Blocks.STONE));
            });
        }
    });
}