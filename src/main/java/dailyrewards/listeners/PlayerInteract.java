package dailyrewards.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import dailyrewards.utils.ConfigUtil;
import dailyrewards.utils.InventoryUtil;
import dailyrewards.utils.PlayerData;

public class PlayerInteract implements Listener {
    
    private final Material filterItem;

    public PlayerInteract(FileConfiguration config){
        filterItem = Material.getMaterial(config.getString("filler-item.material"));
    }

    @EventHandler
    public void event(InventoryClickEvent event){

        if(!event.getInventory().equals(InventoryUtil.getInventory())){
            return;
        }

        event.setCancelled(true);

        ItemStack item = InventoryUtil.getInventory().getItem(event.getSlot());
        
        if(item.getType() == filterItem){
            return;
        }

        Player player = (Player)event.getWhoClicked();
        int day = item.getAmount();
        int playerDay = PlayerData.getTime(player.getUniqueId().toString());

        if(playerDay < day){
            player.sendMessage("Current day:" + playerDay + "\nRequeried days: " + day);
            return;
        }

        for (String command : ConfigUtil.getInventory().getStringList("_"+day+".execute")) {

            if(command.length() > 0){

                Bukkit.getServer().dispatchCommand(
                    Bukkit.getConsoleSender(),
                    command.replace("%player%", player.getDisplayName()));
            }
        }
    }
}
