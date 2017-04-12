package basic;

import java.awt.Image;

public class Agent<T extends Tile> implements TileEntity
{
	private Image image;
	@Override
	public Image getImage() { return image; }
	private T location;
	@Override
	public T getLocation() { return location; }
	public void teleport(T destination) { location = destination; }
	@Override
	public void renderGraphics() {}
	
	public Agent(Image image, T start)
	{
		this.image = image;
		location = start;
	}
}
