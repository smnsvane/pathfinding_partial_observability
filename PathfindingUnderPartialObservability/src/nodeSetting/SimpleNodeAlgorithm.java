package nodeSetting;

import java.util.Collection;

import core.TileImpl;
import core.TileMapImpl;
import core.TileType;

public class SimpleNodeAlgorithm implements NodeSetter {

	@Override
	public boolean isValidNode(TileImpl tile, Collection<TileImpl> neighbors) {
		// count number of surrounding solid/obstacle tiles
		int shadowNeighbors = 0;
		for (TileImpl neighbor : neighbors)
			if (neighbor.getType().equals(TileType.SOLID))
				shadowNeighbors++;

		// accept if tile has one solid/obstacle tile as neighbor
		return shadowNeighbors == 1;
	}

	@Override
	public boolean isValidNode(int x, int y, TileMapImpl knowledge) {
		return isValidNode(knowledge.getTile(x, y), knowledge);
	}

	@Override
	public boolean isValidNode(TileImpl tile, TileMapImpl knowledge) {
		return isValidNode(tile, knowledge.getSurroundingTiles(tile));
	}
}
