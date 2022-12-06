package dailyrewards.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dailyrewards.DailyRewards;

public class ConfigUtil {

    private static final File folder = DailyRewards.getInstance().getDataFolder();
    private final FileConfiguration configuration;

    private ConfigUtil(String name) throws IOException {
        File newFile = new File(folder, name);

        if (!newFile.exists()) {
            newFile.createNewFile();
        }

        this.configuration = YamlConfiguration.loadConfiguration(newFile);
    }

    private FileConfiguration getFileConfig() {
        return this.configuration;
    }

    /*
     * Create files
     */

    private static FileConfiguration inventory;
    private static FileConfiguration data;
    private static FileConfiguration config;

    public static void startUtil() {

        try {

            inventory = new ConfigUtil("inventory.yml").getFileConfig();
            data = new ConfigUtil("data.yml").getFileConfig();
            config = new ConfigUtil("config.yml").getFileConfig();
        
        } catch (IOException e) {
            System.err.println("Error creating files: " + e);
        }
    }

    public static FileConfiguration getInventory() {
        return inventory;
    }

    public static FileConfiguration getData() {
        return data;
    }

    public static FileConfiguration getConfig() {
        return config;
    }
}