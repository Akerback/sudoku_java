package shared.model;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

public class DebugFormatter extends ASudokuFormatter {
	@Override
	public StringConverter<Integer> getConverter() {
		return new StringConverter<>() {
			@Override
			public Integer fromString(String text) {
				try {
					return Integer.parseInt(text);
				}
				catch (NumberFormatException e) {
					return null;
				}
			}

			@Override
			public String toString(Integer number) {
				if (number == null) {
					return "";
				}
				else {
					return Integer.toString(number);
				}
			}
		};
	}

	@Override
	public UnaryOperator<Change> getFilter() {
		return new UnaryOperator<>() {
			@Override
			public Change apply(Change t) {
				String text = t.getText();

				try {
					Integer.parseInt(text);
					return t;
				}
				catch (NumberFormatException e) {
					return null;
				}
			}
		};
	}
}
