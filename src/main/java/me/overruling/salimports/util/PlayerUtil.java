package me.overruling.salimports.util;

import me.overruling.client.Wrapper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class PlayerUtil {
    public static BlockPos GetLocalPlayerPosFloored() { return new BlockPos(Math.floor(Wrapper.getMC().player.posX), Math.floor(Wrapper.getMC().player.posY), Math.floor(Wrapper.getMC().player.posZ)); }
    public static EnumFacing GetFacing()
    {
        switch (MathHelper.floor((double) (Wrapper.getMC().player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7)
        {
            case 0:
            case 1:
                return EnumFacing.SOUTH;
            case 2:
            case 3:
                return EnumFacing.WEST;
            case 4:
            case 5:
                return EnumFacing.NORTH;
            case 6:
            case 7:
                return EnumFacing.EAST;
        }
        return EnumFacing.NORTH;
    }
}