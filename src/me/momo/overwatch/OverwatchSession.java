package me.momo.overwatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.momo.overwatch.events.OverwatchPlayerEquipment;
import me.momo.overwatch.events.OverwatchPlayerLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class OverwatchSession
{
  private static OverwatchSession current;
  private String suspect;
  private long tick;
  private List<OverwatchEvent> events = new ArrayList();
  public long duration;
  public Location startLocation;
  public UUID player;
  
  public OverwatchSession(Player player)
  {
    this.player = player.getUniqueId();
  }
  
  public OverwatchRecord craft()
  {
    this.duration = this.tick;
    String name = this.suspect + new Random().nextInt(1000);
    OverwatchRecord record = new OverwatchRecord(name);
    OverwatchEvent event;
    for (Iterator i$ = this.events.iterator(); i$.hasNext(); record.addEvent(event)) {
      event = (OverwatchEvent)i$.next();
    }
    record.setDuration(this.duration);
    record.setStartLocation(this.startLocation.clone());
    
    return record;
  }
  
  public void nextTick()
  {
    this.tick += 1L;
    
    Player p = Bukkit.getPlayer(this.player);
    Location l = p.getLocation().clone();
    
    add(new OverwatchPlayerLocation(this.tick, l.clone()));
    add(new OverwatchPlayerEquipment(this.tick, p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots(), p.getItemInHand()));
  }
  
  public long getTick()
  {
    return this.tick;
  }
  
  public void record()
  {
    setCurrent(this);
  }
  
  public void end()
  {
    this.duration = getTick();
    setCurrent(null);
  }
  
  public String getSuspect()
  {
    return this.suspect;
  }
  
  public void setSuspect(String suspect)
  {
    this.suspect = suspect;
  }
  
  public void add(OverwatchEvent event)
  {
    this.events.add(event);
  }
  
  public static void setCurrent(OverwatchSession current)
  {
    current = current;
  }
  
  public static OverwatchSession getCurrent()
  {
    return current;
  }
}
