package core;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import basic.Updatable;
import basic.Updateloop;

public abstract class FullScreenViewer implements Updatable {

	private BufferStrategy bs;
	public final int SCREEN_WIDTH, SCREEN_HEIGHT;

	private Updateloop ul;
	private JFrame jFrame;
	public JFrame getJFrame() { return jFrame; }

	public FullScreenViewer() {
		//Acquiring the current Graphics Device and Graphics Configuration
		GraphicsEnvironment graphEnv =
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice graphDevice = graphEnv.getDefaultScreenDevice();
		GraphicsConfiguration graphicConf =
			graphDevice.getDefaultConfiguration();
		SCREEN_WIDTH = graphicConf.getBounds().width;
		SCREEN_HEIGHT = graphicConf.getBounds().height;

		//Creating the JFrame
		jFrame = new JFrame(graphicConf);
		jFrame.setIgnoreRepaint(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.setFocusTraversalKeysEnabled(false);
		jFrame.setUndecorated(true);

		//Switching Resolution to Full Screen
		DisplayMode disMode = new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, 32,
				DisplayMode.REFRESH_RATE_UNKNOWN);
		graphDevice.setFullScreenWindow(jFrame);
		graphDevice.setDisplayMode(disMode);

		//Setting up Double Buffering
		jFrame.createBufferStrategy(2);
		bs = jFrame.getBufferStrategy();
	}
	@Override
	public void update() {
		//Update system state
		systemUpdate();

		//Create graphics object
		Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();

		renderGraphics(g2d);

		//Dispose graphics object
		g2d.dispose();

		//Rendering the current buffer to screen
		bs.show();
	}
	public void startUpdateloop(double updatesPerSecond) {
		ul = new Updateloop(this, updatesPerSecond);
		ul.start();
	}
	public void stopUpdateloop() { ul.stop(); }
	public abstract void renderGraphics(Graphics2D g2d);
	public abstract void systemUpdate();
}
