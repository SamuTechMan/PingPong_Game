package Lr;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import Lr.display.Display;
import Lr.states.StateManager;
import br.input.KeyManager;

public class Game implements Runnable {

	private Display display;
	private Thread thread;
	private boolean running = false;

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;

	private StateManager sm;
	private KeyManager km;

	public Game() {
		display = new Display("PingPong", WIDTH, HEIGHT);
		sm = new StateManager();
		km = new  KeyManager();
		display.setKeyListener(sm);
		display.setKeyListener(km);
		StateManager.setState(StateManager.MENU);
	}

	@Override
	public void run() {
		init();
		int FPS = 60;
		double timePerTick = 1000000000 / FPS;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = now;

			if (delta >= 1) {
				uptade();
				render();
				delta--;
			}
		}
		stop();
	}

	private void init() {

	}

	private void uptade() {
		if (StateManager.GetState() == null)
			return;
		sm.update();
		km.update();
	}

	private void render() {
		BufferStrategy bs = display.getBufferStrategy();
		if (bs == null) {
			display.createBuffeStrategy();
			bs = display.getBufferStrategy();
		}

		Graphics g = bs.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);

		if (StateManager.GetState() != null) {
			sm.render(g);
		}

		g.dispose();
		bs.show();
	}

	public synchronized void start() {
		if (thread != null)
			return;
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		if (thread == null)
			return;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
