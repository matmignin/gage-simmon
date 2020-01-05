package tv.gage.simon.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import tv.gage.common.game.Player;
import tv.gage.common.messaging.BroadcastServiceHelper;

public class Engine {

	private BroadcastServiceHelper broadcastServiceHelper;
	private List<Player> players;
	private List<Player> moves = new ArrayList<Player>();
	private int moveIndex = 0;
	private boolean running;
	
	public Engine(BroadcastServiceHelper broadcastServiceHelper, List<Player> players) {
		this.broadcastServiceHelper = broadcastServiceHelper;
		this.players = players;
	}
	
	public void startGame() {
		if (!players.isEmpty()) {
			setRunning(true);
			resetMoves();
			nextRound();
			broadcastServiceHelper.broadcastToPlayers(players, "start");
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
			broadcastServiceHelper.broadcastToPlayers(players, "out of time");
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
		broadcastServiceHelper.broadcastToGame(
				moves.stream().map(player -> player.getName())
				.collect(Collectors.toList()));
	}
	
	private void nextMove() {
		moveIndex++;
		if (moveIndex == moves.size()) {
			nextRound();
		}
	}
	
	private void broadcastCorrectMove(Player player) {
		String payload = "correct";
		broadcastServiceHelper.broadcastToPlayer(player, payload);
		broadcastServiceHelper.broadcastToGame(payload);
	}
	
	private void broadcastIncorrectMove(Player losingPlayer) {
		String payload = "incorrect";
		broadcastServiceHelper.broadcastToGame(payload);
		broadcastServiceHelper.broadcastToPlayer(losingPlayer, payload);
		broadcastServiceHelper.broadcastToPlayers(players.stream()
				.filter(player -> player != losingPlayer)
				.collect(Collectors.toList()), "win");
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
