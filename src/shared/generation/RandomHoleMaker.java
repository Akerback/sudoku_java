package shared.generation;

import shared.model.SudokuSelection;

public class RandomHoleMaker extends ASudokuHoleMaker {
	@Override
	protected SudokuSelection getNextHole(SudokuSelection holeOptions) {
		return new SudokuSelection(holeOptions.getRandom(getRandomizer()));
	}

}
