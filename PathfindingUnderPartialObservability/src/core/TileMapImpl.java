package core;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import basic.TileMap;

public class TileMapImpl extends TileMap<TileImpl> implements Serializable {

	private static final long serialVersionUID = 1L;

	public void setTile(int x, int y, TileType type) { getTile(x, y).setType(type); }

	private volatile int agentX, agentY;
	public TileImpl getAgentLocation() { return getTile(agentX, agentY); }

	public TileImpl getTile(TileType type) {
		for (TileImpl tile : this)
			if (tile.getType().equals(type))
				return tile;
		throw new RuntimeException("could not find "+type);
	}

	private boolean trail = false;
	public boolean isTrail() { return trail; }
	public void setTrail(boolean trail) { this.trail = trail; }

	public void teleportAgent(int toX, int toY) {
		if (isTrail())
			setTile(agentX, agentY, TileType.SHADOW);
		agentX = toX;
		agentY = toY;
	}

	public TileMapImpl(List<TileImpl> tiles) {
		TileImpl[][] grid =
			new TileImpl[AgentMonitor.GRID_DIMENTION][AgentMonitor.GRID_DIMENTION];
		for (int x = 0; x < AgentMonitor.GRID_DIMENTION; x++)
			for (int y = 0; y < AgentMonitor.GRID_DIMENTION; y++)
				grid[x][y] =
					new TileImpl(x, y, TileType.CLEAR);
		setGrid(grid);
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).getType().equals(TileType.AGENT)) {
				agentX = tiles.get(i).getX();
				agentY = tiles.get(i).getY();
				tiles.get(i).setType(TileType.CLEAR);
			}
			setTile(tiles.get(i));
		}
	}
	/** grid as parameter */
	public TileMapImpl(TileImpl[][] grid, int agentX, int agentY) {
		setGrid(grid); this.agentX = agentX; this.agentY = agentY;
	}

	@Override
	public TileMapImpl clone() {
		TileImpl[][] newTileGrid = new TileImpl[getMapDimention()][getMapDimention()];
		for (int x = 0; x < getMapDimention(); x++)
			for (int y = 0; y < getMapDimention(); y++)
				newTileGrid[x][y] = getTile(x, y).clone();
		return new TileMapImpl(newTileGrid, agentX, agentY);
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TileImpl[][] grid = {{ new TileImpl(0,0,TileType.CLEAR) }};
		TileMapImpl obj = new TileMapImpl(grid, 0, 0);
		SaveRestore.save(obj, "test.sav");
		TileMapImpl resObj = (TileMapImpl) SaveRestore.restore("test.sav");
		System.out.println(obj.equals(resObj));
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TileMapImpl))
			return false;
		TileMapImpl compareObj = (TileMapImpl) obj;
		for (Iterator<TileImpl> i0 = iterator(); i0.hasNext();)
			for (Iterator<TileImpl> i1 = compareObj.iterator(); i1.hasNext();)
				if (!i1.next().equals(i0.next()))
					return false;
		return true;
	}

	public List<TileImpl> getAllTiles(TileType type) {
		List<TileImpl> list = new ArrayList<TileImpl>();
		for (TileImpl tile : this)
			if (tile.getType().equals(type))
				list.add(tile);
		return list;
	}

	public List<TileImpl> getSurroundingTiles(TileImpl center) {
		List<TileImpl> list = new ArrayList<TileImpl>();
		for (int i = center.getX()-1; i <= center.getX()+1; i++)
			for (int j = center.getY()-1; j <= center.getY()+1; j++)
				try {
					if (i == center.getX() && j == center.getY())
						continue;
					list.add(getTile(i, j));
				}
				catch (ArrayIndexOutOfBoundsException e) { /* continue */ }
		return list;
	}

	public TileMapImpl generateShadowedMap(TileImpl viewPoint) {
		TileMapImpl shadowedMap = clone();
		tileLoop:
		for (TileImpl tile : this) {
			List<TileImpl> lineOfSight =
				bresenhamLineAlgorithm(viewPoint.getX(), viewPoint.getY(),
						tile.getX(), tile.getY());
			// note first and last tile is skipped
			// as they are the agent and the viewed tile
			for (int i = 1; i < lineOfSight.size() - 1; i++)
				if (lineOfSight.get(i).getType().equals(TileType.SOLID)) {
					shadowedMap.setTile(new TileImpl(tile.getX(), tile.getY(), TileType.SHADOW));
					continue tileLoop;
				}
		}
		return shadowedMap;
	}
	public void mergeWithMap(TileMapImpl newKnowledge) {
		for (int x = 0; x < getMapDimention(); x++)
			for (int y = 0; y < getMapDimention(); y++)
				if (getTile(x, y).getType().equals(TileType.SHADOW))
					setTile(x, y, newKnowledge.getTile(x, y).getType());
	}
	@Override
	public void drawGraphics(Graphics2D g2d, int xOffset, int yOffset, int tileSpace) {
		super.drawGraphics(g2d, xOffset, yOffset, tileSpace);
		g2d.setColor(TileType.AGENT.getColor());
		g2d.fillRect(
				(AgentMonitor.TILE_PIXEL_DIMENTION + tileSpace) *
				getAgentLocation().getX() + xOffset,
				(AgentMonitor.TILE_PIXEL_DIMENTION + tileSpace) *
				getAgentLocation().getY() + yOffset,
				getTile(0, 0).getTileDimention(), getTile(0, 0).getTileDimention());
	}

	//http://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	public List<TileImpl> bresenhamLineAlgorithm(int x0, int y0, int x1, int y1) {
		boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		if (steep) {
			int tmp = x0;
			x0 = y0;
			y0 = tmp;
			tmp = x1;
			x1 = y1;
			y1 = tmp;
		}
		if (x0 > x1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
			tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		int dx = x1 - x0;
		int dy = Math.abs(y1 - y0);
		int error = dx / 2;
		int yStep = (y0 < y1?1:-1);
		int y = y0;
		List<TileImpl> list = new ArrayList<TileImpl>();
		for (int x = x0; x <= x1; x++) {
			if (steep)	list.add(getTile(y, x));
			else		list.add(getTile(x, y));
			error -= dy;
			if (error < 0) {
				y += yStep;
				error += dx;
			}
		}
		return list;
	}
	public boolean clearLineBetween(TileImpl n0, TileImpl n1) {
		List<TileImpl> list =
			bresenhamLineAlgorithm(n0.getX(), n0.getY(), n1.getX(), n1.getY());
		for (int i = 1; i < list.size() - 1; i++)
			if (!list.get(i).getType().equals(TileType.CLEAR))
				return false;
		return true;
	}
}
