package core;

import java.awt.Graphics2D;
import java.io.Serializable;

import basic.AbstractTile;
import basic.Tile;

public class TileImpl extends AbstractTile implements Serializable {

	private static final long serialVersionUID = 1L;

	private TileType type;
	public TileType getType() { return type; }
	public void setType(TileType type) { this.type = type; }

	public TileImpl(int x, int y, TileType type) {
		super(x, y); setType(type);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && ((TileImpl)obj).getType() == getType();
	}
	public boolean equalCoords(TileImpl tile) { return super.equals(tile); }
	@Override
	public TileImpl clone() {
		return new TileImpl(getX(), getY(), getType());
	}
	@Override
	public String toString() {
		return "("+getX()+", "+getY()+") "+type;
	}
	@Override
	public Tile Clone() { return new TileImpl(getX(), getY(), getType()); }
	@Override
	public void drawGraphics(Graphics2D g2d, int xOffset, int yOffset) {
		g2d.setColor(getType().getColor());
		g2d.fillRect(xOffset, yOffset, getTileDimention(), getTileDimention());
	}
	@Override
	public int getTileDimention() { return AgentMonitor.TILE_PIXEL_DIMENTION; }
	@Override
	public void renderGraphics() {}
}
