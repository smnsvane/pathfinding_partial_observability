package mapPurgers;

import nodeSetting.TunnelNodeAlgorithm;

import core.Node;

public class BasicPurger extends AbstractPurger {

	private TunnelNodeAlgorithm alg;
	public TunnelNodeAlgorithm getAlg() { return alg; }

	public BasicPurger(String mapFilename) {
		super(mapFilename);
		alg = new TunnelNodeAlgorithm();
	}

	@Override
	public boolean purge(Node node) {
		if (!getAlg().isValidNode(node, getMap())) {
			node.disable();
			return true;
		}
		return false;
	}
}
