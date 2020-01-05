package tv.gage.simon.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tv.gage.common.game.Player;
import tv.gage.simon.service.BroadcastService;

public class Engine {

	private BroadcastService broadcastService;
	private List<Player> players;
	private List<Player> moves = new ArrayList<Player>();
	private int moveIndex = 0;
	private boolean running;
	
	public Engine(BroadcastService broadcastService, String gameCode, List<Player> players) {
		this.broadcastService = broadcastService;
		this.players = players;
	}
	
	public void startGame() {
		if (!players.isEmpty()) {
			setRunning(true);
			resetMoves();
			nextRound();
			broadcastService.broadcastToPlayers(players, "start");
		}
	}
	
	public void playerMove(Player player) {
		if (running) {
			if (moves.get(moveIndex) == player) {
				broadcastCorrectMove(player);
				nextMove();
			}
			else {
				setRunning(false);
				distributePointsToEveryoneBut(player);
				broadcastIncorrectMove(player);
			}
		}
	}
	
	public void outOfTime() {
		if (running) {
			setRunning(false);
			broadcastService.broadcastToPlayers(players, "out of time");
			Player player = moves.get(moveIndex);
			distributePointsToEveryoneBut(player);
		}
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean isRunning() {
		return running;
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
		moves.add(randomPlayer());
	}
	
	public List<Player> getMoves() {
		return moves;
	}

	private void resetMoveIndex() {
		moveIndex = 0;
	}
	
	private Player randomPlayer() {
		int index = new Random().ints(0, (players.size() - 1) + 1).findFirst().getAsInt();
		return players.get(index);
	}
	
	private void distributePointsToEveryoneBut(Player losingPlayer) {
		players.forEach(player -> {
			if (player != losingPlayer) {
				player.setScore(player.getScore() + 1);
			}
		});
	}

}
