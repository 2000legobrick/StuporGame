package com.tpprod.stupor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class AI{
	
	/*
	 * Skipped due to the unfinished nature.
	 */
	
	private ArrayList<Mob> mobsAi = new ArrayList<Mob>();
	
	public void setMobAIList(ArrayList<Mob> mobs){
		 mobsAi = new ArrayList<Mob>();
		for (int x = 1; x < mobs.size(); x++)
			mobsAi.add(mobs.get(x));
	}
	
	public AI() {
		
	}
	
	public void Move(World world, Mob player) {
		for(Mob mob: mobsAi) {
			Point pointL1 = new Point(mob.getCurrentX() - StateMachine.getTileSize(), mob.getCurrentY() + StateMachine.getTileSize());
			Point pointL2 = new Point(mob.getCurrentX() - StateMachine.getTileSize(), mob.getCurrentY());
			Point pointL3 = new Point(mob.getCurrentX() - StateMachine.getTileSize(), mob.getCurrentY() - StateMachine.getTileSize());
			Point pointR1 = new Point(mob.getCurrentX() + StateMachine.getTileSize(), mob.getCurrentY() + StateMachine.getTileSize());
			Point pointR2 = new Point(mob.getCurrentX() + StateMachine.getTileSize(), mob.getCurrentY());
			Point pointR3 = new Point(mob.getCurrentX() + StateMachine.getTileSize(), mob.getCurrentY() - StateMachine.getTileSize());

			mob.setL1(PointIntersection(pointL1, world)); 
			mob.setL2(PointIntersection(pointL2, world)); 
			mob.setL3(PointIntersection(pointL3, world)); 
			mob.setR1(PointIntersection(pointR1, world)); 
			mob.setR2(PointIntersection(pointR2, world)); 
			mob.setR3(PointIntersection(pointR3, world)); 
			
			double distanceToPlayer = getDistance(new Point(mob.getCurrentX(), mob.getCurrentY()), new Point (player.getCurrentX(), player.getCurrentY()));
			
			if (distanceToPlayer <= StateMachine.getTileSize() * 5) {
				if (distanceToPlayer >= StateMachine.getTileSize()) {
					if (player.getCurrentX() > mob.getCurrentX()) {
						mob.setVelocityX(mob.getSpeed()/2);
						mob.FaceRight();
						if (mob.isR2()) {
							mob.Jump();
						}
					} else if (player.getCurrentX() < mob.getCurrentX()) {
						mob.setVelocityX(-mob.getSpeed()/2);
						mob.FaceLeft();
						if (mob.isL2()) {
							mob.Jump();
						}
					} else {
						mob.setVelocityX(0);
					}
				}
				if (mob.getProjectileList().isEmpty() && mob.getWantsToShoot() == 0) {
					double velX = (player.getCurrentX() - mob.getCurrentX())/15;
					double velY = ((player.getCurrentY() - mob.getCurrentY())/2 - ((double)Physics.GRAVITY / 8) * 2);
					mob.Shoot(velX + mob.getVelocityX(), -velY);
					mob.setWantsToShoot(6);
				}
			} else {
				if (mob.isFacingLeft()) {
					if (!mob.isL2() && mob.isL1()) {
						mob.setVelocityX(-mob.getSpeed()/3);
					} else {

						if (!mob.isL1()) {
							mob.setVelocityX(-mob.getSpeed()/3);
						} else {
							mob.FaceRight();
						}
					}
				} else {
					if (!mob.isR2() && mob.isR1()) {
						mob.setVelocityX(mob.getSpeed()/3);
					} else {
						if (!mob.isR1()) {
							mob.setVelocityX(-mob.getSpeed()/3);
						} else {
							mob.FaceLeft();
						}
					}
				}
			}
		}
	}
	
	public int getDistance(Point p1, Point p2) {
		return  (int) Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2)+ Math.pow(p2.getY() - p1.getY(), 2));
	}
	
	public boolean PointIntersection (Point point, World world) {
		try {
			if (world.getWorldGrid().get(point.y / StateMachine.getTileSize()).get(point.x / StateMachine.getTileSize()).getType() != 0) {
				return true;
			}
		} catch (Exception e) {}
		return false;
	}
}
