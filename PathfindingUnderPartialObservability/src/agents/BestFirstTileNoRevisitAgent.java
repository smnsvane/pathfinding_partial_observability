package agents;

import java.util.ArrayList;
import java.util.List;

import core.Agent;
import core.AgentMonitor;
import core.TileImpl;
import core.TileMapImpl;
import core.TileType;

public class BestFirstTileNoRevisitAgent extends Agent
{
	public BestFirstTileNoRevisitAgent(AgentMonitor monitor, TileMapImpl knowledge)
	{
		super(monitor, knowledge);
	}

	private List<TileImpl> visited = new ArrayList<TileImpl>();
	public List<TileImpl> getVisited() { return visited; }
	
	@Override
	public void action()
	{
		List<TileImpl> surroundingTiles =
			getKnowledge().getSurroundingTiles(getKnowledge().getAgentLocation());
		for (int i = 0; i < surroundingTiles.size(); i++)
			if (surroundingTiles.get(i).getType().equals(TileType.SOLID))
				surroundingTiles.remove(i--);
		surroundingTiles.removeAll(visited);
		TileImpl best = getTileClosestToGoal(surroundingTiles);
		if (best == null)
		{
			getMonitor().terminateAgent(false, "Can't find goal");
			return;
		}
		visited.add(best);
		getMonitor().moveAgent(best);
	}
}
