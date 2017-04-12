package basic;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class AbstractEngine implements IEngine, Updatable {

	private IEngineState currentEngineState;
	@Override
	public IEngineState getEngineState() { return currentEngineState; }
	/**
	 * WARNING EngineState is initially = null
	 * EngineState must be set before launchEngine is called
	 */
	@Override
	public void setEngineState(IEngineState state) {
		currentEngineState = state;
	}

	public final Updateloop ul;
	
	public AbstractEngine(double updatesPerSecond) {
		ul = new Updateloop(this, updatesPerSecond);
	}

	@Override
	public void renderGraphics() { getEngineState().renderGraphics(); }
	@Override
	public void drawState(Graphics2D g2d) { getEngineState().drawState(g2d); }

	/**
	 * WARNING EngineState is initially = null
	 * EngineState must be set before launchEngine is called
	 */
	@Override
	public void launchEngine() {
		System.out.print("ENGINE LAUNCHING... ");//remove
		ul.start();
		System.out.println("ENGINE LAUNCHED");//remove
	}

	@Override
	public void update() { getEngineState().stateUpdate(); }

	//keyboard
	@Override
	public void keyPressed(KeyEvent e) { getEngineState().keyPress(e); }
	@Override
	public void keyReleased(KeyEvent e) { getEngineState().keyRelease(e); }

	//mouse buttons
	@Override
	public void mousePressed(MouseEvent e) { getEngineState().mousePress(e); }
	@Override
	public void mouseReleased(MouseEvent e) { getEngineState().mouseRelease(e); }

	//mouse motion
	@Override
	public void mouseDragged(MouseEvent e) { getEngineState().mouseDrag(e); }
	@Override
	public void mouseMoved(MouseEvent e) { getEngineState().mouseMove(e); }

	//unused listener methods
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
