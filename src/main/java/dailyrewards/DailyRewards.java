package dailyrewards;

import java.io.File;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dailyrewards.commands.*;
import dailyrewards.data.Methods;
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

        saveDefaultConfig();

        ConfigUtil.startUtil(folder);
        InventoryUtil.startUtil(ConfigUtil.getInventory());
        Methods.start();

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteract(ConfigUtil.getInventory()), this);
        pluginManager.registerEvents(new PlayerJoin(), this);
                
        this.getCommand("reward").setExecutor(new RewardCommand());
    }

    public static DailyRewards getInstance(){
        return plugin;
    }
}