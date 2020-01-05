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

	public static enum CommandType {
		START,
		OUT_OF_TIME
	}
	
	private CommandType type;
	private String data;
	
}
