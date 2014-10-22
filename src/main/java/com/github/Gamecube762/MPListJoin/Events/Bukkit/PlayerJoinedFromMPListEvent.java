package com.github.Gamecube762.MPListJoin.Events.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Created by Gamecube762 on 8/26/14.
 */
public class PlayerJoinedFromMPListEvent extends PlayerEvent{
    private static final HandlerList handlers = new HandlerList();

    public PlayerJoinedFromMPListEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
