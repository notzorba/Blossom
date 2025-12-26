package com.xyrisdev.blossom.util;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WorldEditSelectionUtil {

    private WorldEditSelectionUtil() {
    }

    public record Selection(@NotNull Location min, @NotNull Location max, @NotNull Location center) {
    }

    /**
     * @return The player's current WorldEdit selection for their current world, or null if incomplete/absent.
     */
    public static @Nullable Selection selection(@NotNull org.bukkit.entity.Player bukkitPlayer) {
        final Player wePlayer = BukkitAdapter.adapt(bukkitPlayer);
        final LocalSession session = WorldEdit.getInstance().getSessionManager().get(wePlayer);
        final World weWorld = BukkitAdapter.adapt(bukkitPlayer.getWorld());

        final Region region;
        try {
            region = session.getSelection(weWorld);
        } catch (IncompleteRegionException e) {
            return null;
        }

        final BlockVector3 min = region.getMinimumPoint();
        final BlockVector3 max = region.getMaximumPoint();

        final Location minLoc = new Location(bukkitPlayer.getWorld(), min.x(), min.y(), min.z());
        final Location maxLoc = new Location(bukkitPlayer.getWorld(), max.x(), max.y(), max.z());
        final Location centerLoc = new Location(
                bukkitPlayer.getWorld(),
            (min.x() + max.x()) / 2.0,
            (min.y() + max.y()) / 2.0,
            (min.z() + max.z()) / 2.0
        );

        return new Selection(minLoc, maxLoc, centerLoc);
    }
}
