package tv.gage.simon.service;

import java.util.List;

import tv.gage.common.game.Player;
import tv.gage.simon.engine.Engine;

public class EngineService {

	private Engine engine;
	
	public EngineService(BroadcastService broadcastService, String gameCode, List<Player> players) {
		this.engine = new Engine(broadcastService, gameCode, players);
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
