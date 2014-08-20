MPListJoin
==========

Bukkit Plugin - See if a player has your server added to their multiplayer list!

[Bukkit Dev Page](http://dev.bukkit.org/bukkit-plugins/mplistjoin/)

How it works
==========
This plugin watches the IPs that pings the server, we grab and hold onto that IPs for the time in the config. When a player joins, we check the IPs being held and see if one matches the Player's IP. 

Since the server was pinged, we can tell that a client had the server in it's multiplayer list, from there, we just need a Player to join so we know who it was.

How to use it in your own plugin
=========================

Add this plugin as a Dependency or SoftDependency to your plugin.yml:
```
depend: [MPListJoin]
```


See if the plugin is loaded:
```
public static boolean isMPListJoinAdded() {
        return (Bukkit.getPluginManager().getPlugin("MPListJoin") != null);
    }
```

Then get the information your need:
```
Player player = somePlayer;
if (MPListJoin.playerUsedMPlist( player )) {
    player.sendMessage("Thank you for adding us to your favorites!");
}
```
Methods available are all Static, no need to save the plugin's instance.

|Method|Returns|
|  --- | --- |
|MPListJoin.getPlayersJoinedWith()| List< UUID > |
|MPListJoin.getHeldAddresses()|Set< InetAddress >
|MPListJoin.playerUsedMPlist(Player player)| boolean |
