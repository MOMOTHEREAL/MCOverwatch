package me.momo.overwatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.bukkit.Location;

public class OverwatchRecord
{
  private String name;
  private Date date;
  private List<OverwatchEvent> events = new ArrayList();
  private long duration;
  private Location startLocation;
  
  public OverwatchRecord(String name)
  {
    this.name = name;
    this.date = Calendar.getInstance().getTime();
  }
  
  public void addEvent(OverwatchEvent event)
  {
    getEvents().add(event);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Date getDate()
  {
    return this.date;
  }
  
  public List<OverwatchEvent> getEvents()
  {
    return this.events;
  }
  
  public long getDuration()
  {
    return this.duration;
  }
  
  public void setDuration(long duration)
  {
    this.duration = duration;
  }
  
  public void setStartLocation(Location startLocation)
  {
    this.startLocation = startLocation;
  }
  
  public Location getStartLocation()
  {
    return this.startLocation;
  }
}
