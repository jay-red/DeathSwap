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

public class DSGame extends BukkitRunnable {
    private int swaps;
    private Random rng;
    private int seconds;
    private int playerCount;
    private DSNode[] sudokuBuffer;
    private DSPlayer[][] swapBuffer;
    private ArrayList<UUID> players;
    private ArrayList<UUID> winners;
    private ArrayList<DSPlayer> dsPlayers;
    private HashMap<UUID, Location> locations;

    public DSGame() {
        this.seconds = 0;
        this.playerCount = 0;
        this.players = new ArrayList<UUID>();
        this.dsPlayers = new ArrayList<DSPlayer>();
        this.winners = new ArrayList<UUID>();
        this.locations = new HashMap<UUID, Location>();
        this.rng = new Random();
        for( HashMap.Entry<UUID, DSPlayer> entry : DeathSwap.playerMap.entrySet() ) {
            this.players.add( entry.getKey() );
            this.dsPlayers.add( entry.getValue() );
            this.playerCount++;
        }
        this.sudokuBuffer = new DSNode[ this.playerCount ];
        for( int i = 0; i < this.playerCount; ++i ) {
            this.sudokuBuffer[ i ] = new DSNode();
        }
        this.swapBuffer = new DSPlayer[ this.playerCount - 1 ][ this.playerCount ];
    }

    private void computeSwaps() {
        for( int i = 0; i < this.playerCount; ++i ) {
            this.sudokuBuffer[ i ].setChildren( this.dsPlayers.get( i ), this.dsPlayers );
        }
        int available;
        DSNode ptr;
        DSPlayer player;
        for( int i = 0; i < this.playerCount - 1; ++i ) {
            for( int j = 0; j < this.playerCount; ++j ) {
                this.swapBuffer[ i ][ j ] = null;
            }
        }
        for( int i = 0; i < this.playerCount - 1; ++i ) {
            ptr = this.sudokuBuffer[ 0 ];
            for( int j = 0; j < this.playerCount; ++j ) {
                this.dsPlayers.get( j ).setUsed( false );
                this.sudokuBuffer[ j ].setCompleted( false );
                this.sudokuBuffer[ j ].resetEffective();
                this.sudokuBuffer[ j ].setPlayer( null );
            }
            for( int j = 0; j < this.playerCount; ++j ) {
                player = ptr.getNextChild();
                player.setUsed( true );
                ptr.setPlayer( player );
                ptr.setCompleted( true );
                ptr = player.getNextParent( ptr );
                available = 0;
                if( ptr == null && j < this.playerCount - 1 ) {
                    ptr = this.sudokuBuffer[ available ];
                    while( ptr.getCompleted() ) {
                        ++available;
                        ptr = this.sudokuBuffer[ available ];
                    }
                }
            }
            for( int j = 0; j < this.playerCount; ++j ) {
                this.swapBuffer[ i ][ j ] = this.sudokuBuffer[ j ].getPlayer();
            }
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
                int points;
                int curr = 0;
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
                }
                if( swaps == 0 ) {
                    computeSwaps();
                }
                for( int i = 0; i < this.playerCount; ++i ) {
                    this.dsPlayers.get( i ).setTrappee( this.swapBuffer[ swaps ][ i ].getPlayer().getUniqueId() );
                    this.swapBuffer[ swaps ][ i ].setTrapper( this.dsPlayers.get( i ).getPlayer().getUniqueId() );
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
                this.swaps %= ( this.playerCount - 1 );
                this.seconds = 0;
                break;
            default:
                return;
        }
    }
}