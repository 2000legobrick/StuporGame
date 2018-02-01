package com.tpprod.stupor;

public class AI {
	public AI(Mob mob, Mob player) {
		double d = Math.sqrt(Math.pow((player.currentX - mob.currentX),2) + Math.pow((player.currentY - mob.currentY),2));
		System.out.println(d);
	}
}
