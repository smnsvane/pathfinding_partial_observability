package basic;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface IEngineState
{
	public void stateUpdate();
	public void renderGraphics();
	public void drawState(Graphics2D g2d);
	public void keyPress(KeyEvent e);
	public void keyRelease(KeyEvent e);
	public void mousePress(MouseEvent e);
	public void mouseRelease(MouseEvent e);
	public void mouseDrag(MouseEvent e);
	public void mouseMove(MouseEvent e);
}
