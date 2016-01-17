package me.momo.overwatch;

public class OverwatchRecordThread
  implements Runnable
{
  public void run()
  {
    if (OverwatchSession.getCurrent() != null) {
      OverwatchSession.getCurrent().nextTick();
    }
  }
}
