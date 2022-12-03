package dailyrewards.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dailyrewards.utils.DataUtil;
import dailyrewards.utils.InventoryUtil;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player)sender;

        player.openInventory(InventoryUtil.getInventory());
        player.sendMessage("" + DataUtil.daysToChange.size());
        player.sendMessage(player.getUniqueId().toString());
        return false;
    }
}

