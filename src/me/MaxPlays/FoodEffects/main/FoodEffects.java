package me.MaxPlays.FoodEffects.main;

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

    public void onEnable() {
        instance = this;

        sql = new SQL("data", this);
        sql.connect();
        sql.update("CREATE TABLE IF NOT EXISTS effects(item VARCHAR(64), effect VARCHAR(64), amplifier INT, duration INT);");

        PluginManager pm = Bukkit.getPluginManager();

    }

}
