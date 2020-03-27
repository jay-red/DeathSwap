package com.jaredflores.deathswap;

import java.util.UUID;
import java.util.HashMap;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.jaredflores.deathswap.DSGame;
import com.jaredflores.deathswap.DSPlayer;

import com.jaredflores.deathswap.Commands.CommandDS;

import com.jaredflores.deathswap.Listeners.PlayerDeath;

public class DeathSwap extends JavaPlugin {

    public static Plugin plugin;
    public static Server server;
    public static HashMap<UUID, DSPlayer> playerMap;
    public static boolean existingGame;
    public static boolean startedGame;
    public static BukkitTask dsTask;

    @Override
    public void onEnable() {
        this.plugin = ( Plugin ) this;
        this.server = this.getServer();
        this.server.getPluginManager().registerEvents( new PlayerDeath(), this );

        this.getCommand( "ds" ).setExecutor( new CommandDS() );

        this.playerMap = new HashMap<UUID, DSPlayer>();
        this.existingGame = false;
        this.startedGame = false;
        this.dsTask = null;
    }

    @Override
    public void onDisable() {

    }

}
