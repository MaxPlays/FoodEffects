package me.MaxPlays.FoodEffects.listeners;

import me.MaxPlays.FoodEffects.main.FoodEffects;
import me.MaxPlays.FoodEffects.util.Lists;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Max_Plays on 13.07.2017.
 */
public class EatListener implements Listener{

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e){
        Player p = e.getPlayer();
        ItemStack is = e.getItem();
        if(is != null && Lists.hasEffect(is)){
            final String item = is.getType().toString() + ":" + is.getDurability();
            new BukkitRunnable() {
                @Override
                public void run() {
                    ResultSet rs = FoodEffects.instance.sql.query("SELECT * FROM effects WHERE item='" + item + "';");
                    try {
                        if(rs.next()){
                            do{
                                final PotionEffectType type = PotionEffectType.getByName(rs.getString("effect"));
                                final int amp = rs.getInt("amplifier");
                                final int dur = rs.getInt("duration");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(p.hasPotionEffect(type))
                                            p.removePotionEffect(type);
                                        p.addPotionEffect(new PotionEffect(type, dur, amp));
                                    }
                                }.runTask(FoodEffects.instance);
                            }while (rs.next());
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(FoodEffects.instance);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) | e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getItem() != null && Lists.hasEffect(e.getItem()) && p.getFoodLevel() >= 20f && p.getGameMode() != GameMode.CREATIVE
                    && p.getGameMode() != GameMode.SPECTATOR){
                p.setFoodLevel(19);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(p.getFoodLevel() == 19)
                            p.setFoodLevel(20);
                    }
                }.runTaskLater(FoodEffects.instance, 1);
            }
        }
    }

}
