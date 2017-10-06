package com.tpprod.stupor;

import java.util.ArrayList;

public class Physics implements Runnable {

    public ArrayList<Mob> mobs = new ArrayList<Mob>(); 
	
    private boolean running = true;
    private int displacement, velocity, acceleration, force;
    
	public void addForce(Mob thing) {

	}

	@Override
	public void run() {
		//while (running) {
			//for ()
		//}
	}
	
	public void stop() {
		running = false;
	}
	
	public Physics () {
	}
}
