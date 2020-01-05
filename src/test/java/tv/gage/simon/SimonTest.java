package tv.gage.simon;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import tv.gage.common.exception.PlayerRosterFullException;
import tv.gage.common.exception.UnknownPlayerException;
import tv.gage.common.game.Player;
import tv.gage.common.messaging.BroadcastService;
import tv.gage.common.messaging.Message;
import tv.gage.common.util.JsonUtils;
import tv.gage.simon.engine.GameCommand;
import tv.gage.simon.engine.GameCommand.GameCommandType;
import tv.gage.simon.engine.PlayerCommand;
import tv.gage.simon.engine.PlayerCommand.PlayerCommandType;

public class SimonTest {
	
	private BroadcastService broadcastService = new BroadcastService() {
		public void sendPlayerMessage(Message message) {}
		public void sendGameMessage(Message message) {}
	};

	@Test
	public void instantiationTest() {
		String gameCode = "SIMN";
		Simon simon = new Simon(broadcastService, gameCode);
		assertEquals(gameCode, simon.getGameCode());
		assertEquals(4, simon.getMinNumberOfPlayers());
		assertEquals(8, simon.getMaxNumberOfPlayers());
	}
	
	@Test
	public void receiveStartCommand() throws JsonProcessingException {
		GameCommand command = new GameCommand(GameCommandType.START);
		String jsonCommand = JsonUtils.ObjectToJson(command);
		Simon simon = new Simon(broadcastService, "SIMN");
		simon.receiveGameCommand(jsonCommand);
	}

	@Test
	public void receiveOutOfTimeCommand() throws JsonProcessingException {
		GameCommand command = new GameCommand(GameCommandType.OUT_OF_TIME);
		String jsonCommand = JsonUtils.ObjectToJson(command);
		Simon simon = new Simon(broadcastService, "SIMN");
		simon.receiveGameCommand(jsonCommand);
	}
	
	@Test
	public void receiveDefaultGameCommand() throws JsonProcessingException {
		GameCommand command = new GameCommand(GameCommandType.DEFAULT);
		String jsonCommand = JsonUtils.ObjectToJson(command);
		Simon simon = new Simon(broadcastService, "SIMN");
		simon.receiveGameCommand(jsonCommand);
	}

	@Test
	public void receiveNonParsableGameCommand() throws JsonProcessingException {
		String jsonCommand = "nonsenseJson";
		Simon simon = new Simon(broadcastService, "SIMN");
		simon.receiveGameCommand(jsonCommand);
	}
	
	@Test
	public void receiveMoveCommand() throws JsonProcessingException, PlayerRosterFullException {
		PlayerCommand command = new PlayerCommand(PlayerCommandType.MOVE);
		String jsonCommand = JsonUtils.ObjectToJson(command);
		Simon simon = new Simon(broadcastService, "SIMN");
		Player player = Player.builder().playerCode("GAGE").build();
		simon.addPlayer(player);
		simon.receivePlayerCommand(player, jsonCommand);
	}
	
	@Test
	public void receiveDefaultPlayerCommand() throws JsonProcessingException, PlayerRosterFullException {
		PlayerCommand command = new PlayerCommand(PlayerCommandType.DEFAULT);
		String jsonCommand = JsonUtils.ObjectToJson(command);
		Simon simon = new Simon(broadcastService, "SIMN");
		Player player = Player.builder().playerCode("GAGE").build();
		simon.addPlayer(player);
		simon.receivePlayerCommand(player, jsonCommand);
	}

	@Test
	public void receiveNonParsablePlayerCommand() throws PlayerRosterFullException {
		String jsonCommand = "nonsenseJson";
		Simon simon = new Simon(broadcastService, "SIMN");
		Player player = Player.builder().playerCode("GAGE").build();
		simon.addPlayer(player);
		simon.receivePlayerCommand(player, jsonCommand);
	}
	
	@Test
	public void addPlayerTest() throws PlayerRosterFullException {
		Simon simon = new Simon(broadcastService, "SIMN");
		Player player = Player.builder().playerCode("PLYR").build();
		simon.addPlayer(player);
		assertEquals(1, simon.getPlayers().size());
	}

	@Test
	public void removePlayerTest() throws PlayerRosterFullException, UnknownPlayerException {
		Simon simon = new Simon(broadcastService, "SIMN");
		Player player1 = Player.builder().playerCode("PLYR").build();
		Player player2 = Player.builder().playerCode("WINR").build();
		simon.addPlayer(player1);
		simon.addPlayer(player2);
		simon.removePlayer(player1);
		assertEquals(1, simon.getPlayers().size());
	}

}
