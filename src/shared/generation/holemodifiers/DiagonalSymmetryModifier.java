package shared.generation.holemodifiers;

import shared.model.Sudoku;

public class DiagonalSymmetryModifier extends ASymmetryModifier {
	@Override
	protected int getMirrorIndex(int index) {
		int x = Sudoku.indexToColumn(index);
		int y = Sudoku.indexToRow(index);

		return Sudoku.positionToIndex(8 - x, 8 - y);
	}
}
