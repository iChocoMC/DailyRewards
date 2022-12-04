package dailyrewards.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtil {
    private static Inventory inventory;

    public static Inventory getInventory(){
        return inventory;
    }

    public static void startUtil(FileConfiguration config){

        int size = config.getInt("inventory-options.rows") * 9;

        //Create inventory
        inventory = Bukkit.createInventory(
            null,
            size,
            config.getString("inventory-options.tittle").replace('&', '§'));

        //Create the filler item
        if(config.getBoolean("filler-item.enable")){

            ItemStack filterItem = new ItemStack(Material.getMaterial(config.getString("filler-item.material")));
            filterItem.setDurability((short) config.getInt("filler-item.data"));

            for (int slot = 0; slot < size; slot++){
                inventory.setItem(slot, filterItem);
            }

        }

        String itemName = config.getString("inventory-options.item-name").replace('&', '§');

        //Create the items
        for (int day = 1; day < config.getInt("inventory-options.days")+1; day++) {
            ItemStack item = new ItemStack(Material.getMaterial(config.getString("_"+day+".material")));
            item.setAmount(day);

            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = new ArrayList<>();

            for (String line : config.getStringList("_"+day+".lore")){
                lore.add(line.replace('%', '&'));
            }

            itemMeta.setLore(lore);
            itemMeta.setDisplayName(itemName + day);
            item.setItemMeta(itemMeta);

            inventory.setItem(config.getInt("_"+day+".slot"), item);
        }
    }
}