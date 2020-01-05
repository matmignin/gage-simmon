package tv.gage.simon.service;

import java.util.List;

import tv.gage.common.game.Player;
import tv.gage.common.socket.SocketService;
import tv.gage.simon.engine.Engine;
import tv.gage.simon.engine.GameCommand;
import tv.gage.simon.engine.PlayerCommand;

public class CommandService {
	
	private Engine engine;
	
	public CommandService(SocketService socketService, String gameCode, List<Player> players) {
		engine = new Engine(socketService, gameCode, players);
	}

	public void processGameCommand(GameCommand command) {
		switch (command.getType()) {
		case START:
			engine.startGame();
			break;
		case OUT_OF_TIME:
			engine.outOfTime();
			break;
		default:
		}
	}

	public void processPlayerCommand(Player player, PlayerCommand command) {
		switch (command.getType()) {
		case MOVE:
			engine.playerMove(player);
			break;
		default:
		}
	}
	
}
