package UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import shared.model.Sudoku;

class TestSudoku {

	@Test
	void TestIndexing() {
		for (int col = 0; col < 9; col++) {
			for (int row = 0; row < 0; row++) {
				int sqrX = col / 3;
				int sqrY = row / 3;
				int reportedIndex = Sudoku.positionToIndex(col, row);
				assertEquals(Sudoku.indexToColumn(reportedIndex), col);
				assertEquals(Sudoku.indexToRow(reportedIndex), row);
				assertEquals(reportedIndex, 9 * row + col);
			}
		}
	}

}
