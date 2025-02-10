package shared.generation;

import java.util.Random;

import shared.model.SudokuSelection;

public class RandomHoleMaker extends ASudokuHoleMaker {
	@Override
	protected int getNextHole(SudokuSelection holeOptions, Random randomizer) {
		return holeOptions.getRandom(randomizer);
	}

}
