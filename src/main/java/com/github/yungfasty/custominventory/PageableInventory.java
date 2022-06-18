package com.github.yungfasty.custominventory;

import com.github.yungfasty.custominventory.holder.CustomInventoryHolder;
import com.github.yungfasty.custominventory.utils.ItemBuilder;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.function.BiConsumer;

public class PageableInventory<T> extends CustomInventory {

    @Getter private final List<T> items;
    @Getter @Setter private BiConsumer<T, ItemBuilder> itemConsumer;
    @Getter @Setter private int nextSlot, previousSlot;
    private final List<Integer> blacklisted = Lists.newArrayList();
    @Getter @Setter private Object memory;

    public PageableInventory(List<T> items, String title, int size) {

        super(title, size);

        this.items = items;

    }

    public PageableInventory(List<T> items, String title, String... pattern) {

        super(title, 9 * pattern.length);

        this.items = items;
        pattern(pattern);

    }

    public Inventory build(Integer page) {

        super.getContents()[nextSlot] = null;
        super.getContents()[previousSlot] = null;

        var index = getPerPageItems() * (page - 1);

        if (items.size() < index) return null;

        val inventory = Bukkit.createInventory(new CustomInventoryHolder(this), getSize(), getTitle() + " - #" + page);
        T value;

        if (hasNextPage(page))
            setNextPageIcon(ItemBuilder.of(Material.PAPER).name("§aPróxima página"), nextSlot);

        if (hasPreviousPage(page))
            setPreviousPageIcon(ItemBuilder.of(Material.PAPER).name("§aPágina anterior"), previousSlot);

        inventory.setContents(super.getContents());

        for (int slot = 0; slot < super.getSize(); slot++) {

            if (index >= items.size()) break;

            if (blacklisted.contains(slot)) continue;

            value = items.get(index++);

            var itemStack = ItemBuilder.of(Material.DIRT);

            itemConsumer.accept(value, itemStack);

            inventory.setItem(slot, itemStack.build());

        }

        return inventory;

    }

    /**
     * method used to verify if needs to create an next page item
     * @param currentPage current opened page
     * @return items size are more than the per page items multiplied by the current page
     */
    public boolean hasNextPage(Integer currentPage) {

        return items.size() > getPerPageItems() * currentPage;

    }

    /**
     * method used to verify if needs to create the back item
     * @param currentPage current opened page
     * @return current page are more than 1
     */
    public boolean hasPreviousPage(Integer currentPage) {

        return currentPage > 1;

    }

    /**
     * method used to calc how many items will be used on each page
     * @return how many slots isn't blacklisted.
     */
    private int getPerPageItems() {

        return getSize() - (blacklisted.size() - 2);

        /*
        var perPage = 0;

        for (int index = 0; index < super.getSize(); index++)
            if (!blacklisted.contains(index)) ++perPage;

        return perPage;

         */

    }

    public void setNextPageIcon(ItemBuilder itemEditor, int slot) {

        getContents()[slot] = itemEditor.build();

        addAction(slot, this::nextPage);

    }

    public void setPreviousPageIcon(ItemBuilder itemEditor, int slot) {

        getContents()[slot] = itemEditor.build();

        addAction(slot, this::previousPage);

    }

    private void nextPage(InventoryClickEvent event) {

        PageableInventory customInventory = (PageableInventory) ((CustomInventoryHolder) event.getInventory().getHolder()).getForHolder();
        int currentPage = Integer.parseInt(event.getInventory().getTitle().split("#")[1]);

        event.getWhoClicked().openInventory(customInventory.build(currentPage + 1));

        ((Player) event.getWhoClicked()).updateInventory();

    }

    private void previousPage(InventoryClickEvent event) {

        PageableInventory customInventory = (PageableInventory) ((CustomInventoryHolder) event.getInventory().getHolder()).getForHolder();
        int currentPage = Integer.parseInt(event.getInventory().getTitle().split("#")[1]);

        event.getWhoClicked().openInventory(customInventory.build(currentPage - 1));

        ((Player) event.getWhoClicked()).updateInventory();

    }

    public void pattern(String... pattern) {

        for (int index = 0; index < pattern.length; index++) {

            String string = pattern[index];

            for (int index2 = 0; index2 < string.length(); index2++) {

                char charAt = string.charAt(index2);

                if (charAt != '@')
                    blacklisted.add((9 * index) + index2);

                if (charAt == '<')
                    previousSlot = (9 * index) + index2;

                if (charAt == '>')
                    nextSlot = (9 * index) + index2;

            }

        }

    }

}