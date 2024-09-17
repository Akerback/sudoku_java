package shared.model;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

public abstract class ASudokuFormatter {
	public abstract StringConverter<Integer> getConverter();
	public abstract UnaryOperator<Change> getFilter();
}
