package dailyrewards.data;

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
import dailyrewards.utils.ConfigUtil;

public class RamMethod extends Methods implements Runnable {

    private FileConfiguration dataConfig;
    private final Set<String> daysToChange = new HashSet<>();
    
    protected RamMethod(){
        dataConfig = ConfigUtil.getData();
    }

    @Override
    public void addPlayer(UUID uuid){
        daysToChange.add(uuid.toString());
    }

    @Override
    public int getDay(UUID uuid){
        return dataConfig.getInt(uuid.toString());
    }

    /*
     * Start runnable
     */
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

            BufferedWriter writer = new BufferedWriter(new FileWriter(newDataFile));
            BufferedReader reader = new BufferedReader(new FileReader(oldDataFile));
            
            String line = reader.readLine();

            if(line == null){
                for(String uuid : daysToChange){
                    writer.write(uuid + ": " + 1);
                    writer.newLine();
                }
                save(writer, reader, newDataFile, oldDataFile);                
                return;
            }

            while(line != null){

                String[] split = line.split(": ");
                int day = Integer.parseInt(split[1]);

                line = reader.readLine();

                if(daysToChange.contains(split[0])){
                    day++;
                    writer.write(split[0] + ": " + day);
                    writer.newLine();

                } else {
                    day--;
                    if(day > 0){
                        writer.write(split[0] + ": " + day);
                    }
                    writer.newLine();
                }
            }
            save(writer, reader, newDataFile, oldDataFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save(BufferedWriter writer, BufferedReader reader, File newDataFile, File oldDataFile) throws IOException {
        newDataFile.renameTo(oldDataFile);
        daysToChange.clear();
        dataConfig = YamlConfiguration.loadConfiguration(newDataFile);
        writer.close();
        reader.close();
    }
}
