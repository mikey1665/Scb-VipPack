package mike1665.classpacks.throwables;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import Pauldg7.plugins.SCB.main.SCB;

public class ThrowItem
{
  private SCB scb;
  private ItemStack i;
  private ThrowReason t;
  private Player s;
  private Location l;
  private Vector v;
  private Boolean b;
  private Boolean p;
  public static HashMap<Integer, Double> shooterhealth = new HashMap<Integer, Double>();
  public static HashMap<Item, EntityType> eggs = new HashMap<Item, EntityType>();

  public ThrowItem(SCB scb, ItemStack i, ThrowReason t, Player s, Location l, Vector v, boolean b, boolean p) {
    this.scb = scb;
    this.i = i;
    this.t = t;
    this.s = s;
    this.l = l;
    this.v = v;
    this.b = Boolean.valueOf(b);
    this.p = Boolean.valueOf(p);
    throwItem();
  }

  public void throwItem()
  {
    this.l.setY(this.l.getY() - 0.5D);
    final Item itemdrop = this.l.getWorld().dropItem(this.l, this.i);
    itemdrop.setVelocity(this.v);
    itemdrop.setPickupDelay(2147483647);

    if (this.i.equals(new ItemStack(Material.REDSTONE_COMPARATOR)))
    {
      shooterhealth.put(Integer.valueOf(itemdrop.getEntityId()), Double.valueOf(this.s.getHealth()));
    }
    if ((this.i.getData() instanceof SpawnEgg))
    {
      EntityType segg = ((SpawnEgg)this.i.getData()).getSpawnedType();
      eggs.put(itemdrop, segg);
    }
    BukkitRunnable br = new BukkitRunnable()
    {
      public void run()
      {
        Location pos2;
        if (ThrowItem.this.b.booleanValue())
        {
          Location pos1 = itemdrop.getLocation().add(-0.25D, 0.25D, 0.25D);
          pos2 = itemdrop.getLocation().add(0.25D, -0.25D, -0.25D);
          if ((pos1.getBlock().getType() != Material.AIR) || (pos2.getBlock().getType() != Material.AIR))
          {
            if (ThrowItem.this.t.getEvent().onHitByBlock(itemdrop, ThrowItem.this.s))
            {
              cancel();
              itemdrop.remove();
            }
          }
        }
        if (ThrowItem.this.p.booleanValue())
        {
          for (Entity entity : itemdrop.getNearbyEntities(1.5D, 1.5D, 1.5D))
          {
            if (!entity.equals(ThrowItem.this.s))
            {
              if (ThrowItem.this.t.getEvent().onHitByEntity(entity, itemdrop, ThrowItem.this.s))
              {
                itemdrop.remove();
                cancel();
              }
            }
          }
        }
      }
    };
    br.runTaskTimer(this.scb, 1L, 1L);
  }

  public static HashMap<Integer, Double> getShooterHealth()
  {
    return shooterhealth;
  }

  public static HashMap<Item, EntityType> getEggs() {
    return eggs;
  }
}