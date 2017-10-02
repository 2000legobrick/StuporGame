package com.tpprod.stupor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import javax.swing.JFrame;

public class StateMachine extends Canvas implements Runnable{
	
	private static final long serialVesionUID = 1L;
	public static final int WIDTH = 640, HEIGHT = 480;
	public static final String NAME = "Stupor";
	
	private boolean running = false;
	private Render render = new Render();
	
	public StateMachine () {
		
	}
	
	public void start() {
		if (!running) {
			running = true;
			new Thread(this).start();
		} 
	}
	
	public void stop () {
		running = false;
	}
	
	@Override
	public void run() {
		try {
			render.InitializeWorld();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		int fps = 0, tick = 0;
		double timer = System.currentTimeMillis();
		
		double nsPerTick = 1000000000.0d / 60;
		double previous = System.nanoTime();
		double unprocessed = 0;
		
		boolean canRender = false;
		
		while(running) {
			double current = System.nanoTime();
			unprocessed += (current - previous) / nsPerTick;
			previous = current;
			while (unprocessed >= 1) {
				//Updates game objects
				
				tick ++;
				tick();
				canRender = true;
				--unprocessed;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Draws the current frame
			if (canRender) {
				fps++;
				render();
			}
			//Print current fps and ticks
			if (System.currentTimeMillis() - timer > 1000) {
				System.out.printf("%d fps, %d tick%n", fps, tick);
				fps = 0;
				tick = 0;
				timer += 1000;
			}
		}
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//Render Objects here
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		render.RenderBackground(g, getWidth(), getHeight());
		render.RenderForeground(g, getWidth(), getHeight());
		render.Movement(g);
		//Done with rendering
		g.dispose();
		bs.show();
	}
	
	private void tick() {
		
	}
	
	public static void main(String[] args) {
		StateMachine game = new StateMachine();
		Dimension dimension = new Dimension(WIDTH, HEIGHT);
		game.setMaximumSize(dimension);
		game.setMinimumSize(dimension);
		game.setPreferredSize(dimension);
		game.setSize(dimension);
		
		JFrame frame = new JFrame();
		frame.setTitle(NAME);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
}
