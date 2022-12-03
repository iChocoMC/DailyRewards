package dailyrewards.utils;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dailyrewards.DailyRewards;

public class ConfigUtil {
  
    private FileConfiguration config;
    
    public ConfigUtil(File location, String name) {
        File file = new File(location, name);

        if (!file.exists()){
            DailyRewards.getInstance().saveResource(name, false); 
        }

        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
    }
  
    public FileConfiguration getConfig() {
        return this.config;
    }
  

    /*
    * Files:
    * config.yml, inventory.yml and data.yml
    */
    private static FileConfiguration inventory;
    private static FileConfiguration configuration;
    private static ConfigUtil playerData;

    public static void startUtil(File folder) {
        inventory = (new ConfigUtil(folder, "inventory.yml")).getConfig();
        configuration = DailyRewards.getInstance().getConfig();  
        playerData = new ConfigUtil(folder, "data.yml");
    }
  
    public static FileConfiguration getInventory() {
        return inventory;
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    public static FileConfiguration getData() {
        return playerData.getConfig();
    }
}
