package basic;

import java.awt.Graphics2D;
import java.io.Serializable;

public interface Tile extends Cloneable, Serializable
{
	public int getX();
	public void setX(int x);
	public int getY();
	public void setY(int y);
	public void drawGraphics(Graphics2D g2d, int xOffset, int yOffset);
	public void renderGraphics();
	public int getTileDimention();
	public double fieldDistance(Tile tile);
	public Tile Clone();
}
