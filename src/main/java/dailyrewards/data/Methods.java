package dailyrewards.data;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import dailyrewards.DailyRewards;
import dailyrewards.utils.ConfigUtil;

public abstract class Methods implements Runnable {

    private static Methods method;

    protected final File folder;
    protected final FileConfiguration dataConfig;

    protected Methods() {
        this.dataConfig = ConfigUtil.getData();
        this.folder = DailyRewards.getInstance().getDataFolder();
    }
    
    public static void start() {

        if (ConfigUtil.getConfig().getBoolean("enable-cpu-mode")) {
            method = new CpuMethod();
        } else {
            method = new RamMethod();
        }

        int time = 86400 * 20;
        Bukkit.getScheduler().runTaskTimer(DailyRewards.getInstance(), method, time, time);
    }

    public static Methods getMethod() {
        return method;
    }

    //Abstract methods
    public abstract void addPlayer(UUID uuid);
    public abstract int getDay(UUID uuid);
}
