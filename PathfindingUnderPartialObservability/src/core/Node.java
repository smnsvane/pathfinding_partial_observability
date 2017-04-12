package core;

import java.io.Serializable;

public class Node extends TileImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	public Node(int x, int y, TileImpl goal) {
		super(x, y, TileType.NODE);
		setH(fieldDistance(goal));
		setG(Double.POSITIVE_INFINITY);
	}
	public Node(TileImpl tile, TileImpl goal) {
		this(tile.getX(), tile.getY(), goal);
	}

	private boolean visited = false;
	public boolean isVisited() { return visited; }
	public void setVisited(boolean visited) { this.visited = visited; }

	private Node parrent;
	public Node getParrent() { return parrent; }
	public void setParrent(Node parrent) { this.parrent = parrent; }

	private double g, h;
	public double getG() { return g; }
	public void setG(double g) { this.g = g; }
	public double getH() { return h; }
	public void setH(double h) { this.h = h; }

	public double getF() { return getH() + getG(); }

	@Override
	public String toString() {
		return super.toString() + " G:"+getG()+" H:"+getH();
	}

	@Override
	public void setType(TileType type) {
		if (getType() != null)
			throw new RuntimeException("can't change the type of node");
		super.setType(type);
	}
	public void disable() { super.setType(TileType.DISABLED_NODE); }
}
