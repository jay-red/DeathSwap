package com.jaredflores.deathswap;

import java.util.UUID;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaredflores.deathswap.DSPlayer;
import com.jaredflores.deathswap.DeathSwap;

public class DSGame extends BukkitRunnable {
    private int swaps;
    private Random rng;
    private int seconds;
    private int playerCount;
    private HashSet<UUID> used;
    private ArrayList<UUID> players;
    private ArrayList<UUID> winners;
    private HashMap<UUID, Location> locations;

    public DSGame() {
        this.seconds = 0;
        this.playerCount = 0;
        this.players = new ArrayList<UUID>();
        this.winners = new ArrayList<UUID>();
        this.locations = new HashMap<UUID, Location>();
        this.used = new HashSet<UUID>();
        this.rng = new Random();
        for( HashMap.Entry<UUID, DSPlayer> entry : DeathSwap.playerMap.entrySet() ) {
            this.players.add( entry.getKey() );
            this.playerCount++;
        }
    }

    private void countdown( int c ) {
        for( UUID key : players ) {
            DeathSwap.playerMap.get( key ).getPlayer().sendMessage( ChatColor.YELLOW + "Swapping in " + Integer.toString( c ) + " seconds" );
        }
    }

    public void run() {
        this.seconds += 1;
        DSPlayer p;
        ArrayList<UUID> remaining;
        int nextTrappeeIdx;
        UUID nextTrappee;
        switch( this.seconds ) {
            case 60:
                this.used.clear();
                int points;
                for( UUID key : players ) {
                    p = DeathSwap.playerMap.get( key );
                    points = p.getPoints();
                    if( p.getTrappee() != key ) {
                        if( DeathSwap.playerMap.get( p.getTrappee() ).getDeath() ) {
                            p.getPlayer().sendMessage( ChatColor.GREEN + "You successfully trapped your victim!\n" );
                            points += 1;
                            p.setPoints( points );
                            if( points == 4 ) {
                                this.winners.add( key );
                            }
                        } else {
                            p.getPlayer().sendMessage( ChatColor.RED + "Your trap has failed.\n" );
                        }
                    }
                    p.setNatural( true );
                    remaining = p.getRemaining();
                    if( swaps == 0 ) {
                        remaining.addAll( this.players );
                        remaining.remove( key );
                    }
                    nextTrappeeIdx = this.rng.nextInt( remaining.size() );
                    nextTrappee = remaining.get( nextTrappeeIdx );
                    while( this.used.contains( nextTrappee ) ) {
                        nextTrappeeIdx = this.rng.nextInt( remaining.size() );
                        nextTrappee = remaining.get( nextTrappeeIdx );
                    }
                    //remaining.remove( nextTrappeeIdx );
                    this.used.add( nextTrappee );
                    p.setTrappee( nextTrappee );
                    DeathSwap.playerMap.get( nextTrappee ).setTrapper( key );
                }
                String msg;
                if( this.winners.isEmpty() ) {
                    for( UUID key : players ) {
                        p = DeathSwap.playerMap.get( key );
                        msg = ChatColor.YELLOW + "Your Points: " + Integer.toString( p.getPoints() ) + "\n";
                        msg += "Next Victim: " + DeathSwap.playerMap.get( p.getTrappee() ).getPlayer().getDisplayName() + "\n";
                        msg += "Next Trapper: " + DeathSwap.playerMap.get( p.getTrapper() ).getPlayer().getDisplayName() + "\n";
                        p.getPlayer().sendMessage( msg );
                    }
                } else {
                    msg = ChatColor.YELLOW + "The game has ended!\n";
                    if( winners.size() == 1 ) msg += "Winner: ";
                    else msg += "Winners: ";
                    for( int i = 0; i < winners.size(); ++i ) {
                        msg += DeathSwap.playerMap.get( winners.get( i ) ).getPlayer().getDisplayName();
                        if( i < winners.size() - 1 ) {
                            msg += ", ";
                        }
                    }
                    msg += "\n";
                    for( UUID key : players ) {
                        DeathSwap.playerMap.get( key ).getPlayer().sendMessage( msg );
                    }
                    DeathSwap.existingGame = false;
                    if( DeathSwap.startedGame ) {
                        DeathSwap.dsTask.cancel();
                        DeathSwap.dsTask = null;
                    }
                    DeathSwap.startedGame = false;
                }
                break;
            case 290:
                countdown( 10 );
                break;
            case 291:
                countdown( 9 );
                break;
            case 292:
                countdown( 8 );
                break;
            case 293:
                countdown( 7 );
                break;
            case 294:
                countdown( 6 );
                break;
            case 295:
                countdown( 5 );
                break;
            case 296:
                countdown( 4 );
                break;
            case 297:
                countdown( 3 );
                break;
            case 298:
                countdown( 2 );
                break;
            case 299:
                countdown( 1 );
                break;
            case 300:
                for( UUID key : players ) {
                    p = DeathSwap.playerMap.get( key );
                    this.locations.put( key, p.getPlayer().getLocation() );
                }
                for( UUID key : players ) {
                    p = DeathSwap.playerMap.get( key );
                    p.setNatural( false );
                    p.setDeath( false );
                    p.getPlayer().teleport( this.locations.get( p.getTrapper() ) );
                }
                this.swaps += 1;
                //this.swaps %= ( this.playerCount - 1 );
                this.seconds = 0;
                break;
            default:
                return;
        }
    }
}