package com.ConquestTech;

import com.ConquestTech.EventListeners.BarterListener;
import org.bukkit.plugin.java.JavaPlugin;

public class PiglinCore extends JavaPlugin {

    @Override
    public void onEnable() {
        registerListeners();
    }

    void registerListeners() {
        BarterListener.init(this);
    }

}
