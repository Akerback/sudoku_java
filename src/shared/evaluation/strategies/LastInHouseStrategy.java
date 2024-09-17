package shared.evaluation.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import shared.evaluation.AStrategyResult;
import shared.evaluation.ASudokuStrategy;
import shared.evaluation.Difficulty;
import shared.evaluation.ResultReason;
import shared.evaluation.SudokuEvalView;
import shared.model.SudokuSelection;

public class LastInHouseStrategy extends ASudokuStrategy {

	@Override
	public List<AStrategyResult> apply(SudokuEvalView sudokuEvalView) {
		List<AStrategyResult> results = new ArrayList<>();

		for (int square = 0; square < 9; square++) {
			SudokuSelection selection = SudokuSelection.square(square);
			processSelection(selection, sudokuEvalView, results);
		}

		for (int row = 0; row < 9; row++) {
			SudokuSelection selection = SudokuSelection.row(row);
			processSelection(selection, sudokuEvalView, results);
		}

		for (int col = 0; col < 9; col++) {
			SudokuSelection selection = SudokuSelection.column(col);
			processSelection(selection, sudokuEvalView, results);
		}

		return results;
	}

	private void processSelection(SudokuSelection selection, SudokuEvalView dataSource, List<AStrategyResult> target) {
		int[] candidateAppearances = new int[9];
		int[] candFirstAppearanceIndex = new int[9];

		Arrays.fill(candidateAppearances, 0);
		Arrays.fill(candFirstAppearanceIndex, -1);

		//Collect how many times values appeared as cell candidates,
		//and which index they first appeared in.
		for (Integer index : selection) {
			List<Integer> candidates = dataSource.getCandidates(index);
			for (Integer candidate : candidates) {
				candidateAppearances[candidate - 1] += 1;
				if (candFirstAppearanceIndex[candidate - 1] == -1) {
					candFirstAppearanceIndex[candidate - 1] = index;
				}
			}
		}

		//Go through all candidates that only appeared once,
		//and make that one appearance the solution
		for (int i = 0; i < 9; i++) {
			if (candidateAppearances[i] == 1) {
				ResultReason reasoning = new ResultReason();
				reasoning.addNote(candFirstAppearanceIndex[i], i + 1);

				target.add(makeSolutionResult(candFirstAppearanceIndex[i], i + 1, reasoning));
			}
		}
	}

	@Override
	public Difficulty getDifficulty() { return Difficulty.EASY; }

	@Override
	public String toString() { return "Last-in-house strategy"; }

}
