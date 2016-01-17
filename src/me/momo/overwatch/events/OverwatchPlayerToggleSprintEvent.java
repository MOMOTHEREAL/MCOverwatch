package me.momo.overwatch.events;

import me.momo.overwatch.OverwatchEvent;

public class OverwatchPlayerToggleSprintEvent
  implements OverwatchEvent
{
  private long tick;
  private boolean sprint;
  
  public OverwatchPlayerToggleSprintEvent(long tick, boolean sprint)
  {
    this.tick = tick;
    this.sprint = sprint;
  }
  
  public long tick()
  {
    return this.tick;
  }
  
  public boolean isSprint()
  {
    return this.sprint;
  }
}
