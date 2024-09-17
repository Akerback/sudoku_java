package shared.generation;

import shared.model.SudokuSelection;

public class PerSquareEvenHoleMaker extends ASudokuHoleMaker {
	private int nextSquare = 0;
	@Override
	protected SudokuSelection getNextHole(SudokuSelection holeCandidates) {
		holeCandidates = holeCandidates.getIntersectionWith(SudokuSelection.square(nextSquare));

		nextSquare += 1;
		if (nextSquare >= 9) {
			nextSquare = 0;
		}

		return new SudokuSelection(holeCandidates.getRandom(getRandomizer()));
	}
}
