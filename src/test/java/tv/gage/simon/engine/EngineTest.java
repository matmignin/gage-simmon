package tv.gage.simon.engine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import tv.gage.common.game.Player;
import tv.gage.common.messaging.Message;
import tv.gage.common.socket.SocketService;
import tv.gage.simon.service.BroadcastService;

public class EngineTest {
	
	private SocketService socketService = new SocketService() {
		public void sendPlayerMessage(Message message) {}
		public void sendGameMessage(Message message) {}
	};
	
	private BroadcastService broadcastService = new BroadcastService(socketService, "SIMN");

	@Test
	public void startGameTest() {
		List<Player> players = Arrays.asList(new Player[] {
				Player.builder().playerCode("PLYR").build(),
				Player.builder().playerCode("WINR").build()
		});
		Engine engine = new Engine(broadcastService, "SIMN", players);
		engine.startGame();
		assertEquals(true, engine.isRunning());
	}
	
	@Test
	public void startGameWithoutEnoughPlayersTest() {
		Engine engine = new Engine(broadcastService, "SIMN", new ArrayList<Player>());
		engine.startGame();
		assertEquals(false, engine.isRunning());
	}
	
	@Test
	public void playerCorrectMoveTest() {
		Player player = Player.builder().playerCode("PLYR").build();
		List<Player> players = Arrays.asList(new Player[] {player});
		Engine engine = new Engine(broadcastService, "SIMN", players);
		engine.startGame();
		engine.playerMove(player);
		assertEquals(2, engine.getMoves().size());
	}
	
	@Test
	public void playerNextRountTest() {
		Player player = Player.builder().playerCode("PLYR").build();
		List<Player> players = Arrays.asList(new Player[] {player});
		Engine engine = new Engine(broadcastService, "SIMN", players);
		engine.startGame();
		engine.playerMove(player);
		engine.playerMove(player);
		engine.playerMove(player);
		assertEquals(3, engine.getMoves().size());
	}
	
	@Test
	public void playerMoveIncorrectTest() {
		Player player1 = Player.builder().playerCode("PLYR").build();
		Player player2 = Player.builder().playerCode("WINR").build();
		List<Player> players = Arrays.asList(new Player[] {player1});
		Engine engine = new Engine(broadcastService, "SIMN", players);
		engine.startGame();
		engine.playerMove(player2);
		assertEquals(false, engine.isRunning());
	}

	@Test
	public void playerMoveWithoutRunningTest() {
		Player player = Player.builder().playerCode("PLYR").build();
		List<Player> players = Arrays.asList(new Player[] {player});
		Engine engine = new Engine(broadcastService, "SIMN", players);
		engine.playerMove(player);
		assertEquals(0, engine.getMoves().size());
	}
	
	@Test
	public void outOfTimeTest() {
		Player player = Player.builder().playerCode("PLYR").build();
		List<Player> players = Arrays.asList(new Player[] {player});
		Engine engine = new Engine(broadcastService, "SIMN", players);
		engine.startGame();
		engine.outOfTime();
		assertEquals(false, engine.isRunning());
	}
	
	@Test
	public void outOfTimeWithoutRunningTest() {
		Player player = Player.builder().playerCode("PLYR").build();
		List<Player> players = Arrays.asList(new Player[] {player});
		Engine engine = new Engine(broadcastService, "SIMN", players);
		engine.outOfTime();
		assertEquals(false, engine.isRunning());
	}
	
	@Test
	public void distributePointsToEveryoneButTest() {
		Player player1 = Player.builder().playerCode("PLYR").build();
		Player player2 = Player.builder().playerCode("WINR").build();
		List<Player> players = new ArrayList<Player>(Arrays.asList(new Player[] {player1}));
		Engine engine = new Engine(broadcastService, "SIMN", players);
		engine.startGame();
		players.add(player2);
		engine.playerMove(player2);
		assertEquals(1, players.get(0).getScore());
		assertEquals(0, players.get(1).getScore());
	}
	
}
