package basic;

import java.awt.Graphics2D;

public interface Map<T extends Tile> extends Iterable<T>
{
	public void renderGraphics();
	public void drawGraphics(Graphics2D g2d, int xOffset, int yOffset, int tileSpace);
	public T getTile(int x, int y);
	public void setTile(T tile);
	public int getMapDimention();
	public void setGrid(T[][] grid);
}
