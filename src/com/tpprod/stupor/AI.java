package com.tpprod.stupor;

import java.awt.Point;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class AI{
	
	private ArrayList<Mob> mobsAi = new ArrayList<Mob>();
	
	public void AIs(ArrayList<Mob> mobs){
		mobsAi = (ArrayList<Mob>) mobs.subList(1, mobs.size());
	}
	
	public AI() {
		
	}
	
	public void Move () {
		for(Mob mob: mobsAi) {
			
			//double distance = getDistance(new Point(mob.currentX, mob.currentY), new Point (player.currentX, player.currentY));
			
			//if (distance <= StateMachine.tileSize * 6)
		}
	}
	
	public int getDistance(Point p1, Point p2) {
		return  (int) Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2)+ Math.pow(p2.getY() - p1.getY(), 2));
	}
}
