package shared.generation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import shared.evaluation.Difficulty;
import shared.evaluation.SudokuSolver;
import shared.model.Sudoku;
import shared.model.SudokuSelection;
import shared.utility.RuntimeAssert;

public final class SudokuGenerator {
	private Random defaultRandomizer;
	private IFilledSudokuGenerator fillGenerator;
	private ASudokuHoleMaker holeMaker;
	private SudokuSolver grader;
	
	public SudokuGenerator(IFilledSudokuGenerator _fillGenerator, ASudokuHoleMaker _holeMaker, SudokuSolver _grader) {
		defaultRandomizer = new Random(System.currentTimeMillis());
		
		fillGenerator = _fillGenerator;
		holeMaker = _holeMaker;
		grader = _grader;
	}
	
	public Sudoku generate(Difficulty desiredDifficulty) {
		return generate(desiredDifficulty, defaultRandomizer);
	}
	
	public Sudoku generate(Difficulty desiredDifficulty, Random randomizer) {
		//TODO: backtracking generator
		RuntimeAssert.notNull(fillGenerator);
		RuntimeAssert.notNull(holeMaker);
		RuntimeAssert.notNull(grader);
		
		Sudoku sudoku = fillGenerator.generate(randomizer);
		SudokuSelection remainingFilled = SudokuSelection.all();

		int useableHoles = 1;
		while (useableHoles > 0) {
			SudokuSelection nextHoles = holeMaker.getNextHoles(remainingFilled, randomizer);
			useableHoles = 0;
			
			for (int holePos : nextHoles) {
				//Store in case the new hole needs to be undone
				int removedValue = sudoku.get(holePos);
				sudoku.set(holePos, 0);
				remainingFilled.remove(holePos);
				
				if (remainingFilled.size() < 50) {
					//Evaluate the difficulty, and check if it's unique.
					Difficulty grade = grader.grade(sudoku);
					
					if ((grade.compareTo(desiredDifficulty) > 0) || (grade == Difficulty.UNGRADED)) {
						//If this latest hole resulted in undesireable results, undo it and continue.
						sudoku.set(holePos, removedValue);
						remainingFilled.add(holePos);
						continue;
					}
				}
				
				useableHoles++;
			}
		}
		
		System.out.printf("Generated a difficulty %s sudoku (Difficulty: %s was requested)\n", grader.grade(sudoku, false), desiredDifficulty.toString());
		return sudoku;
	}
}
