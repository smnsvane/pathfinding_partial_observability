package basic;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Iterator;

public class TileMap<T extends Tile> implements Map<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private T[][] grid;
	@Override
	public int getMapDimention() { return grid.length; }
	@Override
	public T getTile(int x, int y) { return grid[x][y]; }
	@Override
	public void setTile(T tile) { grid[tile.getX()][tile.getY()] = tile; }

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int x = 0;
			int y = 0;
			@Override
			public boolean hasNext() { return y < getMapDimention(); }
			@Override
			public T next() {
				T t = getTile(x, y);
				if (x < grid.length - 1) x++;
				else { x = 0; y++; }
				return t;
			}
			@Override
			public void remove() {
				throw new RuntimeException("not implemented");
			}
		};
	}

	@Override
	public void setGrid(T[][] grid) {
		if (grid.length == 0 || grid[0].length == 0)
			throw new RuntimeException("grid has length = 0");
		this.grid = grid;
	}
	
	@Override
	public void renderGraphics() {
		for (T tile : this)
			tile.renderGraphics();
	}
	@Override
	public void drawGraphics(Graphics2D g2d, int xOffset, int yOffset, int tileSpace) {
		for (Tile tile : this)
			tile.drawGraphics(g2d,
				(tile.getTileDimention() + tileSpace) * tile.getX() + xOffset,
				(tile.getTileDimention() + tileSpace) * tile.getY() + yOffset);
	}
}
