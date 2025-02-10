package shared.generation;

import java.util.Random;

import shared.model.SudokuSelection;

public class PerSquareEvenHoleMaker extends ASudokuHoleMaker {
	private int nextSquare = 0;
	@Override
	protected int getNextHole(SudokuSelection holeCandidates, Random randomizer) {
		holeCandidates = holeCandidates.getIntersectionWith(SudokuSelection.square(nextSquare));

		nextSquare += 1;
		if (nextSquare >= 9) {
			nextSquare = 0;
		}

		return holeCandidates.getRandom(randomizer);
	}
}
