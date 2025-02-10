package shared.evaluation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shared.io.SDMConverter;
import shared.model.DebugFormatter;
import shared.model.Sudoku;
import shared.model.SudokuSelection;
import shared.utility.RuntimeAssert;

public class SudokuSolver {
	public List<ASudokuStrategy> strategies;
	private SDMConverter sdmConverter = new SDMConverter();

	public SudokuSolver() {
		strategies = new ArrayList<>();
	}

	public SudokuSolver(ASudokuStrategy[] _strategies) {
		strategies = new ArrayList<>(_strategies.length);

		for (ASudokuStrategy element : _strategies) {
			strategies.add(element);
		}

		//Sort by difficulty
		strategies.sort((first, second) -> first.getDifficulty().compareTo(second.getDifficulty()));
	}
	
	public Difficulty grade(Sudoku sudoku) {
		return grade(sudoku, true);
	}

	/**Calculate a difficulty for a given sudoku by solving it.
	 * 
	 * @param sudoku
	 * @param silent	If true, debug output is hidden.
	 * @return	The difficulty of the hardest technique used while solving, or Difficulty.UNGRADED if there is no unique solution.
	 */
	public Difficulty grade(Sudoku sudoku, boolean silent) {
		int solved = 81 - sudoku.valueFilter(0, SudokuSelection.all()).size();

		AnnotatedSudoku solveResult = solve(sudoku, silent);
		if (!solveResult.isSolved()) {
			if (!silent) {
				System.out.println("Grading solve failed! Sudoku may not have a unique Solution!");
			}
			return Difficulty.UNGRADED;
		}
		
		List<StrategyResult> stepLog = solveResult.getLog();

		Difficulty grade = Difficulty.UNGRADED;
		for (StrategyResult step : stepLog) {
			if (step.getType() == StrategyResult.Type.SOLUTION) {
				solved++;
			}
			
			//The hardest required strategy becomes the grade
			Difficulty difficulty = step.source.getDifficulty();
			if (difficulty.compareTo(grade) > 0) {
				grade = step.source.getDifficulty();
			}

			if (!silent) {
				System.out.println("[" + solved + "/81]" + step.getSource() + ": " + step + " value " + step.value + " at index #" + step.index);
			}
		}

		return grade;
	}

	public boolean hasUniqueSolution(Sudoku sudoku) {
		return solve(sudoku, false).isSolved();
	}

	/**Get the singular solution for a sudoku without modifying it. Returns null if no unique solution could be found.
	 *
	 * @param sudoku	The sudoku to solve
	 * @return	The singular solution for the given sudoku, or null if no unique solution could be found.
	 */
	public Sudoku getSolution(Sudoku sudoku) {
		AnnotatedSudoku solveResult = solve(sudoku, false);

		if (!solveResult.isSolved()) {
			return null;
		}
		else {
			return solveResult.getSudoku();
		}
	}

	/**Apply all set strategies to solve a sudoku. The given sudoku is not modified.
	 *
	 * @param sudoku
	 * @return	SudokuEvalData	Contains the
	 */
	private AnnotatedSudoku solve(Sudoku sudoku, boolean silent) {
		RuntimeAssert.hasElements(strategies);
		RuntimeAssert.notNull(sudoku);
		/*
		if (Memo.safeHasValueFor(solveMemo, sudoku)) {
			return solveMemo.getValue();
		}*/

		Sudoku originalSudoku = sudoku.clone();

		AnnotatedSudoku evalData = new AnnotatedSudoku(sudoku);
		AnnotatedSudokuView evalView = evalData.getView();

		try {
			//As long as atleast one strategy progresses, keep looping
			while (true) {
				int totalChanges = 0;
				for (ASudokuStrategy strat : strategies) {
					//If this strategy has no result, move on to harder strategies.
					List<StrategyResult> stratResults = strat.apply(evalView);

					int changes = evalData.applyResults(stratResults);
					totalChanges += changes;

					//If this strat had success, go back to easier strategies
					if (changes > 0) {
						if (!silent) {
							System.out.println("Solver made " + changes + " changes using strategy: " + strat.toString());
						}
						break;
					}
				}

				if (totalChanges == 0) {
					break;
				}
			}
			if (!silent) {
				System.out.println("Solver has finished!");
			}
		}
		catch (AssertionError e) {
			System.out.println();
			System.out.println("Sudoku Solver has failed!");
			System.out.println();

			System.out.println("Solving from: ");
			String visual = originalSudoku.getPrettyString(new DebugFormatter());
			System.out.println(visual);

			System.out.println();
			System.out.println("The following steps were taken:");
			System.out.println();

			int stepInd = 0;
			List<StrategyResult> log = evalData.getLog();
			for (StrategyResult result : log) {
				System.out.println(stepInd + ":\t" + result.getSource() + " -> " + result.toString() + " (index=" + result.index + ", value=" + result.value + ")");
				stepInd++;
			}
			System.out.println("^ This step failed because of: ");
			logAsIssue(originalSudoku);

			throw e;
		}
		
		return evalData;
	}

	private void logAsIssue(Sudoku sudoku) {
		final String ISSUE_FILE = "KnownIssues.sdm";
		try {
			FileWriter outStream = new FileWriter(ISSUE_FILE, true);
			outStream.write(sdmConverter.toString(sudoku) + "\n");
			outStream.close();
		}
		catch (IOException e) { System.err.println("Failed to log to " + ISSUE_FILE); }
	}
}
