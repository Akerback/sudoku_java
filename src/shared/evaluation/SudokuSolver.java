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
		int solved = 81 - sudoku.valueFilter(0, SudokuSelection.all()).size();

		SudokuEvalData solveResult = solve(sudoku);
		List<AStrategyResult> stepLog = solveResult.getLog();

		Difficulty grade = Difficulty.UNGRADED;
		for (AStrategyResult step : stepLog) {
			String stepString = step.toString();
			//TODO: better way of checking solve status
			if (stepString == "Solution") {
				solved++;
			}
			
			//The hardest required strategy becomes the grade
			Difficulty difficulty = step.source.getDifficulty();
			if (difficulty.compareTo(grade) > 0) {
				grade = step.source.getDifficulty();
			}

			System.out.println("[" + solved + "/81]" + step.getSource() + ": " + step + " value " + step.value + " at index #" + step.index);
		}
		if (!solveResult.isSolved()) {
			System.out.println("Grading solve failed! Sudoku may not have a unique Solution!");
		}

		return grade;
	}

	public boolean hasUniqueSolution(Sudoku sudoku) {
		return solve(sudoku).isSolved();
	}

	/**Get the singular solution for a sudoku without modifying it. Returns null if no unique solution could be found.
	 *
	 * @param sudoku	The sudoku to solve
	 * @return	The singular solution for the given sudoku, or null if no unique solution could be found.
	 */
	public Sudoku getSolution(Sudoku sudoku) {
		SudokuEvalData solveResult = solve(sudoku);

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
	private SudokuEvalData solve(Sudoku sudoku) {
		RuntimeAssert.hasElements(strategies);
		RuntimeAssert.notNull(sudoku);
		/*
		if (Memo.safeHasValueFor(solveMemo, sudoku)) {
			return solveMemo.getValue();
		}*/

		Sudoku originalSudoku = sudoku.clone();

		SudokuEvalData evalData = new SudokuEvalData(sudoku);
		SudokuEvalView evalView = evalData.getView();

		try {
			//As long as atleast one strategy progresses, keep looping
			while (true) {
				int totalChanges = 0;
				for (ASudokuStrategy strat : strategies) {
					//As long as this strategy progresses, keep trying it again
					List<AStrategyResult> stratResults = strat.apply(evalView);

					int changes = evalData.applyResults(stratResults);
					totalChanges += changes;

					if (changes > 0) {
						System.out.println("Solver made " + changes + " changes using strategy: " + strat.toString());
					}

					//If this strat had success, go back to easier strategies
					if (changes > 0) {
						break;
					}
				}

				if (totalChanges == 0) {
					break;
				}
			}
			System.out.println("Solver has finished!");
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
			List<AStrategyResult> log = evalData.getLog();
			for (AStrategyResult result : log) {
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
