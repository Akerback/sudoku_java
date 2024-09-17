package shared.utility;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class Constraints {
	public static ColumnConstraints percentWidth(double percent) {
		ColumnConstraints result = new ColumnConstraints();
		result.setPercentWidth(percent);
		
		return result;
	}
	
	public static RowConstraints percentHeight(double percent) {
		RowConstraints result = new RowConstraints();
		result.setPercentHeight(percent);
		
		return result;
	}
}
