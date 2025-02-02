package shared.generation;

import java.util.Random;

import shared.model.SudokuSelection;

public class RandomHoleMaker extends ASudokuHoleMaker {
	@Override
	protected SudokuSelection getNextHole(SudokuSelection holeOptions, Random randomizer) {
		return new SudokuSelection(holeOptions.getRandom(randomizer));
	}

}
