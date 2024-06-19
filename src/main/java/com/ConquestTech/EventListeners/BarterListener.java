package com.ConquestTech.EventListeners;

import com.ConquestTech.Utilities.ItemBuilder;
import com.ConquestTech.Utilities.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BarterListener implements Listener {

    private static BarterListener Instance;


    public static BarterListener getInstance() {
        return Instance;
    }

    public static BarterListener init(JavaPlugin javaPlugin) {
        return new BarterListener(javaPlugin);
    }

    BarterListener(JavaPlugin javaPlugin) {
        javaPlugin.getServer().getPluginManager().registerEvents(this,javaPlugin);
        Instance = this;
    }

    @EventHandler
    public void onBarter(PiglinBarterEvent barterEvent) {

        if (Utils.RNG(0,100) >= 94) {
            barterEvent.getOutcome().add(new ItemBuilder(Material.ENDER_PEARL)
                    .setAmount(Utils.RNGInt(2,5)).build(false));
        }

    }

}
