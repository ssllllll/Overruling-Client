package me.htrewrite.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Wrapper {
    public static Minecraft getMC() { return Minecraft.getMinecraft(); }
    public static EntityPlayerSP getPlayer() { return getMC().player; }
}