package tv.gage.simon;

import java.io.IOException;

import tv.gage.common.game.Game;
import tv.gage.common.game.Player;
import tv.gage.common.socket.SocketService;
import tv.gage.common.util.JsonUtils;
import tv.gage.simon.engine.GameCommand;
import tv.gage.simon.engine.PlayerCommand;
import tv.gage.simon.service.CommandService;

public class Simon extends Game {

	private CommandService commandService;

	public Simon(SocketService socketService, String gameCode) {
		super(Simon.class, socketService, gameCode, 4, 8);
		commandService = new CommandService(socketService, gameCode, getPlayers());
	}
	
	public void receiveGameCommand(String jsonCommand) {
		try {
			GameCommand gameCommand = (GameCommand) JsonUtils.jsonToObject(jsonCommand, GameCommand.class);
			System.out.println(String.format("Processing %s", jsonCommand));
			commandService.processGameCommand(gameCommand);
		}
		catch (IOException e) {
			
		}
	}

	public void receivePlayerCommand(Player player, String jsonCommand) {
		try {
			PlayerCommand playerCommand = (PlayerCommand) JsonUtils.jsonToObject(jsonCommand, PlayerCommand.class);
			System.out.println(String.format("Processing %s", jsonCommand));
			commandService.processPlayerCommand(player, playerCommand);
		}
		catch (IOException e) {
			
		}
	}
	
}
