package com.github.Gamecube762.MPListJoin;

import com.github.Gamecube762.MPListJoin.Events.Bukkit.PlayerJoinedFromMPListEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;

/**
 * Created by Gamecube762 on 10/21/2014.
 */
public class BukkitLoader extends JavaPlugin implements Listener {

    private long holdLength = 5;
    private boolean tell_servPing = false, tell_MP_playJoin = false;

    @Override
    public void onEnable() {
        if (MPListJoin.serverType != null) {
            getLogger().severe("Plugin was already loaded for " + MPListJoin.serverType.toString());

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        holdLength = getConfig().getLong("IPHoldLength");
        tell_servPing = getConfig().getBoolean("TellConsole.ServerPingIP");
        tell_MP_playJoin = getConfig().getBoolean("TellConsole.PlayerJoinMP");

        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                for (InetAddress a : MPListJoin.heldAddresses.keySet())
                    if (MPListJoin.heldAddresses.get(a) <= 1)
                        MPListJoin.heldAddresses.remove(a);
                    else
                        MPListJoin.heldAddresses.put(a, MPListJoin.heldAddresses.get(a)-1);
            }
        }, 1, 20);
    }

    @Override
    public void onDisable() {
        MPListJoin.playersJoinedWith.clear();
        MPListJoin.heldAddresses.clear();

        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            Player player = Bukkit.getPlayer( args[0] );

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Could not find Player \"" + args[0] + "\", they may not be online!");
                return true;
            }

            sender.sendMessage(
                    player.getDisplayName() +
                            ( ( MPListJoin.playerUsedMPlist(player) ) ? " logged in from " : " wasn't found using " ) +
                            "the Multi-player list!"
            );

        } catch (ArrayIndexOutOfBoundsException e) {return false;}
        return true;
    }

    @EventHandler
    public void onServerListPing(final ServerListPingEvent e) {
        MPListJoin.heldAddresses.put(e.getAddress(), holdLength * 60);

        if (tell_servPing) getLogger().info( e.getAddress() + " Pinged the server!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!MPListJoin.heldAddresses.containsKey( e.getPlayer().getAddress().getAddress() ) ) return;

        MPListJoin.heldAddresses.remove( e.getPlayer().getAddress().getAddress() );
        MPListJoin.playersJoinedWith.add( e.getPlayer().getUniqueId() );

        for (Player p : Bukkit.getOnlinePlayers())
            if (p.hasPermission("mplistjoin.telljoin"))
                p.sendMessage( e.getPlayer().getDisplayName() + " logged in from the multi-player list");

        if (tell_MP_playJoin && MPListJoin.playerUsedMPlist(e.getPlayer()))
            getLogger().info(e.getPlayer().getDisplayName() + " Joined with the multi-player list!");

        Bukkit.getPluginManager().callEvent( new PlayerJoinedFromMPListEvent(e.getPlayer()) );

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if ( MPListJoin.playersJoinedWith.contains(e.getPlayer().getUniqueId()) )
            MPListJoin.playersJoinedWith.remove(e.getPlayer().getUniqueId());
    }
}
