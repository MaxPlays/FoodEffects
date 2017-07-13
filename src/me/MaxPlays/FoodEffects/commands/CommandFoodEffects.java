package me.MaxPlays.FoodEffects.commands;

import me.MaxPlays.FoodEffects.main.FoodEffects;
import me.MaxPlays.FoodEffects.util.Lists;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Max_Plays on 13.07.2017.
 */
public class CommandFoodEffects implements CommandExecutor{

    	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    			if (sender instanceof Player && cmd.getName().equalsIgnoreCase("foodeffects")) {
    				Player p = (Player) sender;
    				if(!p.hasPermission("foodeffects.admin")){
    				    sendHelp(p);
    				    return true;
    				}
    				if(args.length == 1){
                        if(args[0].equalsIgnoreCase("listEffects")){
                            p.sendMessage(Lists.effects);
                        }else if(args[0].equalsIgnoreCase("list")){
                            sendList(p, p.getInventory().getItemInMainHand());
                        }else{
                            sendHelp(p);
                        }
                    }else if(args.length == 2 && args[0].equalsIgnoreCase("remove")){
                        PotionEffectType type = PotionEffectType.getByName(args[1]);
    				    if(type != null){
                            removeEffect(p, p.getInventory().getItemInMainHand(), type);
                        }else{
    				        p.sendMessage(FoodEffects.prefix + "That effect does not exist. Use §c/foodeffects listEffects §7for a list of available effects");
                        }
                    }else if(args.length == 4 && args[0].equalsIgnoreCase("add")){
                        PotionEffectType type = PotionEffectType.getByName(args[1]);
                        if(type != null){
                            addEffect(p, p.getInventory().getItemInMainHand(), type, args[2], args[3]);
                        }else{
                            p.sendMessage(FoodEffects.prefix + "That effect does not exist. Use §c/foodeffects listEffects §7for a list of available effects");
                        }
                    }else{
                        sendHelp(p);
                    }
    			}
    			return true;
    		}

    private void sendHelp(Player p){
    	    p.sendMessage("§8------------- [§cFoodEffects§8] -------------");
    	    p.sendMessage("§7Plugin by §aMax_Plays");
    	    if(p.hasPermission("FoodEffects.admin")){
    	        p.sendMessage("§c/foodeffects help §7Print this help screen");
                p.sendMessage("");
    	        p.sendMessage("§c/foodeffects add <Effect> <Amplifier> <Duration in seconds> §7Add a potion effect to the food item in your hand");
                p.sendMessage("");
    	        p.sendMessage("§c/foodeffects remove <Effect> §7Remove a potion effect from the food item in your hand");
                p.sendMessage("");
    	        p.sendMessage("§c/foodeffects list §7List the potion effects that were added applied to the item in your hand");
                p.sendMessage("");
    	         p.sendMessage("§c/foodeffects listEffects §7List the available potion effects");
    	        p.sendMessage("");
    	        try{
                    TextComponent tc = new TextComponent("§aClick here for more help");
                    tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cClick to get to the GitHub page").create()));
                    tc.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, "https://github.com/MaxPlays/FoodEffects"));
                    p.spigot().sendMessage(tc);
                }catch(Exception e) {p.sendMessage("§aFor more help, visit§7 https://github.com/MaxPlays/FoodEffects"); }
            }
    }
    private void sendList(final Player p, ItemStack is){
        if(Lists.isFood(is)){
            final String s = is.getType().toString() + ":" + is.getDurability();
            new BukkitRunnable() {
                @Override
                public void run() {
                    ResultSet rs = FoodEffects.instance.sql.query("SELECT * FROM effects WHERE item='" + s + "';");
                    try {
                        if(rs.next()){
                            p.sendMessage(FoodEffects.prefix + "Showing assigned effects...");
                            do{
                                p.sendMessage(FoodEffects.prefix + "Effect: §c" + rs.getString("effect") + " §7Amplifier: §c" + rs.getInt("amplifier")
                                        + " §7Duration: §c" + (rs.getInt("duration") / 20) + " seconds");
                            }while(rs.next());
                        }else{
                            p.sendMessage(FoodEffects.prefix + "That item does not have any effects assigned to it");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(FoodEffects.instance);
        }else{
            p.sendMessage(FoodEffects.prefix + "You must hold a food item in your main hand");
        }
    }
    private void removeEffect(final Player p, final ItemStack is, final PotionEffectType type) {
        if(Lists.isFood(is)){
            final String s = is.getType().toString() + ":" + is.getDurability();
            new BukkitRunnable() {
                @Override
                public void run() {
                    ResultSet rs = FoodEffects.instance.sql.query("SELECT * FROM effects WHERE item='" + s + "' AND effect='" + type.getName() + "';");
                    try {
                        if(rs.next()){
                            FoodEffects.instance.sql.update("DELETE FROM effects WHERE item='" + s + "' AND effect='" + type.getName() + "';");
                            p.sendMessage(FoodEffects.prefix + "The effect §c" + type.getName() + " §7has been removed from this item");
                        }else{
                            p.sendMessage(FoodEffects.prefix + "That item does not have the specified effect added to it");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(FoodEffects.instance);
        }else{
            p.sendMessage(FoodEffects.prefix + "You must hold a food item in your main hand");
        }
    }
    private void addEffect(final Player p, final ItemStack is, final PotionEffectType type, String amp, String dur){
        if(Lists.isFood(is)){
            final String s = is.getType().toString() + ":" + is.getDurability();

            int ampl = 0, dura = 0;
            try{
                ampl = Integer.valueOf(amp);
                dura = Integer.valueOf(dur) * 20;
            }catch(Exception e){
                p.sendMessage(FoodEffects.prefix + "The numbers you entered are in an invalid format. Use §c/foodeffects help §7for help");
                return;
            }
            final int amplifier = ampl;
            final int duration = dura;
            if(amplifier <= 0 | amplifier > 255){
                p.sendMessage(FoodEffects.prefix + "The amplifier must be an integer between 1 and 255");
                return;
            }
            if(duration <= 0 | duration > (Integer.MAX_VALUE / 20)){
                p.sendMessage(FoodEffects.prefix + "The duration must be an integer between 1 and " + (Integer.MAX_VALUE / 20));
                return;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    ResultSet rs = FoodEffects.instance.sql.query("SELECT * FROM effects WHERE item='" + s + "' AND effect='" + type.getName() + "';");
                    try {
                        if(rs.next()){
                            p.sendMessage(FoodEffects.prefix + "That item has already the effect §c" + type.getName() + " §7assigned to it." +
                                    " Use §c/foodeffects remove <Effect> §7to remove the effect from the item.");
                        }else{
                            FoodEffects.instance.sql.update("INSERT INTO effects VALUES('" + s + "', '" + type.getName() + "', " + amplifier + ", " + duration + ");");
                            Lists.addItem(is);
                            p.sendMessage(FoodEffects.prefix + "The effect §c" + type.getName() + " §7was added to the item.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(FoodEffects.instance);
        }else{
            p.sendMessage(FoodEffects.prefix + "You must hold a food item in your main hand");
        }
    }

}
