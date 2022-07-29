package github.minersStudios.msUtils.listeners.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import javax.annotation.Nonnull;

public class PistonListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonExtend(@Nonnull BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            event.setCancelled(block.getType() == Material.NOTE_BLOCK || block.getType() == Material.STRUCTURE_VOID);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonRetract(@Nonnull BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            event.setCancelled(block.getType() == Material.NOTE_BLOCK || block.getType() == Material.STRUCTURE_VOID);
        }
    }
}
