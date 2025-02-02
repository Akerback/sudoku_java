package shared.generation;

import java.util.Random;

import shared.evaluation.Difficulty;
import shared.model.Sudoku;
import shared.utility.RuntimeAssert;

public final class SudokuGenerator {
	private Random defaultRandomizer;
	public IFilledSudokuGenerator fillGenerator;
	public ASudokuHoleMaker holeMaker;
	
	public SudokuGenerator(IFilledSudokuGenerator _fillGenerator, ASudokuHoleMaker _holeMaker) {
		defaultRandomizer = new Random(System.currentTimeMillis());
		
		fillGenerator = _fillGenerator;
		holeMaker = _holeMaker;
	}
	
	public Sudoku generate() {
		return generate(defaultRandomizer);
	}
	
	public Sudoku generate(Random randomizer) {
		RuntimeAssert.notNull(fillGenerator);
		RuntimeAssert.notNull(holeMaker);
		
		Sudoku generated = new Sudoku();
		fillGenerator.generate(generated, randomizer);
		holeMaker.makeHoles(generated, Difficulty.UNGRADED, randomizer);
		
		return generated;
	}
}
