package basic;

public abstract class AbstractTile implements Tile {

	private static final long serialVersionUID = 1L;

	private int x, y;
	@Override
	public int getX() { return x; }
	@Override
	public void setX(int x) { this.x = x; }
	@Override
	public int getY() { return y; }
	@Override
	public void setY(int y) { this.y = y; }
	
	public AbstractTile() {}
	public AbstractTile(int x, int y) { this.x = x; this.y = y; }
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof AbstractTile) &&
			((AbstractTile)obj).x == x &&
			((AbstractTile)obj).y == y;
	}
	@Override
	public double fieldDistance(Tile tile)
	{
		double xDist = Math.abs(getX() - tile.getX());
		double yDist = Math.abs(getY() - tile.getY());
		double diagonalDist = Math.min(xDist, yDist) * Math.sqrt(2);
		double linearDist = Math.max(xDist, yDist) - Math.min(xDist, yDist);
		return diagonalDist + linearDist;
	}
}
