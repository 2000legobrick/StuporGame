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
			Point pointL1 = new Point(mob.getCurrentX() - StateMachine.getTileSize(), mob.getCurrentY() + StateMachine.getTileSize());
			Point pointL2 = new Point(mob.getCurrentX() - StateMachine.getTileSize(), mob.getCurrentY());
			Point pointL3 = new Point(mob.getCurrentX() - StateMachine.getTileSize(), mob.getCurrentY() - StateMachine.getTileSize());
			Point pointR1 = new Point(mob.getCurrentX() + StateMachine.getTileSize(), mob.getCurrentY() - StateMachine.getTileSize());
			Point pointR2 = new Point(mob.getCurrentX(), mob.getCurrentY() - StateMachine.getTileSize());
			Point pointR3 = new Point(mob.getCurrentX() - StateMachine.getTileSize(), mob.getCurrentY() - StateMachine.getTileSize());

			mob.setL1(PointIntersection(pointL1, world)); 
			mob.setL2(PointIntersection(pointL2, world)); 
			mob.setL3(PointIntersection(pointL3, world)); 
			mob.setR1(PointIntersection(pointR1, world)); 
			mob.setR2(PointIntersection(pointR2, world)); 
			mob.setR3(PointIntersection(pointR3, world)); 
			
			double distanceToPlayer = getDistance(new Point(mob.getCurrentX(), mob.getCurrentY()), new Point (player.getCurrentX(), player.getCurrentY()));
			
			if (distanceToPlayer <= StateMachine.getTileSize() * 6) {
				if (player.getCurrentX() > mob.getCurrentX()) {
					mob.setVelocityX(mob.getSpeed());
				}
			}
		}
	}
	
	public int getDistance(Point p1, Point p2) {
		return  (int) Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2)+ Math.pow(p2.getY() - p1.getY(), 2));
	}
	
	public boolean PointIntersection (Point point, World world) {
		
		if (world.getWorldGrid().get(point.x / StateMachine.getTileSize()).get(point.y / StateMachine.getTileSize()).getType() != 0) {
			return true;
		}
		return false;
	}
}
