package me.momo.overwatch.events;

import me.momo.overwatch.OverwatchEvent;
import org.bukkit.inventory.ItemStack;

public class OverwatchPlayerEquipment
  implements OverwatchEvent
{
  private long tick;
  public ItemStack helmet;
  public ItemStack chest;
  public ItemStack legs;
  public ItemStack boots;
  public ItemStack hand;
  
  public OverwatchPlayerEquipment(long tick, ItemStack helmet, ItemStack chest, ItemStack legs, ItemStack boots, ItemStack hand)
  {
    this.tick = tick;
    this.helmet = helmet;
    this.chest = chest;
    this.legs = legs;
    this.boots = boots;
    this.hand = hand;
  }
  
  public long tick()
  {
    return this.tick;
  }
}
