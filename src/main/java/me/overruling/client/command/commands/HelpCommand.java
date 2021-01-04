package me.overruling.client.command.commands;

import me.overruling.client.Overruling;
import me.overruling.client.Wrapper;
import me.overruling.client.command.Command;
import me.overruling.client.command.CommandManager;
import me.overruling.client.util.ChatColor;
import net.minecraft.util.text.TextComponentString;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "", "Shows all the commands.");
    }

    @Override
    public void call(String[] args) {
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.prefix_parse('&', "&eCommands:\n"));
        for(Command command : Overruling.INSTANCE.getCommandManager().getCommands())
            message.append(ChatColor.prefix_parse('&', "   &r" + CommandManager.prefix + command.getAlias() + " " + command.getUsage() + " - &l" + command.getDescription() + "\n"));

        Wrapper.getPlayer().sendMessage(new TextComponentString(message.toString()));
    }
}