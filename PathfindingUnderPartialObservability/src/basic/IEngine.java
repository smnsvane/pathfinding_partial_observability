package basic;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface IEngine extends KeyListener, MouseListener, MouseMotionListener {

	public void renderGraphics();
	public void drawState(Graphics2D g2d);
	public void launchEngine();
	public IEngineState getEngineState();
	public void setEngineState(IEngineState state);
}
