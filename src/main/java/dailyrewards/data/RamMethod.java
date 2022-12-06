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

import org.bukkit.configuration.InvalidConfigurationException;

public class RamMethod extends Methods {

    private final Set<String> daysToChange = new HashSet<>();

    protected RamMethod(){
        super();
    }

    @Override
    public void addPlayer(UUID uuid) {
        daysToChange.add(uuid.toString());
    }

    @Override
    public int getDay(UUID uuid) {
        return dataConfig.getInt(uuid.toString());
    }

    //Start Runnable

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

                for(String uuid : daysToChange){
                    writer.write(uuid + ": " + 1);
                    writer.newLine();
                }

                save(writer, reader, newDataFile, oldDataFile);                
                return;
            }

            while (line != null) {

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
            System.err.println("Error executing the runnable of ram Method: " + e);
        }
    }

    private void save(BufferedWriter writer, BufferedReader reader, File newDataFile, File oldDataFile) throws IOException {
        newDataFile.renameTo(oldDataFile);
        daysToChange.clear();

        try {
            dataConfig.load(newDataFile);
        } catch (InvalidConfigurationException e) {
            System.err.println("Please check and remove the data.yml configuration: " + e);
        }

        writer.close();
        reader.close();
    }
}
