package me.overruling.client.module.modules.world;

import me.overruling.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.overruling.client.event.custom.CustomEvent;
import me.overruling.client.event.custom.player.PlayerMotionUpdateEvent;
import me.overruling.client.module.Module;
import me.overruling.client.module.ModuleType;
import me.overruling.exeterimports.mcapi.settings.ModeSetting;
import me.overruling.exeterimports.mcapi.settings.ToggleableSetting;
import me.overruling.salimports.util.BlockInteractionHelper;
import me.overruling.salimports.util.PlayerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AutoTunnelModule extends Module {
    public static final ModeSetting mode = new ModeSetting("Mode", null, 0, BetterMode.construct("1x2", "2x2", "2x3", "3x3"));
    public static final ModeSetting miningMode = new ModeSetting("MiningMode", null, 0, BetterMode.construct("Normal", "Packet"));
    public static final ToggleableSetting visualize = new ToggleableSetting("Visualize", null, true);
    public static final ToggleableSetting pauseAutoWalk = new ToggleableSetting("PauseAutoWalk", null, true);

    private List<BlockPos> _blocksToDestroy = new CopyOnWriteArrayList<>();
    private ICamera camera = new Frustum();
    private boolean _needPause = false;

    public AutoTunnelModule() {
        super("AutoTunnel", "Name says it all", ModuleType.World, 0);
        addOption(mode);
        addOption(miningMode);
        addOption(visualize);
        addOption(pauseAutoWalk);
        endOption();
    }

    @EventHandler
    private Listener<PlayerMotionUpdateEvent> motionUpdateEventListener = new Listener<>(event -> {
        if(event.getEra() != CustomEvent.Era.PRE || event.isCancelled())
            return;

        _blocksToDestroy.clear();
        BlockPos playerPos = PlayerUtil.GetLocalPlayerPosFloored();

        switch (PlayerUtil.GetFacing()) {
            case EAST: {
                switch (mode.getValue()) {
                    case "1x2": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.east());
                            _blocksToDestroy.add(playerPos.east().up());

                            playerPos = new BlockPos(playerPos).east();
                        }
                        break;
                    }

                    case "2x2": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.east());
                            _blocksToDestroy.add(playerPos.east().up());
                            _blocksToDestroy.add(playerPos.east().north());
                            _blocksToDestroy.add(playerPos.east().north().up());

                            playerPos = new BlockPos(playerPos).east();
                        }
                        break;
                    }

                    case "2x3": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.east());
                            _blocksToDestroy.add(playerPos.east().up());
                            _blocksToDestroy.add(playerPos.east().up().up());
                            _blocksToDestroy.add(playerPos.east().north());
                            _blocksToDestroy.add(playerPos.east().north().up());
                            _blocksToDestroy.add(playerPos.east().north().up().up());

                            playerPos = new BlockPos(playerPos).east();
                        }
                        break;
                    }

                    case "3x3": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.east());
                            _blocksToDestroy.add(playerPos.east().up());
                            _blocksToDestroy.add(playerPos.east().up().up());
                            _blocksToDestroy.add(playerPos.east().north());
                            _blocksToDestroy.add(playerPos.east().north().up());
                            _blocksToDestroy.add(playerPos.east().north().up().up());
                            _blocksToDestroy.add(playerPos.east().north().north());
                            _blocksToDestroy.add(playerPos.east().north().north().up());
                            _blocksToDestroy.add(playerPos.east().north().north().up().up());

                            playerPos = new BlockPos(playerPos).east();
                        }
                        break;
                    }

                    default: break;
                }
                break;
            }
            case NORTH: {
                switch (mode.getValue()) {
                    case "1x2": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.north());
                            _blocksToDestroy.add(playerPos.north().up());

                            playerPos = new BlockPos(playerPos).north();
                        }
                        break;
                    }

                    case "2x2": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.north());
                            _blocksToDestroy.add(playerPos.north().up());
                            _blocksToDestroy.add(playerPos.north().east());
                            _blocksToDestroy.add(playerPos.north().east().up());

                            playerPos = new BlockPos(playerPos).north();
                        }
                        break;
                    }

                    case "2x3": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.north());
                            _blocksToDestroy.add(playerPos.north().up());
                            _blocksToDestroy.add(playerPos.north().up().up());
                            _blocksToDestroy.add(playerPos.north().east());
                            _blocksToDestroy.add(playerPos.north().east().up());
                            _blocksToDestroy.add(playerPos.north().east().up().up());

                            playerPos = new BlockPos(playerPos).north();
                        }
                        break;
                    }

                    case "3x3": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.north());
                            _blocksToDestroy.add(playerPos.north().up());
                            _blocksToDestroy.add(playerPos.north().up().up());
                            _blocksToDestroy.add(playerPos.north().east());
                            _blocksToDestroy.add(playerPos.north().east().up());
                            _blocksToDestroy.add(playerPos.north().east().up().up());
                            _blocksToDestroy.add(playerPos.north().east().east());
                            _blocksToDestroy.add(playerPos.north().east().east().up());
                            _blocksToDestroy.add(playerPos.north().east().east().up().up());

                            playerPos = new BlockPos(playerPos).north();
                        }
                        break;
                    }

                    default: break;
                }
                break;
            }
            case SOUTH: {
                switch (mode.getValue()) {
                    case "1x2": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.south());
                            _blocksToDestroy.add(playerPos.south().up());

                            playerPos = new BlockPos(playerPos).south();
                        }
                        break;
                    }

                    case "2x2": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.south());
                            _blocksToDestroy.add(playerPos.south().up());
                            _blocksToDestroy.add(playerPos.south().west());
                            _blocksToDestroy.add(playerPos.south().west().up());

                            playerPos = new BlockPos(playerPos).south();
                        }
                        break;
                    }

                    case "2x3": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.south());
                            _blocksToDestroy.add(playerPos.south().up());
                            _blocksToDestroy.add(playerPos.south().up().up());
                            _blocksToDestroy.add(playerPos.south().west());
                            _blocksToDestroy.add(playerPos.south().west().up());
                            _blocksToDestroy.add(playerPos.south().west().up().up());

                            playerPos = new BlockPos(playerPos).south();
                        }
                        break;
                    }

                    case "3x3": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.south());
                            _blocksToDestroy.add(playerPos.south().up());
                            _blocksToDestroy.add(playerPos.south().up().up());
                            _blocksToDestroy.add(playerPos.south().west());
                            _blocksToDestroy.add(playerPos.south().west().up());
                            _blocksToDestroy.add(playerPos.south().west().up().up());
                            _blocksToDestroy.add(playerPos.south().west().west());
                            _blocksToDestroy.add(playerPos.south().west().west().up());
                            _blocksToDestroy.add(playerPos.south().west().west().up().up());

                            playerPos = new BlockPos(playerPos).south();
                        }
                        break;
                    }

                    default: break;
                }
                break;
            }
            case WEST: {
                switch (mode.getValue()) {
                    case "1x2": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.west());
                            _blocksToDestroy.add(playerPos.west().up());

                            playerPos = new BlockPos(playerPos).west();
                        }
                        break;
                    }

                    case "2x2": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.west());
                            _blocksToDestroy.add(playerPos.west().up());
                            _blocksToDestroy.add(playerPos.west().south());
                            _blocksToDestroy.add(playerPos.west().south().up());

                            playerPos = new BlockPos(playerPos).west();
                        }
                        break;
                    }

                    case "2x3": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.west());
                            _blocksToDestroy.add(playerPos.west().up());
                            _blocksToDestroy.add(playerPos.west().up().up());
                            _blocksToDestroy.add(playerPos.west().south());
                            _blocksToDestroy.add(playerPos.west().south().up());
                            _blocksToDestroy.add(playerPos.west().south().up().up());

                            playerPos = new BlockPos(playerPos).west();
                        }
                        break;
                    }

                    case "3x3": {
                        for (int i = 0; i < 3; ++i)
                        {
                            _blocksToDestroy.add(playerPos.west());
                            _blocksToDestroy.add(playerPos.west().up());
                            _blocksToDestroy.add(playerPos.west().up().up());
                            _blocksToDestroy.add(playerPos.west().south());
                            _blocksToDestroy.add(playerPos.west().south().up());
                            _blocksToDestroy.add(playerPos.west().south().up().up());
                            _blocksToDestroy.add(playerPos.west().south().south());
                            _blocksToDestroy.add(playerPos.west().south().south().up());
                            _blocksToDestroy.add(playerPos.west().south().south().up().up());

                            playerPos = new BlockPos(playerPos).west();
                        }
                        break;
                    }

                    default: break;
                }
                break;
            }
            default: break;
        }

        BlockPos toDestroy = null;
        for (BlockPos pos : _blocksToDestroy) {
            IBlockState state = mc.world.getBlockState(pos);
            if (state.getBlock() == Blocks.AIR || state.getBlock() instanceof BlockDynamicLiquid || state.getBlock() instanceof BlockStaticLiquid || state.getBlock() == Blocks.BEDROCK)
                continue;
            toDestroy = pos;
            break;
        }

        if (toDestroy != null) {
            event.cancel();
            float[] rotations = BlockInteractionHelper.getLegitRotations(new Vec3d(toDestroy.getX(), toDestroy.getY(), toDestroy.getZ()));
        }
    });

    public boolean pauseAutoWalk() { return false; }
}