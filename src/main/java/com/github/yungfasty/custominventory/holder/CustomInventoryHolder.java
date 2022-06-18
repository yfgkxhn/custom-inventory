package com.github.yungfasty.custominventory.holder;

import com.github.yungfasty.custominventory.CustomInventory;
import com.github.yungfasty.custominventory.PageableInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author yungfasty
 * @version 1.0.0
 */
@AllArgsConstructor
public class CustomInventoryHolder implements InventoryHolder {

    /**
     * actions map
     */
    @Getter @Setter private CustomInventory forHolder;

    public Inventory getInventory() { return null; }

    /**
     * method used to check if has to execute an action to this slot
     * @param slot clicked slot
     * @return return false if actionsMap value to key slot is null
     */
    public boolean hasAction(Integer slot) { return Objects.nonNull(forHolder.getActions().getOrDefault(slot, null)); }

    /**
     * method used to retrieve an action from @actionsMap
     * @param slot clicked slot
     * @return action to be executed with this slot
     */
    public Consumer<InventoryClickEvent> getAction(Integer slot) { return forHolder.getActions().get(slot); }

    /**
     * method used to execute an action
     * @param event sender to execute
     * @param slot clicked slot
     */
    public void execute(InventoryClickEvent event, Integer slot) { getAction(slot).accept(event); }

    public Consumer<InventoryClickEvent> getDefaultAction() { return forHolder.getDefaultAction(); }

    public void close() {

        try { finalize(); }
        catch (Throwable exception) { exception.printStackTrace(); }

    }

}
