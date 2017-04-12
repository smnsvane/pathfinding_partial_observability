package agents;

import core.Agent;
import core.AgentMonitor;
import core.TileMapImpl;

public class SouthWalkAgent extends Agent {

	public SouthWalkAgent(AgentMonitor monitor, TileMapImpl knowledge) {
		super(monitor, knowledge);
	}

	@Override
	public void action() {
		getMonitor().moveAgent(getKnowledge().getAgentLocation().getX(),
				getKnowledge().getAgentLocation().getY() + 1);
	}
}
