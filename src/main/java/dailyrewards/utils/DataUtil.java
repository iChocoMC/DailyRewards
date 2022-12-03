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
    
    //Guarda las uuids de los jugadores en String
    private static final Set<String> daysToChange = new HashSet<>();
    
    public DataUtil(){
        dataConfig = ConfigUtil.getData();
    }

    /*
     * Como utilizé un HashSet, no hace falta revisar si
     * la uuid del jugador ya se encuentra en este, porque el HashSet
     * no admite repeticiones. 
     * 
     * Metodo utilizado en la clase PlayerJoin.java
     */
    public static void addPlayer(String uuid){
        daysToChange.add(uuid);
    }

    /*
     * Devuelve la racha de días que lleva el jugador
     * 
     * Metodo utilizado en la clase PlayerInteract.java
     */
    public static int getDay(UUID uuid){
        int day = dataConfig.getInt(uuid.toString());
        return (day > 0) ? day : 0;
    }

    /*
     * Este runnable se ejecuta cada 24h.
     * 
     * Explicación básica:
     *  Crea un archivo newData.yml y ahi escribe todos los días de los jugadores
     *  actualizados, luego lo renombra a data.yml y listo.
     * 
     * Explicación detallada:
     *  Crea un archivo newData.yml, luego revisa si existe y si esto es verdad, lo elimina.
     *  Revisa cada linea del data.yml para luego pasarlas al archivo newData.yml.
     *  Cambiando los días de los jugadores, dependiendo de si entraron al servidor o no.
     *  Guarda los cambios y se acaba el runnable. 
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
                save(newDataFile, oldDataFile);
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

            save(newDataFile, oldDataFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Utilidad para no escribir tanto codigo, esto guarda renombra
     * el archivo newData.yml a data.yml y borra el contenido del hashmap.
     */
    private void save(File newDataFile, File oldDataFile) {
        newDataFile.renameTo(oldDataFile);
        daysToChange.clear();
        dataConfig = YamlConfiguration.loadConfiguration(newDataFile);
    }
}
