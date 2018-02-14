package com.tpprod.stupor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class AI{
	
	private ArrayList<Mob> mobsAi = new ArrayList<Mob>();
	
	public void AIs(ArrayList<Mob> mobs){
		//mobsAi = (ArrayList<Mob>) mobs.subList(1, mobs.size());
	}
	
	public AI() {
		
	}
	
	public void Move(World world, Mob player) {
		for(Mob mob: mobsAi) {
			Point pointL1 = new Point(mob.currentX - StateMachine.tileSize, mob.currentY + StateMachine.tileSize);
			Point pointL2 = new Point(mob.currentX - StateMachine.tileSize, mob.currentY);
			Point pointL3 = new Point(mob.currentX - StateMachine.tileSize, mob.currentY - StateMachine.tileSize);
			Point pointR1 = new Point(mob.currentX + StateMachine.tileSize, mob.currentY - StateMachine.tileSize);
			Point pointR2 = new Point(mob.currentX, mob.currentY - StateMachine.tileSize);
			Point pointR3 = new Point(mob.currentX - StateMachine.tileSize, mob.currentY - StateMachine.tileSize);

			mob.L1 = PointIntersection(pointL1, world); 
			mob.L2 = PointIntersection(pointL2, world); 
			mob.L3 = PointIntersection(pointL3, world); 
			mob.R1 = PointIntersection(pointR1, world); 
			mob.R2 = PointIntersection(pointR2, world); 
			mob.R3 = PointIntersection(pointR3, world); 
			
			double distanceToPlayer = getDistance(new Point(mob.currentX, mob.currentY), new Point (player.currentX, player.currentY));
			
			if (distanceToPlayer <= StateMachine.tileSize * 6) {
				if (player.currentX > mob.currentX) {
					mob.velocityX = mob.speed;
				}
			}
		}
	}
	
	public int getDistance(Point p1, Point p2) {
		return  (int) Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2)+ Math.pow(p2.getY() - p1.getY(), 2));
	}
	
	public boolean PointIntersection (Point point, World world) {
		
		if (world.worldGrid.get(point.x / StateMachine.getTileSize()).get(point.y / StateMachine.getTileSize()).getType() != 0) {
			return true;
		}
		return false;
	}
}
