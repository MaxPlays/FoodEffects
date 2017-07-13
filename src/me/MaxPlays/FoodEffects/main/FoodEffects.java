package me.MaxPlays.FoodEffects.main;

import me.MaxPlays.FoodEffects.commands.CommandFoodEffects;
import me.MaxPlays.FoodEffects.listeners.EatListener;
import me.MaxPlays.FoodEffects.util.Lists;
import me.MaxPlays.FoodEffects.util.SQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Max_Plays on 12.07.2017.
 */
public class FoodEffects extends JavaPlugin{

    public static FoodEffects instance;
    public SQL sql;
    public static String prefix = "§8[§cFoodEffects§8] §7";

    public void onEnable() {
        instance = this;

        sql = new SQL("data", this);
        sql.connect();
        sql.update("CREATE TABLE IF NOT EXISTS effects(item VARCHAR(64), effect VARCHAR(64), amplifier INT, duration INT);");

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EatListener(), this);

        new Lists();
        getCommand("foodeffects").setExecutor(new CommandFoodEffects());
    }

    public void onDisable() {
        sql.disconnect();
    }
}
