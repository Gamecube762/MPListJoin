package com.github.Gamecube762.MPListJoin;

import org.spongepowered.api.event.SpongeEventHandler;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.SpongeInitializationEvent;
import org.spongepowered.api.event.state.SpongeServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;


/**
 * Created by Gamecube762 on 10/21/2014.
 */

@Plugin(id = "MPListJoin_Sponge", name = "MPListJoin", version = "1.0.2")
public class SpongeLoader {

    @SpongeEventHandler
    public void onInitialization(SpongeInitializationEvent event) {
        if (MPListJoin.serverType != null) {
            event.game.getLogger().info("MPListJoin was already loaded for " + MPListJoin.serverType.toString());
            return;
        }
        event.game.getLogger().info("MPListJoin is not ready for Sponge yet. Currently the plugin will do nothing.");
    }

    @SpongeEventHandler
    public void onServerStarting(ServerStartingEvent event) {}

    @SpongeEventHandler
    public void on(SpongeServerStoppingEvent event) {}

}
