package tv.gage.simon;

import java.io.IOException;

import tv.gage.common.exception.PlayerRosterFullException;
import tv.gage.common.exception.UnknownPlayerException;
import tv.gage.common.game.Game;
import tv.gage.common.game.Player;
import tv.gage.common.messaging.BroadcastService;
import tv.gage.common.util.JsonUtils;
import tv.gage.simon.engine.GameCommand;
import tv.gage.simon.engine.PlayerCommand;
import tv.gage.simon.service.EngineService;

public class Simon extends Game {

	private EngineService engineService;

	public Simon(BroadcastService broadcastService, String gameCode) {
		super(Simon.class, broadcastService, gameCode, 4, 8);
		this.engineService = new EngineService(broadcastServiceHelper, players);
	}
	
	@Override
	public void addPlayer(Player player) throws PlayerRosterFullException {
		super.addPlayer(player);
		broadcastServiceHelper.broadcastToGame(String.format("Added %s", player.getName()));
	}
	
	@Override
	public void removePlayer(Player player) throws UnknownPlayerException {
		super.removePlayer(player);
		broadcastServiceHelper.broadcastToGame(String.format("Removed %s", player.getName()));
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
			broadcastServiceHelper.broadcastToGame(String.format("Error parsing command %s", jsonCommand));
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
			broadcastServiceHelper.broadcastToPlayer(player, String.format("Error parsing command %s", jsonCommand));
		}
	}
	
}
