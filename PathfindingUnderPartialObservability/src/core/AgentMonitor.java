package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import mapPurgers.AdvPurger;

public class AgentMonitor extends FullScreenViewer implements KeyListener {

	public static void main(String[] args) {
		try {
		if (args.length == 0)
			new AgentMonitor(agents.IWA_StarAgent.class, "6");
		else
			new AgentMonitor(agents.IWA_StarAgent.class, args[0]);
		} catch (Exception e) { e.printStackTrace(); System.exit(1); }
	}

	private final double UPS = 5.0;
	public static final int TILE_PIXEL_DIMENTION = 6;
	public static final int GRID_DIMENTION = 50;

	public final String filename;

	private boolean pause;
	private TileMapImpl normal, shadow, trail, agentKnowledge;

	private Agent agent;
	private TileImpl getAgentLocation() { return normal.getAgentLocation(); }
	public TileImpl getGoalLocation() { return normal.getTile(TileType.GOAL); }

	private double agentTravelDistance = 0.0;
	public void moveAgent(TileImpl destination) {
		moveAgent(destination.getX(), destination.getY());
	}
	public void moveAgent(int x, int y) {
		// can only move one tile at a time (strait or diagonal)
		// can only move to a non-obstacle tile
		if (Math.abs(getAgentLocation().getX() - x) > 1 ||
				Math.abs(getAgentLocation().getY() - y) > 1 ||
				normal.getTile(x, y).getType().equals(TileType.SOLID)) {

			terminateAgent(false, "Illegal agent move from="+getAgentLocation()+
					" to=("+normal.getTile(x, y)+")");
			return;
		}
		TileImpl oldAgentLocation = getAgentLocation();
		normal.teleportAgent(x, y);
		trail.teleportAgent(x, y);
		shadow = normal.generateShadowedMap(normal.getAgentLocation());
		agentKnowledge.mergeWithMap(shadow);
		agentKnowledge.teleportAgent(x, y);
		agentTravelDistance += oldAgentLocation.fieldDistance(getAgentLocation());
	}

	private AgentStatus currentAgentStatus = AgentStatus.RUNNING;
	public AgentStatus getAgentStatus() { return currentAgentStatus; }
	private enum AgentStatus {
		TERMINATED_IN_GOAL, TERMINATED, RUNNING, PAUSED
	}

	private Semaphore agentGuard = new Semaphore(0);
	public void terminateAgent(boolean inGoal) {
		if (inGoal)
			currentAgentStatus = AgentStatus.TERMINATED_IN_GOAL;
		else
			currentAgentStatus = AgentStatus.TERMINATED;
		new Semaphore(0).acquireUninterruptibly();
	}
	public void terminateAgent(boolean inGoal, String msg) {
		System.err.println(msg);
		terminateAgent(inGoal);
	}

	public AgentMonitor(Class<?> agentImpl, String mapFileName) {
		filename = "agent_knowledge"+mapFileName+".sav";
		pause = false;
		getJFrame().addKeyListener(this);

		List<TileImpl> tiles= DataReader.readFile(mapFileName+".map");

		normal				= new TileMapImpl(tiles);

		trail				= normal.clone();
		trail.setTrail(true);

		shadow				= normal.generateShadowedMap(normal.getAgentLocation());

		File file = new File(filename);
		if (file.isFile())
			try {
				//purge agent knowledge
				new AdvPurger(filename).purge();

				agentKnowledge = (TileMapImpl) SaveRestore.restore(filename);
				agentKnowledge.teleportAgent(getAgentLocation().getX(),
						getAgentLocation().getY());
			}
			catch (IOException e) { e.printStackTrace(); }
			catch (ClassNotFoundException e) { e.printStackTrace(); }
		else
			agentKnowledge	= shadow.clone();

		try {
			this.agent = (Agent) agentImpl.getConstructors()[0].
							newInstance(this, agentKnowledge);				
		} catch (Exception e) { e.printStackTrace(); System.exit(1); }

		startUpdateloop(UPS);

		while (true) {
			drawGuard.acquireUninterruptibly();
			agent.action();
		}
	}
	private Semaphore drawGuard = new Semaphore(0);
	@Override
	public void renderGraphics(Graphics2D g2d) {
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		int x = 490, y = 30, dx = 400, dy = 390, dxText = 5, dyText = -7;

		normal.drawGraphics(g2d, x, y, 1);
		drawText(g2d, "Total View", x+dxText, y+dyText);

		trail.drawGraphics(g2d, x += dx, y, 1);
		drawText(g2d, "Agent Trail View", x+dxText, y+dyText);

		shadow.drawGraphics(g2d, x -= dx, y += dy, 1);
		drawText(g2d, "Shadow View", x+dxText, y+dyText);

		agentKnowledge.drawGraphics(g2d, x += dx, y, 1);
		drawText(g2d, "Agent Knowledge View", x+dxText, y+dyText);

		x = 5; y = 15; dy = 15;
		g2d.setColor(Color.WHITE);
		g2d.drawString("Spacebar = pause/unpause", x, y);
		g2d.drawString("Ecs = quit", x, y += dy);
		g2d.drawString("Agent Status : "+getAgentStatus(), x, y += 2*dy);
		g2d.drawString("Agent Distance Traveled : "+agentTravelDistance, x, y += dy);
		g2d.drawString("Agent Location : "+getAgentLocation(), x, y += dy);
		g2d.drawString("Goal Location : "+normal.getTile(TileType.GOAL), x, y += 2*dy);

		// done drawing -> agent may move
		if (!pause && (currentAgentStatus.equals(AgentStatus.RUNNING)))
			drawGuard.release();
	}
	private Font textFont = new Font("Tahoma", Font.PLAIN, 12);
	private void drawText(Graphics2D g2d, String text, int x, int y) {
		g2d.setFont(textFont);
		g2d.setColor(Color.WHITE);
		g2d.drawString(text, x, y);
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_SPACE:
			if (pause)
				;//TODO
			else {
				//TODO
				agentGuard.drainPermits();
			}
			pause = !pause;
			break;
		}
	}

	// UNUSED methods
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void systemUpdate() { /* do nothing*/ }
}
