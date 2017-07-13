package me.MaxPlays.FoodEffects.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Max_Plays on 13.07.2017.
 */
public class CommandFoodEffectsCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 1){
            String[] operations = new String[]{"add", "remove", "list", "listEffects", "help"};
            List<String> list = new ArrayList<>();
            for(String s: operations){
                if(s.toLowerCase().startsWith(args[0].toLowerCase()))
                    list.add(s);
            }
            return list;
        }else if(args.length == 2 && (args[0].equalsIgnoreCase("add") | args[0].equalsIgnoreCase("remove"))){
            List<String> list = new ArrayList<>();
            for(int i = 1; i < PotionEffectType.values().length; i++){
                String type = PotionEffectType.values()[i].getName();
                if(type.startsWith(args[1].toUpperCase()))
                    list.add(type);
            }
            return list;
        }

        return Arrays.asList("");
    }
}
