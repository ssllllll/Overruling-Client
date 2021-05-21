package me.htrewrite.client.module.modules.render;

import me.htrewrite.client.clickgui.components.buttons.settings.bettermode.BetterMode;
import me.htrewrite.client.event.custom.player.PlayerUpdateEvent;
import me.htrewrite.client.event.custom.render.RenderEvent;
import me.htrewrite.client.module.Module;
import me.htrewrite.client.module.ModuleType;
import me.htrewrite.client.util.RenderHelp;
import me.htrewrite.exeterimports.mcapi.settings.ModeSetting;
import me.htrewrite.exeterimports.mcapi.settings.ToggleableSetting;
import me.htrewrite.exeterimports.mcapi.settings.ValueSetting;
import me.htrewrite.salimports.util.Pair;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoleESPModule extends Module {
    public static final ModeSetting mode = new ModeSetting("Mode", null, 0, BetterMode.construct("Pretty", "Solid", "Outline", "Glow", "Glow 2"));
    public static final ValueSetting<Double> off_set = new ValueSetting<>("Height", null, .2d, 0d, 1d);
    public static final ValueSetting<Double> range = new ValueSetting<>("Range", null, 6d, 1d, 12d);
    public static final ToggleableSetting hide_own = new ToggleableSetting("HideOwn", null, true);
    public static final ToggleableSetting dual_enable = new ToggleableSetting("Dual", null, true);
    public static final ValueSetting<Double> lineA = new ValueSetting<>("LineA", null, 255d, 0d, 255d);
    public static final ToggleableSetting bedrock_enable = new ToggleableSetting("Bedrock", null, true);
    public static final ValueSetting<Double> rb = new ValueSetting<>("B_R", null, 0d, 0d, 255d);
    public static final ValueSetting<Double> gb = new ValueSetting<>("B_G", null, 255d, 0d, 255d);
    public static final ValueSetting<Double> bb = new ValueSetting<>("B_B", null, 0d, 0d, 255d);
    public static final ValueSetting<Double> ab = new ValueSetting<>("B_A", null, 50d, 0d, 255d);
    public static final ToggleableSetting obsidian_enable = new ToggleableSetting("Obsidian", null, true);
    public static final ValueSetting<Double> ro = new ValueSetting<>("O_R", null, 255d, 0d, 255d);
    public static final ValueSetting<Double> go = new ValueSetting<>("O_G", null, 0d, 0d, 255d);
    public static final ValueSetting<Double> bo = new ValueSetting<>("O_B", null, 0d, 0d, 255d);
    public static final ValueSetting<Double> ao = new ValueSetting<>("O_A", null, 50d, 0d, 255d);

    ArrayList<Pair<BlockPos, Boolean>> holes = new ArrayList<>();
    ArrayList<Pair<BlockPos, Boolean>> dual_holes = new ArrayList<>();
    Map<BlockPos, Integer> dual_hole_sides = new HashMap<>();

    boolean outline = false;
    boolean solid = false;
    boolean glow = false;
    boolean glowOutline = false;

    int color_r_o;
    int color_g_o;
    int color_b_o;

    int color_r_b;
    int color_g_b;
    int color_b_b;

    int color_r;
    int color_g;
    int color_b;
    int color_a;

    int safe_sides;

    public static HoleESPModule INSTANCE;
    public HoleESPModule() {
        super("HoleESP", "Renders holes.", ModuleType.Render, 0);
        addOption(mode);
        addOption(off_set);
        addOption(range);
        addOption(hide_own);
        addOption(dual_enable);
        addOption(lineA);
        addOption(bedrock_enable);
        addOption(rb.setVisibility(v -> bedrock_enable.isEnabled()));
        addOption(gb.setVisibility(v -> bedrock_enable.isEnabled()));
        addOption(bb.setVisibility(v -> bedrock_enable.isEnabled()));
        addOption(ab.setVisibility(v -> bedrock_enable.isEnabled()));
        addOption(obsidian_enable);
        addOption(ro.setVisibility(v -> obsidian_enable.isEnabled()));
        addOption(go.setVisibility(v -> obsidian_enable.isEnabled()));
        addOption(bo.setVisibility(v -> obsidian_enable.isEnabled()));
        addOption(ao.setVisibility(v -> obsidian_enable.isEnabled()));
        endOption();

        INSTANCE = this;
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        color_r_b = rb.getValue().intValue();
        color_g_b = gb.getValue().intValue();
        color_b_b = bb.getValue().intValue();
        color_r_o = ro.getValue().intValue();
        color_g_o = go.getValue().intValue();
        color_b_o = bo.getValue().intValue();
        holes.clear();
        dual_holes.clear();
        dual_hole_sides.clear();

        if(mc.player == null || mc.world == null)
            return;
        switch (mode.getValue()) {
            case "Pretty": {
                outline = true;
                solid   = true;
                glow = false;
                glowOutline = false;
            } break;
            case "Solid": {
                outline = false;
                solid   = true;
                glow = false;
                glowOutline = false;
            } break;
            case "Outline": {
                outline = true;
                solid   = false;
                glow = false;
                glowOutline = false;
            } break;
            case "Glow": {
                outline = false;
                solid = false;
                glow = true;
                glowOutline = false;
            } break;
            case "Glow 2": {
                outline = false;
                solid = false;
                glow = true;
                glowOutline = true;
            } break;
            default: break;
        }

        int colapso_range = range.getValue().intValue();
        List<BlockPos> spheres = sphere(player_as_blockpos(), colapso_range);
        for (BlockPos pos : spheres) {
            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
                continue;
            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR))
                continue;
            if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR))
                continue;

            boolean possible = true;
            safe_sides = 0;
            int air_orient = -1;
            int counter = 0;
            for (BlockPos seems_blocks : new BlockPos[] {
                    new BlockPos( 0, -1,  0),
                    new BlockPos( 0,  0, -1),
                    new BlockPos( 1,  0,  0),
                    new BlockPos( 0,  0,  1),
                    new BlockPos(-1,  0,  0)
            }) {
                Block block = mc.world.getBlockState(pos.add(seems_blocks)).getBlock();

                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    possible = false;

                    if (counter == 0) break;

                    if (air_orient != -1) {
                        air_orient = -1;
                        break;
                    }

                    if (block.equals(Blocks.AIR)) {
                        air_orient = counter;
                    } else {
                        break;
                    }
                }

                if (block == Blocks.BEDROCK) {
                    safe_sides++;
                }
                counter++;
            }
            if (possible) {
                if (safe_sides == 5) {
                    if (!bedrock_enable.isEnabled()) continue;
                    holes.add(new Pair<>(pos, true));
                } else {
                    if (!obsidian_enable.isEnabled()) continue;
                    holes.add(new Pair<>(pos, false));
                }
                continue;
            }

            if (!dual_enable.isEnabled() || air_orient < 0) continue;
            BlockPos second_pos = pos.add(orientConv(air_orient));
            if (checkDual(second_pos, air_orient)) {
                boolean low_ceiling_hole = !mc.world.getBlockState(second_pos.add(0,1,0)).getBlock().equals(Blocks.AIR);
                if (safe_sides == 8) {
                    if (low_ceiling_hole) {
                        holes.add(new Pair<BlockPos, Boolean>(pos, true));
                    } else {
                        if (!dual_hole_sides.containsKey(pos)) {
                            dual_holes.add(new Pair<BlockPos, Boolean>(pos, true));
                            dual_hole_sides.put(pos, air_orient);
                        }
                        if (!dual_hole_sides.containsKey(second_pos)) {
                            dual_holes.add(new Pair<BlockPos, Boolean>(second_pos, true));
                            dual_hole_sides.put(second_pos, oppositeIntOrient(air_orient));
                        }
                    }
                } else {
                    if (low_ceiling_hole) {
                        holes.add(new Pair<BlockPos, Boolean>(pos, false));
                    } else {
                        if (!dual_hole_sides.containsKey(pos)) {
                            dual_holes.add(new Pair<BlockPos, Boolean>(pos, false));
                            dual_hole_sides.put(pos, air_orient);
                        }
                        if (!dual_hole_sides.containsKey(second_pos)) {
                            dual_holes.add(new Pair<BlockPos, Boolean>(second_pos, false));
                            dual_hole_sides.put(second_pos, oppositeIntOrient(air_orient));
                        }
                    }
                }
            }
        }
    });

    public List<BlockPos> sphere(BlockPos pos, float r) {
        int plus_y = 0;

        List<BlockPos> sphere_block = new ArrayList<>();

        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();

        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = cy - (int)r; y < cy + r; ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (cy - y) * (cy - y);
                    if (dist < r * r) {
                        BlockPos spheres = new BlockPos(x, y + plus_y, z);
                        sphere_block.add(spheres);
                    }
                }
            }
        }

        return sphere_block;
    }

    public BlockPos player_as_blockpos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private BlockPos orientConv(int orient_count) {
        BlockPos converted = null;

        switch(orient_count) {
            case 0:
                converted = new BlockPos( 0, -1,  0);
                break;
            case 1:
                converted = new BlockPos( 0,  0, -1);
                break;
            case 2:
                converted = new BlockPos( 1,  0,  0);
                break;
            case 3:
                converted = new BlockPos( 0,  0,  1);
                break;
            case 4:
                converted = new BlockPos(-1,  0,  0);
                break;
            case 5:
                converted = new BlockPos(0,  1,  0);
                break;
        }
        return converted;
    }

    private int oppositeIntOrient(int orient_count) {

        int opposite = 0;

        switch(orient_count)
        {
            case 0:
                opposite = 5;
                break;
            case 1:
                opposite = 3;
                break;
            case 2:
                opposite = 4;
                break;
            case 3:
                opposite = 1;
                break;
            case 4:
                opposite = 2;
                break;
        }
        return opposite;
    }

    private boolean checkDual(BlockPos second_block, int counter) {
        int i = -1;

		/*
			lets check down from second block to not have esp of a dual hole of one space
			missing a bottom block
		*/
        for (BlockPos seems_blocks : new BlockPos[] {
                new BlockPos( 0,  -1, 0), //Down
                new BlockPos( 0,  0, -1), //N
                new BlockPos( 1,  0,  0), //E
                new BlockPos( 0,  0,  1), //S
                new BlockPos(-1,  0,  0)  //W
        }) {
            i++;
            //skips opposite direction check, since its air
            if(counter == oppositeIntOrient(i)) {
                continue;
            }

            Block block = mc.world.getBlockState(second_block.add(seems_blocks)).getBlock();
            if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                return false;
            }

            if (block == Blocks.BEDROCK) {
                safe_sides++;
            }
        }
        return true;
    }

    @EventHandler
    private Listener<RenderEvent> renderEventListener = new Listener<>(event -> {
        float off_set_h;
        if (!holes.isEmpty() || !dual_holes.isEmpty()) {
            off_set_h = off_set.getValue().floatValue();

            for (Pair<BlockPos, Boolean> hole : holes) {
                if (hole.getSecond()) {
                    color_r = color_r_b;
                    color_g = color_g_b;
                    color_b = color_b_b;
                    color_a = ab.getValue().intValue();
                } else {
                    color_r = color_r_o;
                    color_g = color_g_o;
                    color_b = color_b_o;
                    color_a = ao.getValue().intValue();
                }

                if (hide_own.isEnabled() && hole.getFirst().equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
                    continue;
                }

                if (solid) {
                    RenderHelp.prepare("quads");
                    RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                            hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ(),
                            1, off_set_h, 1,
                            color_r, color_g, color_b, color_a,
                            "all"
                    );

                    RenderHelp.release();
                }

                if (outline) {
                    RenderHelp.prepare("lines");
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ(),
                            1, off_set_h, 1,
                            color_r, color_g, color_b, lineA.getValue().intValue(),
                            "all"
                    );

                    RenderHelp.release();
                }

                if (glow) {
                    RenderHelp.prepare("lines");
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ(),
                            1, 0, 1,
                            color_r, color_g, color_b, lineA.getValue().intValue(),
                            "all"
                    );
                    RenderHelp.release();

                    RenderHelp.prepare("quads");
                    RenderHelp.draw_gradiant_cube(RenderHelp.get_buffer_build(),
                            hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ(),
                            1, off_set_h, 1,
                            new Color(color_r, color_g, color_b, color_a), new Color(0, 0, 0, 0),
                            "all"
                    );

                    RenderHelp.release();
                }

                if (glowOutline) {
                    RenderHelp.prepare("lines");
                    RenderHelp.draw_gradiant_outline(RenderHelp.get_buffer_build(), hole.getFirst().getX(),
                            hole.getFirst().getY(), hole.getFirst().getZ(), off_set_h,
                            new Color(color_r, color_g, color_b, lineA.getValue().intValue()),
                            new Color(0, 0, 0, 0), "all");
                    RenderHelp.release();
                }
            }

            for (Pair<BlockPos, Boolean> hole : dual_holes) {

                BlockPos playerPos = new BlockPos(mc.player);
                if (hide_own.isEnabled() && (hole.getFirst().equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)) ||
                        hole.getFirst().equals(playerPos.add(orientConv(oppositeIntOrient(dual_hole_sides.get(hole.getFirst()))))))) {
                    continue;
                }

                if (hole.getSecond()) {
                    color_r = color_r_b;
                    color_g = color_g_b;
                    color_b = color_b_b;
                    color_a = ab.getValue().intValue();
                } else {
                    color_r = color_r_o;
                    color_g = color_g_o;
                    color_b = color_b_o;
                    color_a = ao.getValue().intValue();
                }

                if (solid) {
                    RenderHelp.prepare("quads");
                    RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                            hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ(),
                            1, off_set_h, 1,
                            color_r, color_g, color_b, color_a,
                            getDirectionsToRenderQuad(hole.getFirst())
                    );
                    RenderHelp.release();
                }

                if (outline) {
                    RenderHelp.prepare("lines");
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ(),
                            1, off_set_h, 1,
                            color_r, color_g, color_b, lineA.getValue().intValue(),
                            getDirectionsToRenderOutline(hole.getFirst())
                    );

                    RenderHelp.release();
                }

                if (glow) {
                    RenderHelp.prepare("lines");
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ(),
                            1, 0, 1,
                            color_r, color_g, color_b, lineA.getValue().intValue(),
                            getDirectionsToRenderOutline(hole.getFirst())
                    );
                    RenderHelp.release();

                    RenderHelp.prepare("quads");
                    RenderHelp.draw_gradiant_cube(RenderHelp.get_buffer_build(),
                            hole.getFirst().getX(), hole.getFirst().getY(), hole.getFirst().getZ(),
                            1, off_set_h, 1,
                            new Color(color_r, color_g, color_b, color_a), new Color(0, 0, 0, 0),
                            getDirectionsToRenderQuad(hole.getFirst())
                    );
                    RenderHelp.release();
                }

                if (glowOutline) {
                    RenderHelp.prepare("lines");
                    RenderHelp.draw_gradiant_outline(RenderHelp.get_buffer_build(), hole.getFirst().getX(),
                            hole.getFirst().getY(), hole.getFirst().getZ(), off_set_h,
                            new Color(color_r, color_g, color_b, lineA.getValue().intValue()),
                            new Color(0, 0, 0, 0),
                            getDirectionsToRenderOutline(hole.getFirst())
                    );
                    RenderHelp.release();
                }
            }
        }
    });

    private String getDirectionsToRenderQuad(BlockPos hole) {
        int sideNotToDraw = dual_hole_sides.get(hole);

        switch(sideNotToDraw) {
            case 1:
                return "east-south-west-top-bottom";
            case 2:
                return "north-south-west-top-bottom";
            case 3:
                return "north-east-west-top-bottom";
            case 4:
                return "north-east-south-top-bottom";
            default:
                break;
        }

        return "all";
    }

    private String getDirectionsToRenderOutline (BlockPos hole) {
        int sideNoToDraw = dual_hole_sides.get(hole);
        switch(sideNoToDraw) {
            case 1:
                return "downeast-upeast-downsouth-upsouth-downwest-upwest-southwest-southeast";
            case 2:
                return "downnorth-upnorth-downsouth-upsouth-downwest-upwest-northwest-southwest";
            case 3:
                return "upnorth-downnorth-upeast-downeast-upwest-downwest-northeast-northwest";
            case 4:
                return "upnorth-downnorth-upeast-downeast-upsouth-downsouth-northeast-southeast";
            default:
                break;
        }
        return "all";
    }

    public boolean isBlockHole(BlockPos block) {
        return holes.contains(new Pair<BlockPos, Boolean>(block, true)) || holes.contains(new Pair<BlockPos, Boolean>(block, false));
    }
}