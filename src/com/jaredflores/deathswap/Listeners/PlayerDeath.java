package com.jaredflores.deathswap.Listeners;

import java.util.UUID;
import java.util.HashMap;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.jaredflores.deathswap.DeathSwap;
import com.jaredflores.deathswap.DSPlayer;

public class PlayerDeath implements Listener {
    
    @EventHandler
    public void onPlayerDeath( PlayerDeathEvent evt ) {
        if( DeathSwap.startedGame ) {
            Player player = evt.getEntity();
            if( DeathSwap.playerMap.containsKey( player.getUniqueId() ) ) {
                DSPlayer trappee = DeathSwap.playerMap.get( player.getUniqueId() );
                int points = trappee.getPoints() - 1;
                if( points < 0 ) {
                    points = 0;
                }
                trappee.setPoints( points );
                trappee.getPlayer().sendMessage( "You have died! Your new points: " + Integer.toString( points ) );
                if( !trappee.getNatural() ) {
                    trappee.setDeath( true );
                }
            }
        }
    }

}