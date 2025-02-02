package shared.model;

import java.util.Arrays;

import shared.utility.RuntimeAssert;

public class Sudoku implements Cloneable {
	private int[] contents;
	private SudokuSelection issues;

	public Sudoku() {
		contents = new int[81];
		for (int i = 0; i < 81; i++) {
			contents[i] = 0;
		}

		issues = new SudokuSelection();
	}

	public Sudoku(CompactSudoku compactRep) {
		this();

		for (int i = 0; i < 81; i++) {
			set(compactRep.get(i), i);
		}
	}

	public CompactSudoku getCompact() {
		return new CompactSudoku(this);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(contents);
	}

	@Override
	public boolean equals(Object o) {
		try {
			Sudoku other = (Sudoku)o;
			if (this.hashCode() != other.hashCode()) {
				return false;
			}

			return Arrays.equals(this.contents, other.contents);
		}
		catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public Sudoku clone() {
		Sudoku clone = new Sudoku();

		clone.contents = Arrays.copyOf(this.contents, 81);
		clone.issues = new SudokuSelection(issues);

		return clone;

	}

	public int get(int index) {
		return contents[index];
	}

	public void set(int index, int value) {
		if (value < 0 || value > 9) {
			value = 0;
		}

		contents[index] = value;
		refreshCellIssues(index);
	}

	/**Filter indices in a selection to only those filled with a specific value inside the Sudoku.
	 * 
	 * @param value
	 * @param selection
	 * @return	A new selection containing only indices from the given selection that have the desired value inside this Sudoku.
	 */
	public SudokuSelection valueFilter(int value, SudokuSelection selection) {
		SudokuSelection newSelection = new SudokuSelection();

		for (Integer index : selection) {
			if (contents[index] == value) {
				newSelection.add(index);
			}
		}

		return newSelection;
	}

	public void fill(SudokuSelection selection, int value) {
		for (Integer index : selection) {
			contents[index] = value;
		}
	}

	public String getPrettyString(ASudokuFormatter formatter) {
		String result = "";
		for (int row = 0; row < 9; row++) {
			//Horisontal border every 3 rows
			if (row % 3 == 0) {
				result += "----------------------\n";//22 dashes
			}
			for (int column = 0; column < 9; column++) {
				//Vertical border every 3 columns
				if (column % 3 == 0) {
					result += "|";
				}
				result += " " + formatter.getConverter().toString(get(positionToIndex(column, row)));
			}
			//Finish each row with another border, and a newline
			result += "|\n";
		}
		//Finish the entire print with another horisontal border
		result += "----------------------";

		return result;
	}

	public void regenerateIssues() {
		issues.clear();

		for (int row = 0; row < 9; row++) {
			selectHouseIssues(SudokuSelection.row(row), issues);
		}

		for (int col = 0; col < 9; col++) {
			selectHouseIssues(SudokuSelection.column(col), issues);
		}

		for (int sqr = 0; sqr < 9; sqr++) {
			selectHouseIssues(SudokuSelection.square(sqr), issues);
		}
	}

	/**Get a selection containing all cells that conflict with the rules of Sudoku.
	 * 
	 * @return
	 */
	public SudokuSelection getIssues() {
		return new SudokuSelection(issues);
	}

	public boolean isLegalBoardState() {
		return issues.size() == 0;
	}

	public boolean hasEmptyCells() {
		return valueFilter(0, SudokuSelection.all()).size() > 0;
	}

	public boolean isSolved() {
		return isLegalBoardState() && (!hasEmptyCells());
	}

	private void refreshCellIssues(int cellIndex) {
		int row = indexToRow(cellIndex);
		int col = indexToColumn(cellIndex);
		int sqr = indexToSquare(cellIndex);

		SudokuSelection issueSet = new SudokuSelection();

		SudokuSelection rowSelect = SudokuSelection.row(row);
		SudokuSelection colSelect = SudokuSelection.column(col);
		SudokuSelection sqrSelect = SudokuSelection.square(sqr);

		selectHouseIssues(rowSelect, issueSet);
		selectHouseIssues(colSelect, issueSet);
		selectHouseIssues(sqrSelect, issueSet);

		SudokuSelection joined = rowSelect.getUnionWith(colSelect).getUnionWith(sqrSelect);

		for (Integer index : joined) {
			if (issueSet.contains(index)) {
				issues.add(index);
			}
			else {
				issues.remove(index);
			}
		}
	}

	/**Find all duplicated value in a section, and add them to a given selection.
	 * All 1-9 values (inclusive) that appear more than once in the section have all the indices they appear at added to the selectionTarget.
	 *
	 * @param house	The area to look for duplicates in.
	 * @param selectionTarget	The target to add duplicated values to.
	 */
	private void selectHouseIssues(SudokuSelection house, SudokuSelection selectionTarget) {
		for (int i = 0; i < 9; i++) {
			int candidate = i + 1;
			SudokuSelection appearances = valueFilter(candidate, house);
			if (appearances.size() > 1) {
				selectionTarget.addAll(appearances);
			}
		}
	}

	//--Indexing functions

	//Who needs math when you have lookup tables?
	private static final int[] SQUARE_ORIGINS = {0, 3, 6, 27, 30, 33, 54, 57, 60};
	private static final int[] SQUARE_OFFSETS = {0, 1, 2, 9, 10, 11, 18, 19, 20};

	public static int squarePositionToIndex(int squareX, int squareY, int internalX, int internalY) {
		RuntimeAssert.inRange(squareX, 0, 3);
		RuntimeAssert.inRange(squareY, 0, 3);
		RuntimeAssert.inRange(internalX, 0, 3);
		RuntimeAssert.inRange(internalY, 0, 3);

		int squareInd = squareY * 3 + squareX;
		int internalInd = internalY * 3 + internalX;

		return SQUARE_ORIGINS[squareInd] + SQUARE_OFFSETS[internalInd];
	}

	public static int positionToIndex(int x, int y) {
		RuntimeAssert.inRange(x, 0, 9);
		RuntimeAssert.inRange(y, 0, 9);

		return y * 9 + x;
	}

	public static int indexToRow(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		return index / 9;
	}

	public static int indexToColumn(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		return index % 9;
	}

	public static int indexToSquare(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		int squareY = index / 27;
		int squareX = (index / 3) % 3;

		return squareY * 3 + squareX;
	}
}
