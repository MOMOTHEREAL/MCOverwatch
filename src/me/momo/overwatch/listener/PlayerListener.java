package me.momo.overwatch.listener;

import me.momo.overwatch.OverwatchSession;
import me.momo.overwatch.events.OverwatchPlayerInteractEvent;
import me.momo.overwatch.events.OverwatchPlayerToggleSneakEvent;
import me.momo.overwatch.events.OverwatchPlayerToggleSprintEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class PlayerListener
  implements Listener
{
  @EventHandler
  public void onInteract(PlayerInteractEvent event)
  {
    if (OverwatchSession.getCurrent() == null) {
      return;
    }
    long tick = OverwatchSession.getCurrent().getTick();
    OverwatchSession.getCurrent().add(new OverwatchPlayerInteractEvent(tick, event.getAction()));
  }
  
  @EventHandler
  public void onSneak(PlayerToggleSneakEvent event)
  {
    if (OverwatchSession.getCurrent() == null) {
      return;
    }
    long tick = OverwatchSession.getCurrent().getTick();
    OverwatchSession.getCurrent().add(new OverwatchPlayerToggleSneakEvent(tick, event.isSneaking()));
  }
  
  @EventHandler
  public void onSprint(PlayerToggleSprintEvent event)
  {
    if (OverwatchSession.getCurrent() == null) {
      return;
    }
    long tick = OverwatchSession.getCurrent().getTick();
    OverwatchSession.getCurrent().add(new OverwatchPlayerToggleSprintEvent(tick, event.isSprinting()));
  }
}
