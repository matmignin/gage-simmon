package tv.gage.simon.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import tv.gage.common.game.Player;
import tv.gage.common.messaging.Message;
import tv.gage.common.socket.SocketService;

public class BroadcastServiceTest {

	private SocketService socketService = new SocketService() {
		public void sendPlayerMessage(Message message) {}
		public void sendGameMessage(Message message) {}
	};
	
	@Test
	public void broadcastToPlayer() {
		BroadcastService broadcastService = new BroadcastService(socketService, "SIMN");
		Player player = Player.builder().playerCode("PLYR").build();
		broadcastService.broadcastToPlayer(player, "Test");
	}
	
	@Test
	public void broadcastToPlayersTest() {
		BroadcastService broadcastService = new BroadcastService(socketService, "SIMN");
		List<Player> players = Arrays.asList(new Player[] {
				Player.builder().playerCode("PLYR").build(),
				Player.builder().playerCode("WINR").build()
		});
		broadcastService.broadcastToPlayers(players, "Test");
	}

	@Test
	public void broadcastToGameTest() {
		BroadcastService broadcastService = new BroadcastService(socketService, "SIMN");
		broadcastService.broadcastToGame("Test");
	}

}
