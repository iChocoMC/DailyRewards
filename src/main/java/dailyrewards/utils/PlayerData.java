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

    /*
     * Si el jugador ha entrado por primera vez se le pondra como dia 1
     * En cambio si lleva más de 1 día y entra de vuelta, por ej 2.
     * Se cambiara ese 2 a 2000. Si el día es mayor a 1000, se podra entender
     * que el jugador ha entrado, en cambio, si este no entra, el dia sera 2.
     */

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

    /*
     * Si el jugador ha entrado al servidor por más de 1 día, el día sera mayor a 1000
     * y si esto es verdad se devolvera el día dividido 1000 para no causar errores
     */
    public static int getTime(String uuid){
        int day = dataConfig.getInt(uuid);
        if(day > 1000){
            return day/1000;
        }
        return day;
    }

    /*
     * Revisa todas las lineas y si el día del jugador es mayor a 1000
     * Se añadira 1 día más por entrar al servidor y el día pasara a day/1000.
     * 
     * En cambio, si el dia es menor a 1000, se entendera como que ese jugador
     * no ha entrado, entonces se le restara 1 día. Y si ese día es menor a 0, se borrará
     * toda información del data.yml
     */
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
