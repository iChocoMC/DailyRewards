package dailyrewards;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dailyrewards.commands.*;
import dailyrewards.data.Methods;
import dailyrewards.listeners.*;
import dailyrewards.utils.*;

public class DailyRewards extends JavaPlugin{

    @Override
    public void onEnable() {

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        ConfigUtil.startUtil();
        InventoryUtil.startUtil(ConfigUtil.getInventory());
        Methods.start();

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteract(ConfigUtil.getInventory()), this);
        pluginManager.registerEvents(new PlayerJoin(), this);
                
        this.getCommand("reward").setExecutor(new RewardCommand());
    }

    public static DailyRewards getInstance() {
        return getPlugin(DailyRewards.class);
    }
}