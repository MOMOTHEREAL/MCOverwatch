package me.momo.overwatch;

import me.momo.overwatch.listener.PlayerListener;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class OverwatchPlugin
  extends JavaPlugin
{
  public void onEnable()
  {
    getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    getServer().getScheduler().runTaskTimer(this, new OverwatchRecordThread(), 1L, 1L);
    getCommand("overwatch").setExecutor(new OverwatchCommand());
  }
}
