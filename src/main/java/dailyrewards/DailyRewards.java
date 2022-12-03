package dailyrewards;

import java.io.File;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dailyrewards.commands.*;
import dailyrewards.listeners.*;
import dailyrewards.utils.*;

public class DailyRewards extends JavaPlugin{
    
    private static DailyRewards plugin;
    
    public void onEnable(){
        plugin = this;
        File folder = this.getDataFolder();

        if(!folder.exists()){
            folder.mkdir();
        }
        ConfigUtil.startUtil(folder);
        InventoryUtil.startUtil(ConfigUtil.getInventory());
        
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteract(ConfigUtil.getInventory()), this);
        pluginManager.registerEvents(new PlayerJoin(), this);
        
        int time = 86400 * 20;
        
        this.getServer().getScheduler().runTaskTimer(plugin, new DataUtil(), time, time);

        this.getCommand("reward").setExecutor(new RewardCommand());
    }

    public static DailyRewards getInstance(){
        return plugin;
    }
}