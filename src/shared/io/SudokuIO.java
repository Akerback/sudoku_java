package shared.io;

import java.nio.file.Path;

import javafx.util.StringConverter;
import shared.model.Sudoku;

public class SudokuIO {
	//TODO: implement io
	public static void write(Path path, Sudoku sudoku, StringConverter<Sudoku> converter) {

	}

	public static Sudoku read(Path path, StringConverter<Sudoku> converter) {
		throw new UnsupportedOperationException("SudokuIO.read has not been implemented!");
	}
}
