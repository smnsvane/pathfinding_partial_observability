package nodeSetting;

import java.util.Collection;

import core.TileImpl;
import core.TileMapImpl;

public interface NodeSetter {

	public boolean isValidNode(TileImpl tile, Collection<TileImpl> neighbours);
	public boolean isValidNode(int x, int y, TileMapImpl knowledge);
	public boolean isValidNode(TileImpl tile, TileMapImpl knowledge);
}
