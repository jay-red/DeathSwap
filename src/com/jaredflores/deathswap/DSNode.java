package com.jaredflores.deathswap;

import java.util.Random;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;

public class DSNode {
	LinkedList<DSPlayer> children;
	DSPlayer player;
	int effective;
	boolean completed;

	public DSNode() {
		this.children = new LinkedList<DSPlayer>();
		setEffective( 0 );
		setCompleted( false );
	}

	public void setChildren( DSPlayer player, ArrayList<DSPlayer> players ) {
		ArrayList<Boolean> used = new ArrayList<Boolean>();
		int playerCount = players.size();
		for( int i = 0; i < playerCount; ++i ) {
			used.add( players.get( i ) == player );
		}
		Random rng = new Random();
		int nextInt;
		DSPlayer nextPlayer;
		this.children.clear();
		for( int i = 1; i < playerCount; ++i ) {
			nextInt = rng.nextInt( playerCount );
			while( used.get( nextInt ) ) {
				nextInt = rng.nextInt( playerCount );
			}
			nextPlayer = players.get( nextInt );
			nextPlayer.addParent( this );
			used.set( nextInt, true );
			this.children.addLast( nextPlayer );
		}
	}

	public void setPlayer( DSPlayer player ) {
		this.player = player;
	}

	public void setCompleted( boolean completed ) {
		this.completed = completed;
	}

	public void setEffective( int effective ) {
		this.effective = effective;
	}

	public void resetEffective() {
		this.effective = this.children.size();
	}

	public boolean getCompleted() {
		return this.completed;
	}

	public int getEffective() {
		return this.effective;
	}

	public DSPlayer getPlayer() {
		return this.player;
	}

	public DSPlayer getNextChild() {
		Iterator itr = this.children.iterator();
		DSPlayer player;
		while( itr.hasNext() ) {
			player = ( DSPlayer ) ( itr.next() );
			if( !player.getUsed() ) {
				itr.remove();
				return player;
			}
		}
		return null;
	}
}