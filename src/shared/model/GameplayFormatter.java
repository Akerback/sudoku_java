package shared.model;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

public class GameplayFormatter extends ASudokuFormatter {
	@Override
	public StringConverter<Integer> getConverter() {
		return new StringConverter<>() {
			@Override
			public Integer fromString(String text) {
				try {
					int converted = Integer.parseInt(text);
					if ((converted >= 1) && (converted <= 9)) {
						return converted;
					}
					else {
						return 0;
					}
				}
				catch (NumberFormatException e) {
					return 0;
				}
			}

			@Override
			public String toString(Integer number) {
				if ((number == null) || (number <= 0) || (number > 9)) {
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
				String fullText = t.getControlNewText();
				if (fullText.matches("[1-9]?")) {
					return t;
				}

				String changeText = t.getText();
				if (changeText.matches("[1-9]?")) {
					//If there is a number entered, replace all the text with the entered number
					if (fullText.length() > 0) {
						t.setRange(0, t.getControlNewText().length() - 1);
					}

					return t;
				}
				else {
					return null;
				}
			}
		};
	}
}
