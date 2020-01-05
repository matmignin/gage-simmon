package tv.gage.simon.engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCommand {

	public static enum CommandType {
		MOVE
	}
	
	private CommandType type;
	
}
