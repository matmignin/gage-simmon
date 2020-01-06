package tv.gage.simon.service;

import java.util.List;

import tv.gage.common.game.Player;
import tv.gage.common.messaging.BroadcastServiceHelper;
import tv.gage.simon.engine.Engine;

public class EngineService {

	private Engine engine;
	
	public EngineService(BroadcastServiceHelper broadcastServiceHelper, List<Player> players) {
		this.engine = new Engine(broadcastServiceHelper, players);
	}
	
	public void startGame() {
		engine.startGame();
	}
	
	public void outOfTime() {
		engine.outOfTime();
	}
	
	public void playerMove(Player player) {
		engine.playerMove(player);
	}
	
}
