package subside.plugins.koth2barapi;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

import subside.plugins.koth.adapter.KothAdapter;
import subside.plugins.koth.adapter.KothWrapper;

public class BarTimer extends BukkitRunnable {
    private Koth2BarAPI plugin;
    
    public BarTimer(Koth2BarAPI plugin) {
        this.plugin = plugin;
        removeBar();
    }

    @Override
    public void run() {
        try {
            @SuppressWarnings("unchecked")
            List<KothWrapper> adapter = (List<KothWrapper>) KothAdapter.getAdapter().getRunningKoths();

            KothWrapper koth;

            if (adapter.size() > 0) {
                koth = adapter.get(0);
                if (!koth.isRunning()) {
                    removeBar();
                    return;
                }
            } else {
                removeBar();
                return;
            }

            String msg = plugin.getMessage();
            Location loc = koth.getKoth().getMiddle();

            msg = msg.replaceAll("%area%", koth.getKoth().getName()).replaceAll("%player%", koth.getCapper()).replaceAll("%world%", loc.getWorld().getName());
            msg = msg.replaceAll("%x%", loc.getBlockX() + "").replaceAll("%y%", loc.getBlockY() + "").replaceAll("%z%", loc.getBlockZ() + "");
            msg = msg.replaceAll("%minutes%", koth.getTimeObject().getMinutesCapped() + "").replaceAll("%seconds%", koth.getTimeObject().getSecondsCapped() + "");
            msg = msg.replaceAll("%minutes_left%", koth.getTimeObject().getMinutesLeft() + "").replaceAll("%seconds_left%", koth.getTimeObject().getSecondsLeft() + "");

            float percentage = 100.0F;

            if (!koth.getCapper().equalsIgnoreCase("none")) {
                percentage = ((float)koth.getTimeObject().getTotalSecondsLeft() / (float)koth.getTimeObject().getLengthInSeconds()) * 100F;
            }

            setMessage(ChatColor.translateAlternateColorCodes('&', msg), percentage, koth.getTimeObject().getLengthInSeconds());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message, float percentage, int length) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(BossBarAPI.hasBar(player)){
                BossBar bar = BossBarAPI.getBossBar(player);
                bar.setMessage(message);
                bar.setHealth(percentage);
            } else {
                BossBarAPI.setMessage(player, message, percentage, Integer.MAX_VALUE);
            }
        }
    }

    public void removeBar() {
        Bukkit.getScheduler().cancelTasks(plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            BossBarAPI.removeBar(player);
        }
    }
}