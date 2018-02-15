package com.tpprod.stupor;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HealthRegen implements Runnable {
	
	private AudioFile healthSound = new AudioFile("Content/Audio/SoundEffects/HealthRegen.wav");
	
	/*
	 * This does the actual incrementing of the mobs health waiting in between each
	 * health increment so that it is not an instantaneous heal
	 */
	public void healthRegen(Mob entity, int heal) {
		if (entity.getHealth() <= entity.getMaxHealth()) {
			for (int i = 0; i < 10; i++) {
				entity.healthUp(1);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					StringWriter error = new StringWriter();
					e.printStackTrace(new PrintWriter(error));
					try {
						Log.add(error.toString());
					} catch (Exception e1) {
					}
				}
			}
		}
		healthSound.stop();
	}

	/*
	 * This multithreading allows the players health to tick while other things
	 * happen in the game
	 */
	public void start() {
		new Thread(this).start();
		healthSound.play();
	}

	/*
	 * When called this starts the actual regen
	 */
	@Override
	public void run() {
		healthRegen(StateMachine.getPhysics().getPlayer(), 1);
	}
}
