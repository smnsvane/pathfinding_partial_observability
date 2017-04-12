package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.Agent;
import core.AgentMonitor;
import core.Node;
import core.NodeGraph;
import core.SaveRestore;
import core.TileImpl;
import core.TileMapImpl;
import static core.TileType.*;

import nodeSetting.NodeSetter;
import nodeSetting.TunnelNodeAlgorithm;

public class IWA_StarAgent extends Agent
{
	private NodeGraph graph;
	
	private NodeSetter nodeSettingAproach;
	
	public IWA_StarAgent(AgentMonitor monitor, TileMapImpl knowledge)
	{
		super(monitor, knowledge);
		graph = new NodeGraph(getKnowledge());
		for (TileImpl node : getKnowledge().getAllTiles(NODE))
			graph.addNode((Node) node);
		
		// make sure that agents starting tile is a node
		TileImpl startTile = getKnowledge().getTile(
				getKnowledge().getAgentLocation().getX(),
				getKnowledge().getAgentLocation().getY());
		if (!startTile.equals(NODE))
			createNode(startTile);
		
		// add goal as a node to the graph
		TileImpl goal = getMonitor().getGoalLocation();
		graph.addNode(new Node(goal, goal));
		
		// initialize node setting algorithm
		nodeSettingAproach = new TunnelNodeAlgorithm();
	}
	
	public void placeNewNodes()
	{
		for (TileImpl tile : getKnowledge().getAllTiles(CLEAR))
			if (nodeSettingAproach.isValidNode(tile, getKnowledge()))
				createNode(tile);
	}
	
	public void createNode(int x, int y) { createNode(getKnowledge().getTile(x, y)); }
	public void createNode(TileImpl location)
	{
		Node node = new Node(location, getMonitor().getGoalLocation());
		getKnowledge().setTile(node);
		graph.addNode(node);
	}
	public void removeNode(Node node)
	{
		graph.removeNode(node);
		getKnowledge().setTile(new TileImpl(node.getX(), node.getY(), CLEAR));
	}
	private void saveKnowledge()
	{
		try { SaveRestore.save(getKnowledge(), getMonitor().filename); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	private List<TileImpl> walkPath = new ArrayList<TileImpl>();
	private List<Node> nodePath = new ArrayList<Node>();
	private void buildwalkPath()
	{
		Node dist = nodePath.remove(0);
		List<TileImpl> list =
			getKnowledge().bresenhamLineAlgorithm
			(
				getKnowledge().getAgentLocation().getX(),
				getKnowledge().getAgentLocation().getY(),
				dist.getX(),
				dist.getY()
			);
		
		// if first walk location is in same location as agent...
		if (list.get(0).equalCoords(getKnowledge().getAgentLocation()))
		{
			list.remove(0);
			walkPath.addAll(list);
		}
		else
			// reverse walk list
			for (int i = list.size() - 2; i >= 0; i--)
				walkPath.add(list.get(i));
	}
	@Override
	public void action()
	{
		// if walk path contains no tiles
		if (walkPath.isEmpty())
		{
			// if node path contains no waypoints (nodes)
			if (nodePath.isEmpty())
			{
				// if goal have been reached...
				if (getMonitor().getGoalLocation().equalCoords(getKnowledge().getAgentLocation()))
				{
					// save known map
					saveKnowledge();
					// terminate agent
					getMonitor().terminateAgent(true, "goal reached");
				}
				
				// build node path
				iwaStar();
				
				// if node does not qualify
				Node current = (Node) getKnowledge().getAgentLocation();
				if (!nodeSettingAproach.isValidNode(current, getKnowledge()))
					removeNode(current);
			}
			
			// build walk path
			buildwalkPath();
		}
		// move agent
		getMonitor().moveAgent(walkPath.remove(0));
	}
	
	public void iwaStar()
	{
		// agent location
		Node x = graph.getNode((Node) getKnowledge().getAgentLocation());
		
		// if node is visited for the first time...
		if (!x.isVisited())
		{
			// mark node as visited
			x.setVisited(true);
		
			// generate neighbors
			placeNewNodes();
		}
		
		// update g score for all nodes in graph
		// (g score = travel distance from current location)
		graph.updateGScore((Node) getKnowledge().getAgentLocation());
		
		// get unvisited node with the best f score
		// (f score = g score + h score)
		Node subgoal = graph.getUnvisitedMinFNode();
		
		// if all nodes have been visited
		// and goal have not been reached...
		if (subgoal == null)
			// return failure
			getMonitor().terminateAgent(false, "could not find goal");
		
		// neighbors of current agent location
		List<Node> neighbors = graph.getNeighbors(x);
		
		// build node path that leads to the current subgoal
		nodePath.add(0, subgoal);
		while (!neighbors.contains(subgoal))
		{
			subgoal = subgoal.getParrent();
			nodePath.add(0, subgoal);
		}
	}
}
