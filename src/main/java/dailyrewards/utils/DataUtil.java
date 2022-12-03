package dailyrewards.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dailyrewards.DailyRewards;

public class DataUtil implements Runnable {

    private static FileConfiguration dataConfig;
    public static final Set<String> daysToChange = new HashSet<>();

    public DataUtil(){
        dataConfig = ConfigUtil.getData();
    }

    public static void addPlayer(String uuid){
        daysToChange.add(uuid);
    }

    /*
     * Devuelve la racha de días que lleva el jugador
     */
    public static int getDay(UUID uuid){
        int day = dataConfig.getInt(uuid.toString());
        return (day > 0) ? day : 0;
    }

    @Override
    public void run() {
        File folder = DailyRewards.getInstance().getDataFolder();
        File newDataFile = new File(folder+"/newData.yml");         
        File oldDataFile = new File(folder+"/data.yml"); 

        if(newDataFile.exists()){
            newDataFile.delete();
        }

        try {
            newDataFile.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try (
            BufferedWriter writer = new BufferedWriter(new FileWriter(newDataFile));
            BufferedReader reader = new BufferedReader(new FileReader(oldDataFile));
        ){
            String line = reader.readLine();

            if(line == null){
                for(String uuid : daysToChange){
                    writer.write(uuid + ": " + 1);
                    writer.newLine();
                }
                newDataFile.renameTo(oldDataFile);
                daysToChange.clear();
                dataConfig = YamlConfiguration.loadConfiguration(newDataFile);
                return;
            }

            while(line != null){

                String[] split = line.split(": ");
                int day = Integer.parseInt(split[1]);

                if(!daysToChange.contains(split[0])){
                    day--;
                    if(day > 0){
                        writer.write(split[0] + ": " + day);
                    }
                    
                    line = reader.readLine();
                    writer.newLine();
                    return;
                }
                day++;
                writer.write(split[0] + ": " + day);
                line = reader.readLine();
                writer.newLine();
            }

            newDataFile.renameTo(oldDataFile);
            daysToChange.clear();

            dataConfig = YamlConfiguration.loadConfiguration(newDataFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
