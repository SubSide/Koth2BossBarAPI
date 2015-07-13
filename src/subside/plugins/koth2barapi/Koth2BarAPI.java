package subside.plugins.koth2barapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.bossbar.BossBarAPI;

import subside.plugins.koth.events.KothStartEvent;

public class Koth2BarAPI extends JavaPlugin implements Listener {
    private String message;
    @Override
    public void onEnable(){
        saveDefaultConfig();
        message = getConfig().getString("message");
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    @Override
    public void onDisable(){
        this.getServer().getScheduler().cancelTasks(this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            BossBarAPI.removeBar(player);
        }
    }
    
    public String getMessage(){
        return message;
    }
    

    @EventHandler
    public void onKothStart(KothStartEvent event){
        new BarTimer(this).runTaskTimer(this, 0, 20);
    }
}
