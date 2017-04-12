package core;

import java.util.List;

public abstract class Agent
{
	private AgentMonitor monitor;
	public AgentMonitor getMonitor() { return monitor; }
	private TileMapImpl knowledge;
	public TileMapImpl getKnowledge() { return knowledge; }
	
	public Agent(AgentMonitor monitor, TileMapImpl knowledge)
	{
		this.monitor = monitor;
		this.knowledge = knowledge;
		graph = new NodeGraph(getKnowledge());
		for (TileImpl tile : getKnowledge())
			if (tile.getType().equals(TileType.NODE))
				getGraph().addNode((Node) tile);
	}
	
	public abstract void action();
	
	public TileImpl getTileClosestToGoal(List<TileImpl> list)
	{
		if (list.isEmpty()) return null;
		TileImpl best = list.get(0);
		for (int i = 1; i < list.size(); i++)
			if (list.get(i).fieldDistance(getMonitor().getGoalLocation()) <
					best.fieldDistance(getMonitor().getGoalLocation()))
				best = list.get(i);
		return best;
	}
	
	private NodeGraph graph;
	public NodeGraph getGraph() { return graph; }
}
