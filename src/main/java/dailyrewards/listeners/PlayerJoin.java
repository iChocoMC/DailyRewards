package dailyrewards.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dailyrewards.utils.DataUtil;

public class PlayerJoin implements Listener {
    
    @EventHandler
    public void event(PlayerJoinEvent event){
        DataUtil.addPlayer(event.getPlayer().getUniqueId().toString());
    }
}
