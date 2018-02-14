package com.tpprod.stupor;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HealthRegen implements Runnable {
	
	public void healthRegen(Mob entity, int heal) {
		if (entity.getHealth() <= entity.getMaxHealth()) {
			for (int i = 0; i < 10; i++) {
				entity.healthUp(1);
				try {
					Thread.sleep(1000);
				} catch(Exception e) {
					StringWriter error = new StringWriter();
					e.printStackTrace(new PrintWriter(error));
					try{
						Log.add(error.toString());
					}catch (Exception e1) {}
				}
			}
		}
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		healthRegen(StateMachine.getPhysics().getPlayer(), 1);
	}
}
