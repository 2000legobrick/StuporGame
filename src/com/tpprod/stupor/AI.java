package com.tpprod.stupor;

import java.util.ArrayList;

public class AI implements Runnable {
	
	private Mob player;
	private ArrayList<Mob> mobsAi = new ArrayList<Mob>();
	
	public void AIs(ArrayList<Mob> tempMobs, Mob tempPlayer){
		
		//for(Mob mob : mobsAi)
		mobsAi = tempMobs;
		
		//mobsAi.remove(0);
		player = tempPlayer;
	}
	
	public AI() {
		
	}
	
	@Override
	public void run() {
		//TODO auto generated code
		while(StateMachine.running) {//switch to mob health
			try{
				Thread.sleep(100);
				for(Mob mob: mobsAi) {
					double d = Math.sqrt(Math.pow((player.currentX - mob.currentX),2) + Math.pow((player.currentY - mob.currentY),2));
					System.out.println(d);
				}
			} catch(Exception e) {
				e.printStackTrace();;
			} 
		}
	}
}
