# FoodEffects
Bukkit plugin that gives potion effects to food

## Installation ##
To install this plugin, download the latest release here and drop it into your plugins folder. Start the server normally. This was written for Spigot 1.11

## Commands ##
This plugin has one simple command: **/FoodEffects**. The sub-commands can be seen using **/FoodEffects help**. There is also a short form
of the command: **/fe**.

### Add potion effects to an item ###
**/fe add <Effect> <Amplifier> <Duration>**
<Effect> is the potion effect (/fe listEffects)
<Amplifier> is the default minecraft amplifier
<Duration> is the time the potion effect will last in seconds
**You can also use the <TAB> button on your keyboard to easily select the desired sub-command/potion effect**

### Remove potion effects from an item ###
**/fe remove <Effect>**
<Effect> is the potion effect (/fe listEffects)

### List active potion effects ###
**/fe list**
Lists the active potion effects of an item

### List available potion effects ###
**/fe listEffects**
Lists all available potion effects

## Permissions ##
There is only one permission: **FoodEffects.admin**
This allows the user to use the /FoodEffect command and all it's sub-commands
