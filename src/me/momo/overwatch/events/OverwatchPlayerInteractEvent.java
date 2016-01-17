package me.momo.overwatch.events;

import me.momo.overwatch.OverwatchEvent;
import org.bukkit.event.block.Action;

public class OverwatchPlayerInteractEvent
  implements OverwatchEvent
{
  private long tick;
  private Action action;
  
  public OverwatchPlayerInteractEvent(long tick, Action action)
  {
    this.tick = tick;
    this.action = action;
  }
  
  public long tick()
  {
    return this.tick;
  }
  
  public Action getAction()
  {
    return this.action;
  }
}
