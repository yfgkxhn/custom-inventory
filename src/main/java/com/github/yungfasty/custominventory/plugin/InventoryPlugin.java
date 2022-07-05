package com.github.yungfasty.custominventory.plugin;

import com.github.yungfasty.custominventory.loader.CustomInventoryLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryPlugin extends JavaPlugin {

    public void onEnable() {

        CustomInventoryLoader.init(this);

    }

}
