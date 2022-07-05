package com.github.yungfasty.custominventory.manager;

import com.github.yungfasty.custominventory.CustomInventory;
import com.google.common.collect.Maps;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

import java.util.Map;

public class InventoryManager {

    private final Map<String, CustomInventory> staticInventories = Maps.newHashMap();

    public @Nullable CustomInventory getStaticInventory(String identifier) {

        return staticInventories.getOrDefault(identifier, null);

    }

    public CustomInventory createStaticInventory(String identifier, String title, int size) {

        return staticInventories.put(identifier, new CustomInventory(title, size));

    }

}
