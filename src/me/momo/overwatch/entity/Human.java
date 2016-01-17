package me.momo.overwatch.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.PlayerInventory;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Human
{
  String name;
  World world;
  public int id;
  Location l;
  int itemInHand;
  UUID uuid;
  private List<Integer> ids = new ArrayList();
  
  private void setPrivateField(Class type, Object object, String name, Object value)
  {
    try
    {
      Field f = type.getDeclaredField(name);
      f.setAccessible(true);
      f.set(object, value);
      f.setAccessible(false);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public void setPitch(float pitch)
  {
    walk(0.0D, 0.0D, 0.0D, this.l.getYaw(), pitch);
  }
  
  public void setYaw(float yaw)
  {
    walk(0.0D, 0.0D, 0.0D, yaw, this.l.getPitch());
  }
  
  public Human(World w, String name, int id, Location l, int itemInHand)
  {
    this.name = name;
    this.world = w;
    this.id = id;
    this.l = l;
    this.itemInHand = itemInHand;
    this.uuid = UUID.randomUUID();
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn();
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "a", Integer.valueOf(id));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "b", new GameProfile(this.uuid, name));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "c", Integer.valueOf((int)l.getX() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "d", Integer.valueOf((int)l.getY() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "e", Integer.valueOf((int)l.getZ() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "f", Byte.valueOf(getCompressedAngle(l.getYaw())));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "g", Byte.valueOf(getCompressedAngle(l.getPitch())));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "h", Integer.valueOf(itemInHand));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "i", d);
    
    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", Integer.valueOf(id));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b", Integer.valueOf((int)l.getX() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c", Integer.valueOf((int)l.getY() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d", Integer.valueOf((int)l.getZ() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e", Byte.valueOf(getCompressedAngle(l.getYaw())));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f", Byte.valueOf(getCompressedAngle(l.getPitch())));
    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(spawn);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(tp);
    }
    this.ids.add(Integer.valueOf(id));
  }
  
  public Human(EntityHuman h)
  {
    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(h);
    int id = new Random().nextInt(4000) + 1000;
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "a", Integer.valueOf(id));
    this.id = id;
    this.uuid = h.getUniqueID();
    PacketPlayOutEntityEquipment armor1 = new PacketPlayOutEntityEquipment(id, 1, h.inventory.getArmorContents()[0]);
    PacketPlayOutEntityEquipment armor2 = new PacketPlayOutEntityEquipment(id, 2, h.inventory.getArmorContents()[1]);
    PacketPlayOutEntityEquipment armor3 = new PacketPlayOutEntityEquipment(id, 3, h.inventory.getArmorContents()[2]);
    PacketPlayOutEntityEquipment armor4 = new PacketPlayOutEntityEquipment(id, 4, h.inventory.getArmorContents()[3]);
    PacketPlayOutEntityEquipment sword = new PacketPlayOutEntityEquipment(id, 0, h.inventory.getItem(0));
    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(spawn);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(armor1);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(armor2);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(armor3);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(armor4);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(sword);
    }
  }
  
  public void teleport(Location loc)
  {
    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b", Integer.valueOf((int)(loc.getX() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c", Integer.valueOf((int)(loc.getY() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d", Integer.valueOf((int)(loc.getZ() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e", Byte.valueOf(getCompressedAngle(loc.getYaw())));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f", Byte.valueOf(getCompressedAngle(loc.getPitch())));
    this.l = loc;
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(tp);
    }
  }
  
  private byte getCompressedAngle(float value)
  {
    return (byte)(int)(value * 256.0F / 360.0F);
  }
  
  private byte getCompressedAngle2(float value)
  {
    return (byte)(int)(value * 256.0F / 360.0F);
  }
  
  public void remove()
  {
    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.id });
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }
  }
  
  public void updateItems(ItemStack hand, ItemStack boots, ItemStack legs, ItemStack chest, ItemStack helmet)
  {
    PacketPlayOutEntityEquipment[] ps = { new PacketPlayOutEntityEquipment(this.id, 1, CraftItemStack.asNMSCopy(boots)), new PacketPlayOutEntityEquipment(this.id, 2, CraftItemStack.asNMSCopy(legs)), new PacketPlayOutEntityEquipment(this.id, 3, CraftItemStack.asNMSCopy(chest)), new PacketPlayOutEntityEquipment(this.id, 4, CraftItemStack.asNMSCopy(helmet)), new PacketPlayOutEntityEquipment(this.id, 0, CraftItemStack.asNMSCopy(hand)) };
    for (PacketPlayOutEntityEquipment pack : ps) {
      for (Player p : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(pack);
      }
    }
  }
  
  @Deprecated
  public void setName(String s)
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    d.a(10, s);
    
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }
  
  public void hideForPlayer(Player p)
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)32));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, d, true);
    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
  }
  
  public void showForPlayer(Player p)
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, d, true);
    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
  }
  
  public void addPotionColor(Color r)
  {
    int color = r.asBGR();
    DataWatcher dw = new DataWatcher(null);
    dw.a(7, Integer.valueOf(color));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, dw, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }
  
  public void addPotionColor(int color)
  {
    DataWatcher dw = new DataWatcher(null);
    dw.a(7, Integer.valueOf(color));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, dw, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }
  
  public void walk(double a, double b, double c)
  {
    walk(a, b, c, this.l.getYaw(), this.l.getPitch());
  }
  
  public void walk(double a, double b, double c, float yaw, float pitch)
  {
    byte x = (byte)(int)a;
    byte y = (byte)(int)b;
    byte z = (byte)(int)c;
    PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook();
    setPrivateField(PacketPlayOutEntity.class, packet, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutEntity.class, packet, "b", Byte.valueOf(x));
    setPrivateField(PacketPlayOutEntity.class, packet, "c", Byte.valueOf(y));
    setPrivateField(PacketPlayOutEntity.class, packet, "d", Byte.valueOf(z));
    setPrivateField(PacketPlayOutEntity.class, packet, "e", Byte.valueOf(getCompressedAngle(yaw)));
    setPrivateField(PacketPlayOutEntity.class, packet, "f", Byte.valueOf(getCompressedAngle2(pitch)));
    
    PacketPlayOutEntityHeadRotation p2 = new PacketPlayOutEntityHeadRotation();
    setPrivateField(PacketPlayOutEntityHeadRotation.class, p2, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutEntityHeadRotation.class, p2, "b", Byte.valueOf(getCompressedAngle(yaw)));
    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(p2);
    }
    this.l.setPitch(pitch);
    this.l.setYaw(yaw);
    this.l.add(a, b, c);
  }
  
  public void sendtoplayer(Player who)
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn();
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "b", new GameProfile(this.uuid, this.name));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "c", Integer.valueOf((int)(this.l.getX() * 32.0D)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "d", Integer.valueOf((int)(this.l.getY() * 32.0D)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "e", Integer.valueOf((int)(this.l.getZ() * 32.0D)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "f", Byte.valueOf(getCompressedAngle(this.l.getYaw())));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "g", Byte.valueOf(getCompressedAngle(this.l.getPitch())));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "h", Integer.valueOf(this.itemInHand));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "i", d);
    
    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b", Integer.valueOf((int)(this.l.getX() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c", Integer.valueOf((int)(this.l.getY() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d", Integer.valueOf((int)(this.l.getZ() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e", Byte.valueOf(getCompressedAngle(this.l.getYaw())));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f", Byte.valueOf(getCompressedAngle(this.l.getPitch())));
    
    ((CraftPlayer)who).getHandle().playerConnection.sendPacket(spawn);
    ((CraftPlayer)who).getHandle().playerConnection.sendPacket(tp);
  }
  
  public void setInvisible()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)32));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }
  
  public void setCrouched()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)2));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }
  
  public void reset()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }
  
  public void sprint()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)8));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }
  
  public void block()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)16));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(this.id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }
  
  public void damage()
  {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", Integer.valueOf(2));
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet18);
    }
  }
  
  public void swingArm()
  {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", Integer.valueOf(0));
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet18);
    }
  }
  
  @Deprecated
  public void eatInHand()
  {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", Integer.valueOf(5));
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet18);
    }
  }
  
  public void sleep()
  {
    PacketPlayOutRelEntityMove packet17 = new PacketPlayOutRelEntityMove();
    setPrivateField(PacketPlayOutRelEntityMove.class, packet17, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutRelEntityMove.class, packet17, "b", Integer.valueOf((int)getX()));
    setPrivateField(PacketPlayOutRelEntityMove.class, packet17, "c", Integer.valueOf((int)getY()));
    setPrivateField(PacketPlayOutRelEntityMove.class, packet17, "d", Integer.valueOf((int)getZ()));
    setPrivateField(PacketPlayOutRelEntityMove.class, packet17, "e", Integer.valueOf(0));
    for (Player p : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet17);
    }
  }
  
  public double getX()
  {
    return this.l.getX();
  }
  
  public double getY()
  {
    return this.l.getY();
  }
  
  public double getZ()
  {
    return this.l.getZ();
  }
  
  public Location getLocation()
  {
    return this.l;
  }
}
