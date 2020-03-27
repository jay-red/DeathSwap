package com.jaredflores.deathswap.Commands;

import java.util.UUID;
import java.util.HashMap;

import org.bukkit.ChatColor;

import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import com.jaredflores.deathswap.DeathSwap;
import com.jaredflores.deathswap.DSPlayer;
import com.jaredflores.deathswap.DSGame;

public class CommandDS implements CommandExecutor {

    public void usageTip( CommandSender sender ) {
        String msg = ChatColor.RED + "Invalid command. Usage:" + "\n";
        msg += ChatColor.RED + "/ds init - Initializes a new Death Swap game" + "\n";
        msg += ChatColor.RED + "/ds add <player> - Adds a player to the Death Swap game" + "\n";
        msg += ChatColor.RED + "/ds start - Starts the existing Death Swap game" + "\n";
        msg += ChatColor.RED + "/ds start - Stops the existing Death Swap game" + "\n";
        sender.sendMessage( msg );
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
        if( args.length > 0 ) {
            String cmd = args[ 0 ];
            if( cmd.equalsIgnoreCase( "init" ) ) {
                if( DeathSwap.existingGame ) {
                    sender.sendMessage( ChatColor.RED + "Game already exists!" );
                } else {
                    DeathSwap.playerMap.clear();
                    DeathSwap.existingGame = true;
                    DeathSwap.startedGame = false;
                    for( Player p : DeathSwap.server.getOnlinePlayers() ) {
                        p.sendMessage( ChatColor.YELLOW + "A game of Death Swap has been created!" );
                    }
                }
            } else if( cmd.equalsIgnoreCase( "add" ) ) {
                if( DeathSwap.existingGame ) {
                    if( DeathSwap.startedGame ) {
                        sender.sendMessage( ChatColor.RED + "Game has already started!" );
                    } else {
                        if( args.length > 1 ) {
                            Player target = DeathSwap.server.getPlayerExact( args[ 1 ] );
                            if( target != null ) {
                                if( DeathSwap.playerMap.containsKey( target.getUniqueId() ) ) {
                                    sender.sendMessage( ChatColor.RED + "Player already in game!" );
                                } else {
                                    DeathSwap.playerMap.put( target.getUniqueId(), new DSPlayer( target ) );
                                    sender.sendMessage( ChatColor.YELLOW + target.getDisplayName() + " has been added to the game." );
                                    target.sendMessage( ChatColor.YELLOW + "You have been added to the Death Swap game." );
                                }
                            } else {
                                sender.sendMessage( ChatColor.RED + "Player not found." );
                            }
                        } else {
                            sender.sendMessage( ChatColor.RED + "You must specify a player!" );
                            sender.sendMessage( ChatColor.RED + "/ds add <player> - Adds a player to the Death Swap game" );
                        }
                    }
                } else {
                    sender.sendMessage( ChatColor.RED + "There is no active Death Swap game." );
                }
            } else if( cmd.equalsIgnoreCase( "remove" ) ) {
                if( DeathSwap.existingGame ) {
                    if( DeathSwap.startedGame ) {
                        sender.sendMessage( ChatColor.RED + "Game has already started!" );
                    } else {
                        if( args.length > 1 ) {
                            Player target = DeathSwap.server.getPlayerExact( args[ 1 ] );
                            if( target != null ) {
                                if( DeathSwap.playerMap.containsKey( target.getUniqueId() ) ) {
                                    DeathSwap.playerMap.remove( target.getUniqueId() );
                                    sender.sendMessage( ChatColor.YELLOW + target.getDisplayName() + " has been removed from the game." );
                                    target.sendMessage( ChatColor.YELLOW + "You have been removed from the Death Swap game." );
                                } else {
                                    sender.sendMessage( ChatColor.RED + "Player not in game." );
                                }
                            } else {
                                sender.sendMessage( ChatColor.RED + "Player not found." );
                            }
                        } else {
                            sender.sendMessage( ChatColor.RED + "You must specify a player!" );
                            sender.sendMessage( ChatColor.RED + "/ds add <player> - Adds a player to the Death Swap game" );
                        }
                    }
                } else {
                    sender.sendMessage( ChatColor.RED + "There is no active Death Swap game." );
                }
            } else if( cmd.equalsIgnoreCase( "start" ) ) {
                if( DeathSwap.existingGame ) {
                    if( DeathSwap.startedGame ) {
                        sender.sendMessage( ChatColor.RED + "Game has already started!" );
                    } else {
                        if( DeathSwap.playerMap.size() >= 2 ) {
                            for( HashMap.Entry<UUID, DSPlayer> entry : DeathSwap.playerMap.entrySet() ) {
                                Player p = entry.getValue().getPlayer();
                                p.sendMessage( ChatColor.YELLOW + "Death Swap has begun!" );
                            }
                            DeathSwap.startedGame = true;
                            DeathSwap.dsTask = ( new DSGame() ).runTaskTimer( DeathSwap.plugin, 0L, 20L );
                        } else {
                            sender.sendMessage( ChatColor.RED + "There must be at least 2 players.\nThere is currently: " + Integer.toString( DeathSwap.playerMap.size() ) );
                        }
                    }
                } else {
                    sender.sendMessage( ChatColor.RED + "There is no active Death Swap game." );
                }
            } else if( cmd.equalsIgnoreCase( "stop" ) ) {
                if( DeathSwap.existingGame ) {
                    sender.sendMessage( ChatColor.YELLOW + "Game has been stopped." );
                    DeathSwap.existingGame = false;
                    if( DeathSwap.startedGame ) {
                        DeathSwap.dsTask.cancel();
                        DeathSwap.dsTask = null;
                    }
                    DeathSwap.startedGame = false;
                } else {
                    sender.sendMessage( ChatColor.RED + "There is no active Death Swap game." );
                }
            } else {
                usageTip( sender );
            }
        } else {
            usageTip( sender );
        }
        return true;
    }
}