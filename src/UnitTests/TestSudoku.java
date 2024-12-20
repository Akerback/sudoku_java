package UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import shared.model.Sudoku;

class TestSudoku {
	
	@Test
	void TestFromIndex() {
		final int[] CORRECT_ROWS = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 
			1, 1, 1, 1, 1, 1, 1, 1, 1, 
			2, 2, 2, 2, 2, 2, 2, 2, 2, 
			3, 3, 3, 3, 3, 3, 3, 3, 3, 
			4, 4, 4, 4, 4, 4, 4, 4, 4, 
			5, 5, 5, 5, 5, 5, 5, 5, 5, 
			6, 6, 6, 6, 6, 6, 6, 6, 6, 
			7, 7, 7, 7, 7, 7, 7, 7, 7, 
			8, 8, 8, 8, 8, 8, 8, 8, 8,
		};
		
		final int[] CORRECT_COLS = {
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8
		};
		
		final int[] CORRECT_SQRS = {
			0, 0, 0, 1, 1, 1, 2, 2, 2,
			0, 0, 0, 1, 1, 1, 2, 2, 2,
			0, 0, 0, 1, 1, 1, 2, 2, 2,
			3, 3, 3, 4, 4, 4, 5, 5, 5,
			3, 3, 3, 4, 4, 4, 5, 5, 5,
			3, 3, 3, 4, 4, 4, 5, 5, 5,
			6, 6, 6, 7, 7, 7, 8, 8, 8,
			6, 6, 6, 7, 7, 7, 8, 8, 8,
			6, 6, 6, 7, 7, 7, 8, 8, 8,
		};
		
		for (int i = 0; i < 81; i++) {
			assertEquals(CORRECT_ROWS[i], Sudoku.indexToRow(i));
			assertEquals(CORRECT_COLS[i], Sudoku.indexToColumn(i));
			assertEquals(CORRECT_SQRS[i], Sudoku.indexToSquare(i));
		}
	}
}
