package tv.gage.simon.service;

import java.util.List;

import tv.gage.common.game.Player;
import tv.gage.common.messaging.Message;
import tv.gage.common.socket.SocketService;

public class BroadcastService {

	private SocketService socketService;
	private String gameCode;

	public BroadcastService(SocketService socketService, String gameCode) {
		this.socketService = socketService;
		this.gameCode = gameCode;
	}
	
	public void broadcastToPlayer(Player player, Object payload) {
		Message message = Message.builder()
				.gameCode(gameCode)
				.player(player)
				.payload(payload)
				.build();
		socketService.sendPlayerMessage(message);
	}
	
	public void broadcastToPlayers(List<Player> players, Object payload) {
		Message message = Message.builder()
				.gameCode(gameCode)
				.players(players)
				.payload(payload)
				.build();
		socketService.sendPlayerMessage(message);
	}
	
	public void broadcastToGame(Object payload) {
		Message message = Message.builder()
				.gameCode(gameCode)
				.payload(payload)
				.build();
		socketService.sendGameMessage(message);
	}
	
}
