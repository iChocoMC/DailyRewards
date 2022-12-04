package dailyrewards.data;

import java.util.UUID;
import dailyrewards.DailyRewards;

public abstract class Methods {

    private static Methods method;
    
    public static void start(){
        DailyRewards plugin = DailyRewards.getInstance();
        int time = 86400 * 20;

        if(plugin.getConfig().getBoolean("enable-cpu-mode")){
            CpuMethod cpuMethod = new CpuMethod();
            method = cpuMethod;   
            plugin.getServer().getScheduler().runTaskTimer(plugin, cpuMethod , time, time);
            return;
        }
        RamMethod ramMethod = new RamMethod();
        method = ramMethod;
        plugin.getServer().getScheduler().runTaskTimer(plugin, ramMethod , time, time);
    }

    public static Methods getMethod(){
        return method;
    }

    public abstract void addPlayer(UUID uuid);
    public abstract int getDay(UUID uuid);
}
