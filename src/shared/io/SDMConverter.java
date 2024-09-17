package shared.io;

import javafx.util.StringConverter;
import shared.model.Sudoku;
import shared.utility.RuntimeAssert;

/**Converter for the .sdm format described here: https://www.sudocue.net/fileformats.php
 *
 */
public class SDMConverter extends StringConverter<Sudoku> {

	@Override
	public Sudoku fromString(String string) {
		String cleanedString = string.strip();

		RuntimeAssert.areEqual(cleanedString.length(), 81);

		Sudoku sudoku = new Sudoku();

		for (int i = 0; i < 81; i++) {
			String letter = cleanedString.substring(i, i + 1);
			int value = 0;
			try {
				value = Integer.parseInt(letter);
			}
			catch (NumberFormatException e) {
				value = 0;
			}

			sudoku.set(i, value);
		}

		return sudoku;
	}

	@Override
	public String toString(Sudoku sudoku) {
		String string = "";
		for (int i = 0; i < 81; i++) {
			string += Integer.toString(sudoku.get(i));
		}

		return string;
	}

}
