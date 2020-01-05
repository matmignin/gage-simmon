package tv.gage.simon;

import java.io.IOException;

import tv.gage.common.game.Game;
import tv.gage.common.game.Player;
import tv.gage.common.socket.SocketService;
import tv.gage.common.util.JsonUtils;
import tv.gage.simon.engine.GameCommand;
import tv.gage.simon.engine.PlayerCommand;
import tv.gage.simon.service.BroadcastService;
import tv.gage.simon.service.EngineService;

public class Simon extends Game {

	private BroadcastService broadcastService;
	private EngineService engineService;

	public Simon(SocketService socketService, String gameCode) {
		super(Simon.class, socketService, gameCode, 4, 8);
		broadcastService = new BroadcastService(socketService, gameCode);
		engineService = new EngineService(broadcastService, gameCode, players);
	}
	
	public void receiveGameCommand(String jsonCommand) {
		try {
			GameCommand command = (GameCommand) JsonUtils.jsonToObject(jsonCommand, GameCommand.class);
			switch (command.getType()) {
			case START:
				engineService.startGame();
				break;
			case OUT_OF_TIME:
				engineService.outOfTime();
				break;
			default:
			}
		}
		catch (IOException e) {
			broadcastService.broadcastToGame(String.format("Error parsing command %s", jsonCommand));
		}
	}

	public void receivePlayerCommand(Player player, String jsonCommand) {
		try {
			PlayerCommand command = (PlayerCommand) JsonUtils.jsonToObject(jsonCommand, PlayerCommand.class);
			switch (command.getType()) {
			case MOVE:
				engineService.playerMove(player);
				break;
			default:
			}
		}
		catch (IOException e) {
			broadcastService.broadcastToPlayer(player, String.format("Error parsing command %s", jsonCommand));
		}
	}
	
}
