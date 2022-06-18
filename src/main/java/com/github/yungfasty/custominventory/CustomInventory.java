package com.github.yungfasty.custominventory;

import com.github.yungfasty.custominventory.holder.CustomInventoryHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 * @author yungfasty
 * @version 1.0.0
 */
public class CustomInventory {

    @Getter private final ItemStack[] contents;
    @Getter private final HashMap<Integer, Consumer<InventoryClickEvent>> actions = new LinkedHashMap<>();
    @Getter @Setter private Consumer<InventoryClickEvent> defaultAction;
    @Getter private final String title;
    @Getter private final int size;

    public CustomInventory(String title, int size) {

        this.size = size;
        this.contents = new ItemStack[size];
        this.title = title;

    }

    public void addItem(Integer slot, ItemStack itemStack) {

        contents[slot] = itemStack;

    }

    public void addAction(Integer slot, Consumer<InventoryClickEvent> consumer) {

        actions.put(slot, consumer);

    }

    public Inventory build() {

        val customHolder = new CustomInventoryHolder(this);
        val inventory = Bukkit.createInventory(customHolder, size, title);

        inventory.setContents(contents);

        return inventory;

    }

}
