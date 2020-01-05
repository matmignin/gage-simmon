package tv.gage.simon.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tv.gage.common.game.Player;
import tv.gage.common.socket.SocketService;
import tv.gage.simon.service.BroadcastService;

public class Engine {

	private BroadcastService broadcastService;
	private List<Player> players;
	private List<String> moves = new ArrayList<String>();
	private int moveIndex = 0;
	
	
	public Engine(SocketService socketService, String gameCode, List<Player> players) {
		this.broadcastService = new BroadcastService(socketService, gameCode);
		this.players = players;
	}
	
	public void startGame() {
		resetMoves();
		nextRound();
		broadcastService.broadcastToPlayers(players, "start");
	}
	
	public void playerMove(Player player) {
		if (moves.get(moveIndex) == player.getPlayerCode()) {
			broadcastCorrectMove(player);
			nextMove();
		}
		else {
			distributePoints(player);
			broadcastIncorrectMove(player);
		}
	}
	
	public void outOfTime() {
		
	}
	
	private void nextRound() {
		addMove();
		resetMoveIndex();
		broadcastService.broadcastToGame(moves);
	}
	
	private void nextMove() {
		moveIndex++;
		if (moveIndex == moves.size()) {
			nextRound();
		}
	}
	
	private void broadcastCorrectMove(Player player) {
		String payload = "correct";
		broadcastService.broadcastToPlayer(player, payload);
		broadcastService.broadcastToGame(payload);
	}
	
	private void broadcastIncorrectMove(Player player) {
		String payload = "incorrect";
		broadcastService.broadcastToPlayer(player, payload);
		broadcastService.broadcastToGame(payload);
	}
	
	private void resetMoves() {
		moves.clear();
	}
	
	private void addMove() {
		moves.add(randomPlayer().getPlayerCode());
		broadcastService.broadcastToPlayers(players, moves.toString());
	}
	
	private void resetMoveIndex() {
		moveIndex = 0;
	}
	
	private Player randomPlayer() {
		int index = new Random().ints(0, (players.size())).findFirst().getAsInt();
		return players.get(index);
	}
	
	private void distributePoints(Player losingPlayer) {
		players.forEach(player -> {
			if (player != losingPlayer) {
				player.setScore(player.getScore() + 1);
			}
		});
	}

}
