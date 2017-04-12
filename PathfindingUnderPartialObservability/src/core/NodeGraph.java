package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeGraph {
	
	/* ***********************************
	 * CONSTRUCTOR AND BASE FUNCTIONALITY
	 *************************************/

	private TileMapImpl knowledge;

	public NodeGraph(TileMapImpl agentKnowledge) {
		this.knowledge = agentKnowledge;
		allNodes = new HashMap<Integer, Node>();
		// rebuild graph from previous run if possible
		for (TileImpl tile : agentKnowledge.getAllTiles(TileType.NODE))
			addNode((Node) tile);
	}

	/* ***********************************
	 * NODE STRUCTURE AND UTILITY METHODS
	 *************************************/

	private HashMap<Integer, Node> allNodes;
	public static int toKey(Node n) { return n.getX() + n.getY()*10000; }
	public Node getNode(Node key) { return allNodes.get(toKey(key)); }
	public void addNode(Node n) { allNodes.put(toKey(n), n); }
	public void removeNode(Node n) { allNodes.remove(n); }

	public List<Node> getNeighbors(Node home) {
		List<Node> neighbors = new ArrayList<Node>();
		for (Node node : allNodes.values()) {
			if (home.equals(node)) continue;
			if (knowledge.clearLineBetween(home, node))
				neighbors.add(node);
		}
		return neighbors;
	}

	/* **************************************
	 * HEURISTIC AND TRAVEL DISTANCE METHODS
	 ****************************************/

	public void updateGScore(Node homeNode) {
		for (Node node : allNodes.values())
			node.setG(Double.POSITIVE_INFINITY);
		setGScore(getNode(homeNode), 0.0, null, Double.POSITIVE_INFINITY);
	}
	private void setGScore(Node node, double gScore, Node parrent, double bestFScore) {
		// Optimization
		if (gScore > bestFScore) return;

		node.setG(gScore);
		node.setParrent(parrent);

		// Optimization
		if (!node.isVisited() && bestFScore > node.getF())
			bestFScore = node.getF();

		for (Node neighbor : getNeighbors(node)) {
			double newGScore = gScore + node.fieldDistance(neighbor);
			if (neighbor.getG() > newGScore)
				setGScore(neighbor, newGScore, node, bestFScore);
		}
	}

	public Node getUnvisitedMinFNode() {
		double f = Double.POSITIVE_INFINITY;
		Node subGoalNode = null;
		for (Node node : allNodes.values()) {
			if (node.isVisited()) continue;
			if (node.getF() < f) {
				f = node.getF();
				subGoalNode = node;
			}
		}
		return subGoalNode;
	}

	@Override
	public String toString() { return allNodes.toString(); }
}
