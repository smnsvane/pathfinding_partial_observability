package mapPurgers;

import java.util.List;

import core.Node;
import core.NodeGraph;

public class AdvPurger extends BasicPurger {

	private NodeGraph graph;

	public AdvPurger(String mapFilename) {
		super(mapFilename);
		System.out.println("map "+getMap());
		graph = new NodeGraph(getMap());
	}

	@Override
	public boolean purge(Node node) {
		boolean simpleRemove = super.purge(node);

		if (simpleRemove)
			return true;

		// if node is a part of a cluster dispose it
		List<Node> neighbors = graph.getNeighbors(node);
		if (isCluster(neighbors)) {
			node.disable();
			return true;
		}
		return false;
	}

	public boolean isCluster(List<Node> nodes) {
		for (int i = 0; i < nodes.size(); i++) {
			// if nodes.get(i) is a neighbor of all node in nodes parameter;
			List<Node> list = graph.getNeighbors(nodes.get(i));
			for (int j = 0; j < nodes.size(); j++) {
				if (!list.contains(nodes.get(j)))
					return false;
			}
		}
		return true;
	}
}
