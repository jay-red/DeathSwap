package com.jaredflores.deathswap;

import java.util.UUID;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;

import org.bukkit.entity.Player;

public class DSPlayer {
    private int points;
    private UUID trapper;
    private UUID trappee;
    private Player player;
    private boolean death;
    private boolean natural;
    private LinkedList<DSNode> parents;
    private DSNode nextParent;
    private boolean used;

    public DSPlayer( Player player ) {
        setPoints( 0 );
        setTrapper( player.getUniqueId() );
        setTrappee( player.getUniqueId() );
        setPlayer( player );
        setNatural( true );
        this.parents = new LinkedList<DSNode>();
        setUsed( false );
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

    public void setUsed( boolean used ) {
        this.used = used;
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

    public boolean getUsed() {
        return this.used;
    }

    public DSNode getNextParent( DSNode current ) {
        this.nextParent = null;
        DSNode parent;
        int effective;
        Iterator itr = this.parents.iterator();
        while( itr.hasNext() ) {
            parent = ( DSNode ) ( itr.next() );
            if( parent == current ) {
                itr.remove();
            }
            if( !parent.getCompleted() ) {
                if( parent != current ) {
                    effective = parent.getEffective() - 1;
                    parent.setEffective( effective );
                    if( nextParent == null ) {
                        nextParent = parent;
                    } else if( effective < nextParent.getEffective() ) {
                        nextParent = parent;
                    }
                }
            }
        }
        return this.nextParent;
    }

    public void addParent( DSNode parent ) {
        this.parents.addLast( parent );
    }
}