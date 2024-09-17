package UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import shared.model.SudokuSelection;

class TestSelection {

	@Test
	void TestBasicRows() {
		SudokuSelection rowSelection = SudokuSelection.row(0);
		assertTrue(rowSelection.containsAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 6, 8)));
		assertEquals(rowSelection.size(), 9);

		rowSelection = SudokuSelection.row(8);
		assertTrue(rowSelection.containsAll(Arrays.asList(72, 73, 74, 75, 76, 77, 78, 79, 80)));
		assertEquals(rowSelection.size(), 9);

		assertThrows(AssertionError.class, () -> SudokuSelection.row(-1));
		assertThrows(AssertionError.class, () -> SudokuSelection.row(9));
	}

	@Test
	void TestBasicCols() {
		SudokuSelection colSelection = SudokuSelection.column(0);
		assertTrue(colSelection.containsAll(Arrays.asList(0, 9, 18, 27, 36, 45, 54, 63, 72)));
		assertEquals(colSelection.size(), 9);

		colSelection = SudokuSelection.column(8);
		assertTrue(colSelection.containsAll(Arrays.asList(8, 17, 26, 35, 44, 53, 62, 71, 80)));
		assertEquals(colSelection.size(), 9);

		assertThrows(AssertionError.class, () -> SudokuSelection.column(-1));
		assertThrows(AssertionError.class, () -> SudokuSelection.column(9));
	}

	@Test
	void TestBasicSqrs() {
		SudokuSelection selection = SudokuSelection.square(0);
		assertTrue(selection.containsAll(Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20)));
		assertEquals(selection.size(), 9);

		selection = SudokuSelection.square(8);
		assertTrue(selection.containsAll(Arrays.asList(60, 61, 62, 69, 70, 71, 78, 79, 80)));
		assertEquals(selection.size(), 9);

		assertThrows(AssertionError.class, () -> SudokuSelection.square(-1));
		assertThrows(AssertionError.class, () -> SudokuSelection.square(9));
	}

	@Test
	void TestAffected() {
		SudokuSelection selection = SudokuSelection.affectedBy(0);
		assertTrue(selection.containsAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 18, 19, 20, 27, 36, 45, 54, 63, 72)));
		assertEquals(selection.size(), 21);

		selection = SudokuSelection.affectedBy(29);
		assertTrue(selection.containsAll(Arrays.asList(27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 45, 46, 47, 2, 11, 20, 56, 65, 74)));
		assertEquals(selection.size(), 21);

		assertThrows(AssertionError.class, () -> SudokuSelection.affectedBy(-1));
		assertThrows(AssertionError.class, () -> SudokuSelection.affectedBy(81));
	}
}
