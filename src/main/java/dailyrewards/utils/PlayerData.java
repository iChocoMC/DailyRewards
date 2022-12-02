package dailyrewards.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

public class PlayerData implements Runnable {

    private static FileConfiguration dataConfig;

    public static void start(){
        dataConfig = ConfigUtil.getData();
    }

    public static void set(String uuid, int day){
        try (
            BufferedWriter bw = new BufferedWriter(new FileWriter(ConfigUtil.getDataFile(), true));
        ) {
            bw.write(uuid + ": " + day);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPlayer(String uuid){
        int day = dataConfig.getInt(uuid);

        if(day == 0){
            set(uuid, 1);
            return;
        }

        if(day != 1 && day < 1000){
            set(uuid, day*1000);
        }
    }

    public static int getTime(String uuid){
        int day = dataConfig.getInt(uuid);
        if(day > 1000){
            return day/1000;
        }
        return day;
    }

    @Override
    public void run() {

        try(
            BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigUtil.getDataFile()));
            BufferedReader reader = new BufferedReader(new FileReader(ConfigUtil.getDataFile()));
        ) {

            String line = "";
            while((line = reader.readLine()) != null){

                String[] lines = line.split(": ");
                int day = Integer.parseInt(lines[1]);

                if(day >= 1000){
                    day = (day/1000)+1;
                    writer.write(lines[0] + ":" + day);
                    return;
                }
                
                day--;
                if(day > 0){
                    writer.write(lines[0] + ":" + day);
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
