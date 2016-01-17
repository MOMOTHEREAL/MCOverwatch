package me.momo.overwatch;

import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class OverwatchCommand
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (!(sender instanceof Player)) {
      return false;
    }
    if (args.length == 0) {
      return false;
    }
    String arg = args[0];
    if (arg.equalsIgnoreCase("start"))
    {
      if (OverwatchSession.getCurrent() != null) {
        return false;
      }
      OverwatchSession session = new OverwatchSession((Player)sender);
      session.setSuspect(sender.getName());
      session.startLocation = ((Player)sender).getLocation();
      session.record();
    }
    else if (arg.equalsIgnoreCase("stop"))
    {
      if (OverwatchSession.getCurrent() == null) {
        return false;
      }
      OverwatchRecord record = OverwatchSession.getCurrent().craft();
      OverwatchSession.getCurrent().end();
      
      OverwatchRecords.getRecords().add(record);
    }
    else if (arg.equalsIgnoreCase("info"))
    {
      if (args.length < 2) {
        return false;
      }
      String name = args[1];
      if (!OverwatchRecords.exists(name)) {
        return false;
      }
      sender.sendMessage("Demo ID: " + OverwatchRecords.getRecord(name).getName());
      sender.sendMessage("Demo Duration ticks (1/20 second): " + OverwatchRecords.getRecord(name).getDuration() + " (" + OverwatchRecords.getRecord(name).getDuration() / 20L + "s)");
      sender.sendMessage("Demo Date: " + OverwatchRecords.getRecord(name).getDate().toString());
      sender.sendMessage("Demo Events: " + OverwatchRecords.getRecord(name).getEvents().size());
    }
    else if (arg.equalsIgnoreCase("list"))
    {
      for (OverwatchRecord record : OverwatchRecords.getRecords())
      {
        sender.sendMessage("---------------------------------------------");
        sender.sendMessage("Demo ID: " + record.getName());
        sender.sendMessage("Demo Duration ticks (1/20 second): " + record.getDuration() + " (" + record.getDuration() / 20L + "s)");
        sender.sendMessage("Demo Date: " + record.getDate().toString());
        sender.sendMessage("Demo Events: " + record.getEvents().size());
        sender.sendMessage("---------------------------------------------");
      }
    }
    else if (arg.equalsIgnoreCase("play"))
    {
      if (args.length < 2) {
        return false;
      }
      String name = args[1];
      if (!OverwatchRecords.exists(name)) {
        return false;
      }
      OverwatchRecord record = OverwatchRecords.getRecord(name);
      OverwatchPlayThread play = new OverwatchPlayThread(record);
      int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Overwatch"), play, 1L, 1L);
      play.setTask(task);
    }
    return false;
  }
}
