package dailyrewards.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import dailyrewards.data.Methods;
import dailyrewards.utils.ConfigUtil;
import dailyrewards.utils.InventoryUtil;

public class PlayerInteract implements Listener {

    private final Material filterItem;
    private final String message;

    public PlayerInteract(FileConfiguration config) {
        filterItem = Material.getMaterial(config.getString("filler-item.material"));
        message = ConfigUtil.getConfig().getString("insufficient-days").replace('&', '§');
    }

    @EventHandler
    public void event(InventoryClickEvent event) {

        if(!event.getInventory().equals(InventoryUtil.getInventory())){
            return;
        }

        event.setCancelled(true);

        ItemStack item = InventoryUtil.getInventory().getItem(event.getSlot());

        if (item.getType() == filterItem)  {
            return;
        }

        Player player = (Player)event.getWhoClicked();
        int day = item.getAmount();
        int playerDay = Methods.getMethod().getDay(player.getUniqueId());

        if (playerDay < day) {

            player.sendMessage(message
                .replace("%days%", ""+playerDay)
                .replace("%requeried_days%", ""+day));
            return;

        }

        //Execute all commands
        for (String command : ConfigUtil.getInventory().getStringList("_"+day+".execute")) {

            Bukkit.getServer().dispatchCommand(
                Bukkit.getConsoleSender(),
                command.replace("%player%", player.getDisplayName()));
        }
    }
}
