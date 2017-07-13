package me.MaxPlays.FoodEffects.util;

import me.MaxPlays.FoodEffects.main.FoodEffects;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Max_Plays on 13.07.2017.
 */
public class Lists {

    private static final Material[] food = new Material[]{Material.APPLE, Material.BAKED_POTATO, Material.BEETROOT, Material.BEETROOT_SOUP, Material.BREAD,
    Material.CARROT_ITEM, Material.CHORUS_FRUIT, Material.RAW_FISH, Material.COOKED_FISH, Material.COOKED_CHICKEN, Material.COOKED_MUTTON, Material.GRILLED_PORK,
    Material.COOKED_RABBIT, Material.COOKIE, Material.GOLDEN_APPLE, Material.GOLDEN_CARROT, Material.MELON, Material.MUSHROOM_SOUP, Material.POISONOUS_POTATO,
    Material.POTATO_ITEM, Material.PUMPKIN_PIE, Material.RABBIT_STEW, Material.RAW_BEEF, Material.RAW_CHICKEN, Material.MUTTON, Material.PORK, Material.RABBIT,
    Material.ROTTEN_FLESH, Material.SPIDER_EYE, Material.COOKED_BEEF};

    public static String effects;
    private static final ArrayList<Material> itemsWithEffects = new ArrayList<>();

    public Lists() {
        StringBuilder sb = new StringBuilder("§aAvailable effects: §7");
        for(int i = 1; i < PotionEffectType.values().length; i++){
            String type = PotionEffectType.values()[i].getName();
            if(i == (PotionEffectType.values().length - 1)){
                sb.append(type);
            }else{
                sb.append(type).append("§8,§7 ");
            }
        }
        effects = sb.toString();
        new BukkitRunnable() {
            @Override
            public void run() {
                itemsWithEffects.clear();
                ResultSet rs = FoodEffects.instance.sql.query("SELECT item FROM effects;");
                try {
                    while(rs.next()){
                        String s = rs.getString("item").split(":")[0];
                        if(!itemsWithEffects.contains(Material.valueOf(s)))
                            itemsWithEffects.add(Material.valueOf(s));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(FoodEffects.instance, 0, 20*60);
    }

    public static boolean isFood(ItemStack is){
        if(is == null)
            return false;
        for(Material m: food){
            if(is.getType().equals(m))
                return true;
        }
        return false;
    }
    public static boolean hasEffect(ItemStack is){
        return itemsWithEffects.contains(is.getType());
    }

    public static void addItem(ItemStack is){
        if(!itemsWithEffects.contains(is.getType()))
            itemsWithEffects.add(is.getType());
    }
}
