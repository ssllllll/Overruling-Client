package me.htrewrite.client.command;

import net.minecraft.client.Minecraft;

public class Command {
    protected final Minecraft mc;
    private String alias, usage, description;

    public Command(String alias, String usage, String description) {
        mc = Minecraft.getMinecraft();
        this.alias = alias;
        this.usage = usage;
        this.description = description;
    }

    public String formatCmd(String cmd) { return CommandManager.prefix + cmd; }

    public String getAlias() { return alias; }
    public String getUsage() { return usage; }
    public String getDescription() { return description; }

    public void call(String[] args) {}
}