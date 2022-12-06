package dailyrewards.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;

public class CpuMethod extends Methods {
    
    protected CpuMethod() {
        super();
    }

    @Override
    public void addPlayer(UUID uuid) {

        String uuidString = uuid.toString();
        int day = dataConfig.getInt(uuidString);

        if(day == 0){
            if(!dataConfig.contains(uuidString)){
                dataConfig.set(uuidString, 0);
            }
            return;
        }

        if(day < 1000){
            dataConfig.set(uuidString, day*1000);
        }
    }

    @Override
    public int getDay(UUID uuid) {
        int day = dataConfig.getInt(uuid.toString());
        return day >= 1000 ? day/1000 : day;
    }

    /*
     * Start runnable
     */
    @Override
    public void run() {

        File newDataFile = new File(folder+"/newData.yml");         
        File oldDataFile = new File(folder+"/data.yml"); 

        try {
            newDataFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(newDataFile));
            BufferedReader reader = new BufferedReader(new FileReader(oldDataFile));
            String line = reader.readLine();
            
            if (line == null) {
                save(writer, reader, newDataFile, oldDataFile);
                return;
            }

            while (line != null) {

                String[] split = line.split(": ");
                int day = Integer.parseInt(split[1]);
                
                line = reader.readLine();

                if(day >= 1000 || day == 0){
                    day++;
                    write(writer, split[0] + ": " + day);
                    
                } else {

                    day--;
                    if(day > 0){
                        write(writer, split[0] + ": " + day);   
                    }
                }
            }
            save(writer, reader, newDataFile, oldDataFile);

        } catch (IOException e) {
            System.err.println("Error executing the runnable of cpu Method: " + e);
        }
    }

    private void write(BufferedWriter writer, String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }

    private void save(BufferedWriter writer, BufferedReader reader, File newDataFile, File oldDataFile) throws IOException {
        newDataFile.renameTo(oldDataFile);
        dataConfig.save(oldDataFile);

        try {
            dataConfig.load(oldDataFile);
        } catch (InvalidConfigurationException e) {
            System.err.println("Please check and remove the data.yml configuration: " + e);
        }

        writer.close();
        reader.close();
    }
}