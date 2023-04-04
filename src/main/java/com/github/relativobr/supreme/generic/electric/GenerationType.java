package com.github.relativobr.supreme.generic.electric;

import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.BlockPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum GenerationType {

    SKY("Sky") {
        @Override
        protected int generate(@Nonnull World world, @Nonnull Block block, int def) {
            switch (world.getEnvironment()) {
                case NORMAL: {
                    if (block.getRelative(BlockFace.UP).getLightFromSky() == 15) {
                        return def;
                    }
                    return 0;
                }
                case NETHER:
                case THE_END:
                default:
                    return 0;
            }
        }
    },
    FIRE("Fire") {
        @Override
        protected int generate(@Nonnull World world, @Nonnull Block block, int def) {
            Material material = block.getRelative(BlockFace.DOWN).getType();
            if (material == Material.FIRE
                    || material == Material.SOUL_FIRE
                    || material == Material.LAVA
                    || material == Material.CAMPFIRE
                    || material == Material.SOUL_CAMPFIRE) {
                return def;
            }
            return 0;
        }
    },
    WATER("Water") {
        @Override
        protected int generate(@Nonnull World world, @Nonnull Block block, int def) {
            Material material = block.getRelative(BlockFace.DOWN).getType();
            if (material == Material.WATER
                    || material == Material.WATER_CAULDRON) {
                return def;
            }
            return 0;
        }
    },
    WIND("Wind") {
        public final Map<BlockPosition, BlockFace> cachedFace = new HashMap<>();

        @Override
        protected int generate(@Nonnull World world, @Nonnull Block block, int def) {
            switch (world.getEnvironment()) {
                case NETHER:
                case NORMAL: {
                    BlockPosition position = new BlockPosition(block);
                    if ((cachedFace.containsKey(position) && block.getRelative(cachedFace.get(position)).getType() == Material.AIR) || checkRelative(block, BlockFace.NORTH) || checkRelative(block, BlockFace.EAST) || checkRelative(block, BlockFace.SOUTH) || checkRelative(block, BlockFace.WEST)) {
                        return def;
                    }
                    return 0;
                }
                case THE_END:
                default:
                    return 0;
            }
        }

        protected boolean checkRelative(Block block, BlockFace face) {
            if (block.getRelative(face).getType() == Material.AIR) {
                cachedFace.put(new BlockPosition(block), face);
                return true;
            }
            return false;
        }
    },
    DARK("Dark") {
        @Override
        protected int generate(@Nonnull World world, @Nonnull Block block, int def) {
            switch (world.getEnvironment()) {
                case NETHER:
                case THE_END:
                    return def;
                case NORMAL:
                    if (block.getRelative(BlockFace.UP).getLightFromSky() != 15) {
                        return def;
                    }
                    return 0;
                default:
                    return 0;
            }
        }
    },
    EVERY("Every") {
        @Override
        protected int generate(@Nonnull World world, @Nonnull Block block, int def) {
            return def;
        }
    };

    @Getter
    private final String toString;

    protected abstract int generate(@Nonnull World world, @Nonnull Block block, int def);

}
