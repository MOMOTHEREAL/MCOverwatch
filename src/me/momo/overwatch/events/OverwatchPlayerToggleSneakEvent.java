package me.momo.overwatch.events;

import me.momo.overwatch.OverwatchEvent;

public class OverwatchPlayerToggleSneakEvent
  implements OverwatchEvent
{
  private long tick;
  private boolean sneak;
  
  public OverwatchPlayerToggleSneakEvent(long tick, boolean sneak)
  {
    this.tick = tick;
    this.sneak = sneak;
  }
  
  public boolean isSneak()
  {
    return this.sneak;
  }
  
  public long tick()
  {
    return this.tick;
  }
}
