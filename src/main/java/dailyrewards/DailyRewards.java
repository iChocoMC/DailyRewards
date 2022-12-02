package dailyrewards;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dailyrewards.commands.*;
import dailyrewards.listeners.PlayerInteract;
import dailyrewards.listeners.PlayerJoin;
import dailyrewards.utils.ConfigUtil;
import dailyrewards.utils.InventoryUtil;
import dailyrewards.utils.PlayerData;

public class DailyRewards extends JavaPlugin{
    
    private static DailyRewards plugin;
    
    public void onEnable(){
        plugin = this;

        saveDefaultConfig();

        ConfigUtil.startUtil(this.getDataFolder());
        InventoryUtil.startUtil(ConfigUtil.getInventory());
        PlayerData.start();
        
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteract(ConfigUtil.getInventory()), this);
        pluginManager.registerEvents(new PlayerJoin(), this);
        
        int time = 86400*20;
        this.getServer().getScheduler().runTaskTimer(plugin, new PlayerData(), time, time);

        this.getCommand("test").setExecutor(new TestCommand());
    }

    public static DailyRewards getInstance(){
        return plugin;
    }
}