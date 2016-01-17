package me.momo.overwatch;

import java.util.ArrayList;
import java.util.List;

public class OverwatchRecords
{
  private static List<OverwatchRecord> records = new ArrayList();
  
  public static OverwatchRecord getRecord(String name)
  {
    for (OverwatchRecord record : records) {
      if (record.getName().equalsIgnoreCase(name)) {
        return record;
      }
    }
    return null;
  }
  
  public static List<OverwatchRecord> getRecords()
  {
    return records;
  }
  
  public static boolean exists(String name)
  {
    return getRecord(name) != null;
  }
}
