package github.minersStudios.msUtils.listeners.block;

import github.minersStudios.msUtils.utils.EventListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import javax.annotation.Nonnull;

@EventListener
public class PistonListener implements Listener {

    @EventHandler
    public void onPistonExtends(@Nonnull BlockPistonExtendEvent event){
        for (Block block : event.getBlocks()) {
            event.setCancelled(block.getType() == Material.NOTE_BLOCK || block.getType() == Material.STRUCTURE_VOID);
        }
    }

    @EventHandler
    public void onPistonEvent(@Nonnull BlockPistonRetractEvent event){
        for (Block block : event.getBlocks()) {
            event.setCancelled(block.getType() == Material.NOTE_BLOCK || block.getType() == Material.STRUCTURE_VOID);
        }
    }
}
