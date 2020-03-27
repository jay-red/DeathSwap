package com.jaredflores.deathswap;

import java.util.UUID;
import java.util.ArrayList;

import org.bukkit.entity.Player;

public class DSPlayer {
    private int points;
    private UUID trapper;
    private UUID trappee;
    private Player player;
    private boolean death;
    private boolean natural;
    private ArrayList<UUID> remaining;

    public DSPlayer( Player player ) {
        setPoints( 0 );
        setTrapper( player.getUniqueId() );
        setTrappee( player.getUniqueId() );
        setPlayer( player );
        setNatural( true );
        this.remaining = new ArrayList<UUID>();
    }

    public void setPoints( int points ) {
        this.points = points;
    }

    public void setTrapper( UUID trapper ) {
        this.trapper = trapper;
    }

    public void setTrappee( UUID trappee ) {
        this.trappee = trappee;
    }

    public void setPlayer( Player player ) {
        this.player = player;
    }

    public void setDeath( boolean death ) {
        this.death = death;
    }

    public void setNatural( boolean natural ) {
        this.natural = natural;
    }

    public int getPoints() {
        return this.points;
    }

    public UUID getTrapper() {
        return this.trapper;
    }

    public UUID getTrappee() {
        return this.trappee;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean getDeath() {
        return this.death;
    }

    public boolean getNatural() {
        return this.natural;
    }

    public ArrayList<UUID> getRemaining() {
        return this.remaining;
    }
}