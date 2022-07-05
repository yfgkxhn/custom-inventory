package com.github.yungfasty.custominventory.loader;

import com.github.yungfasty.custominventory.holder.CustomInventoryHolder;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

/**
 * @author yungfasty
 * @version 1.0.0
 */
public class CustomInventoryLoader {

    /**
     * method used to init the system.
     * aka register and listen to listener
     * @param plugin your main instance
     */
    public static void init(Plugin plugin) {

        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            void onClick(InventoryClickEvent event) {

                event.setCancelled(event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof CustomInventoryHolder);

                if (!event.isCancelled()) return;

                if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

                val playerInventory = event.getClickedInventory().getClass().getSimpleName().contains("PlayerInventory");
                val customHolder = (CustomInventoryHolder) event.getInventory().getHolder();
                val slot = event.getSlot();

                if (customHolder.hasAction(slot)) customHolder.execute(event, slot);
                else if (playerInventory && customHolder.getForHolder().getDefaultActionPlayer() != null) customHolder.getForHolder().getDefaultActionPlayer().accept(event);
                else if (!playerInventory && customHolder.getDefaultAction() != null) customHolder.getDefaultAction().accept(event);

            }

        }, plugin);

        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            void onClick(InventoryCloseEvent event) {

                if (event.getInventory().getHolder() == null || !(event.getInventory().getHolder() instanceof CustomInventoryHolder)) return;

                val customHolder = (CustomInventoryHolder) event.getInventory().getHolder();

                customHolder.setForHolder(null);
                customHolder.close();

            }

        }, plugin);

    }

}
