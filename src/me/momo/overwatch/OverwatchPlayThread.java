package me.momo.overwatch;

import java.io.PrintStream;
import java.util.Random;
import me.momo.overwatch.entity.Human;
import me.momo.overwatch.events.OverwatchPlayerEquipment;
import me.momo.overwatch.events.OverwatchPlayerInteractEvent;
import me.momo.overwatch.events.OverwatchPlayerLocation;
import me.momo.overwatch.events.OverwatchPlayerToggleSneakEvent;
import me.momo.overwatch.events.OverwatchPlayerToggleSprintEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitScheduler;

public class OverwatchPlayThread
  implements Runnable
{
  private long tick = 0L;
  private OverwatchRecord record;
  private Human suspect;
  private int task;
  
  public OverwatchPlayThread(OverwatchRecord record)
  {
    this.record = record;
    int id = new Random().nextInt(999999999);
    this.suspect = new Human(record.getStartLocation().clone().getWorld(), "S " + id, id, record.getStartLocation().clone(), 0);
    this.suspect.teleport(record.getStartLocation().clone());
    for (Player p : Bukkit.getOnlinePlayers()) {
      p.teleport(record.getStartLocation().clone());
    }
    System.out.println(record.getStartLocation().toString());
    System.out.println(record.getStartLocation().clone().toString());
  }
  
  public void run()
  {
    if (this.tick == this.record.getDuration())
    {
      Bukkit.getScheduler().cancelTask(this.task);
      this.suspect.remove();
      return;
    }
    for (OverwatchEvent event : this.record.getEvents()) {
      if (event.tick() == this.tick) {
        if ((event instanceof OverwatchPlayerInteractEvent))
        {
          OverwatchPlayerInteractEvent e = (OverwatchPlayerInteractEvent)event;
          if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
            this.suspect.swingArm();
          } else if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            this.suspect.block();
          }
        }
        else if ((event instanceof OverwatchPlayerLocation))
        {
          OverwatchPlayerLocation e = (OverwatchPlayerLocation)event;
          this.suspect.walk(e.getLocation().clone().getX(), e.getLocation().clone().getY(), e.getLocation().clone().getZ(), e.getLocation().clone().getYaw(), e.getLocation().clone().getPitch());
          this.suspect.teleport(e.getLocation().clone());
        }
        else if ((event instanceof OverwatchPlayerEquipment))
        {
          OverwatchPlayerEquipment e = (OverwatchPlayerEquipment)event;
          this.suspect.updateItems(e.hand, e.boots, e.legs, e.chest, e.helmet);
        }
        else if ((event instanceof OverwatchPlayerToggleSneakEvent))
        {
          OverwatchPlayerToggleSneakEvent e = (OverwatchPlayerToggleSneakEvent)event;
          if (e.isSneak()) {
            this.suspect.setCrouched();
          } else {
            this.suspect.reset();
          }
        }
        else if ((event instanceof OverwatchPlayerToggleSprintEvent))
        {
          OverwatchPlayerToggleSprintEvent e = (OverwatchPlayerToggleSprintEvent)event;
          if (e.isSprint()) {
            this.suspect.sprint();
          } else {
            this.suspect.reset();
          }
        }
      }
    }
    this.tick += 1L;
  }
  
  public void setTask(int task)
  {
    this.task = task;
  }
}
