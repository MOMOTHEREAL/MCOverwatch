package me.momo.overwatch.events;

import me.momo.overwatch.OverwatchEvent;
import org.bukkit.Location;

public class OverwatchPlayerLocation
  implements OverwatchEvent
{
  private long tick;
  private Location location;
  
  public OverwatchPlayerLocation(long tick, Location location)
  {
    this.tick = tick;
    this.location = location;
  }
  
  public long tick()
  {
    return this.tick;
  }
  
  public Location getLocation()
  {
    return this.location;
  }
}
