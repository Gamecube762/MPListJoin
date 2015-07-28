package com.github.Gamecube762.MPListJoin;

import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Gamecube762 on 10/21/2014.
 */
public class MPListJoin {

    protected static ServerType serverType;

    protected static List<UUID> playersJoinedWith = new ArrayList<UUID>();
    protected static Map<InetAddress, Long> heldAddresses = new ConcurrentHashMap<InetAddress, Long>();


    public static List<UUID> getPlayersJoinedWith() {
        return playersJoinedWith;
    }

    public static Map<InetAddress, Long> getHeldAddresses() {
        return heldAddresses;
    }

    @Deprecated//to be removed with Sponge or Trident release.
    public static boolean playerUsedMPlist(Player player) {
        return playersJoinedWith.contains(player.getUniqueId());
    }

    public static boolean playerUsedMPlist(UUID uuid) {
        return playersJoinedWith.contains(uuid);
    }


    public enum ServerType {
        BUKKIT,
        Sponge
    }
}
