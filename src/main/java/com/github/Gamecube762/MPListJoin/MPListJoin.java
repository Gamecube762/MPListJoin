package com.github.Gamecube762.MPListJoin;

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
import java.util.*;

/**
 * Created by Gamecube762 on 8/19/14.
 */
public class MPListJoin extends JavaPlugin implements Listener{

    private long holdLength = 5;
    private boolean tell_servPing = false, tell_MP_playJoin = false, onlyMP = true;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        holdLength = getConfig().getLong("IPHoldLength");
        tell_servPing = getConfig().getBoolean("TellConsole.ServerPingIP");
        tell_MP_playJoin = getConfig().getBoolean("TellConsole.PlayerJoinMP");
        onlyMP = getConfig().getBoolean("OnlySendMessageIfJoinedByMP");

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        playersJoinedWith.clear();
        heldAddresses.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            Player player = Bukkit.getPlayer( args[0] );

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Could not find Player \"" + args[0] + "\", they might not be online!");
                return true;
            }

            sender.sendMessage(
                player.getDisplayName() +
                ( ( playerUsedMPlist(player) ) ? " logged in from " : " wasn't found using " ) +
                "the Multi-player list!"
            );

        } catch (ArrayIndexOutOfBoundsException e) {return false;}
        return true;
    }

    @EventHandler
    public void onServerListPing(final ServerListPingEvent e) {
        heldAddresses.add(e.getAddress());

        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                if (heldAddresses.contains( e.getAddress() ))
                    heldAddresses.remove( e.getAddress() );
            }
        }, holdLength * 60 * 20);

        if (tell_MP_playJoin) getLogger().info( e.getAddress() + " Pinged the server!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if ( heldAddresses.contains( e.getPlayer().getAddress().getAddress() ) ) {
            heldAddresses.remove( e.getPlayer().getAddress().getAddress() );
            playersJoinedWith.add( e.getPlayer().getUniqueId() );
        }

        for (Player p : Bukkit.getOnlinePlayers())
            if (p.hasPermission("mplistjoin.telljoin"))
                p.sendMessage( e.getPlayer().getDisplayName() + " logged in from the multi-player list");

        if (tell_MP_playJoin && playerUsedMPlist( e.getPlayer() ))
            getLogger().info(e.getPlayer().getDisplayName() + " Joined with the multi-player list!");

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if ( playersJoinedWith.contains( e.getPlayer().getUniqueId() ) )
            playersJoinedWith.remove( e.getPlayer().getUniqueId() );
    }


    // ========== statics ==========

    private static List<UUID> playersJoinedWith = new ArrayList<UUID>();
    private static Set<InetAddress> heldAddresses = new HashSet<InetAddress>();

    public static List<UUID> getPlayersJoinedWith() {
        return playersJoinedWith;
    }

    public static Set<InetAddress> getHeldAddresses() {
        return heldAddresses;
    }

    public static boolean playerUsedMPlist(Player player) {
        return playersJoinedWith.contains( player.getUniqueId() );
    }
}
