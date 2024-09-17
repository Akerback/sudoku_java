package shared.generation.holemodifiers;

import shared.model.Sudoku;

public class VerticalSymmetryModifier extends ASymmetryModifier {
	
	@Override
	protected int getMirrorIndex(int index) {
		int x = Sudoku.indexToColumn(index);
		int y = Sudoku.indexToRow(index);

		return Sudoku.positionToIndex(x, 8 - y);
	}
}
