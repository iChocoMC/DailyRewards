package dailyrewards.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dailyrewards.data.Methods;

public class PlayerJoin implements Listener {

    @EventHandler
    public void event(PlayerJoinEvent event) {
        Methods.getMethod().addPlayer(event.getPlayer().getUniqueId());
    }
}