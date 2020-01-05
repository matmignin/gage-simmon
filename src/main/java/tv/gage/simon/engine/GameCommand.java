package tv.gage.simon.engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameCommand {

	public static enum GameCommandType {
		START,
		OUT_OF_TIME,
		DEFAULT
	}
	
	private GameCommandType type;
	
}
