package agents;

import java.util.List;

import core.Agent;
import core.AgentMonitor;
import core.TileImpl;
import core.TileMapImpl;
import core.TileType;

public class BestFirstTileAgent extends Agent {

	public BestFirstTileAgent(AgentMonitor monitor, TileMapImpl knowledge) {
		super(monitor, knowledge);
	}
	
	@Override
	public void action() {
		List<TileImpl> list =
			getKnowledge().getSurroundingTiles(getKnowledge().getAgentLocation());
		for (int i = 0; i < list.size(); i++)
			if (!list.get(i).getType().equals(TileType.CLEAR))
				list.remove(i--);
		TileImpl best = getTileClosestToGoal(list);
		getMonitor().moveAgent(best);
	}
}
