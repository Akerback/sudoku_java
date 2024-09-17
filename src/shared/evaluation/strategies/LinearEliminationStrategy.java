package shared.evaluation.strategies;

import java.util.ArrayList;
import java.util.List;

import shared.evaluation.AStrategyResult;
import shared.evaluation.ASudokuStrategy;
import shared.evaluation.Difficulty;
import shared.evaluation.ResultReason;
import shared.evaluation.SudokuEvalView;
import shared.model.SudokuSelection;

public class LinearEliminationStrategy extends ASudokuStrategy {
	@Override
	public Difficulty getDifficulty() { return Difficulty.EASY; }

	@Override
	public List<AStrategyResult> apply(SudokuEvalView sudokuEvalView) {
		List<AStrategyResult> results = new ArrayList<>();

		for (int i = 0; i < 81; i++) {
			if (sudokuEvalView.getValue(i) != 0) {
				continue;
			}
			List<Integer> candidates = sudokuEvalView.getCandidates(i);
			for (Integer candidate : candidates) {
				SudokuSelection selection = SudokuSelection.affectedBy(i);
				if (sudokuEvalView.getAppearanceCounts(selection)[candidate - 1] > 0) {
					SudokuSelection specificSelection = sudokuEvalView.indicesOf(candidate, selection);
					results.add(makeRemovalResult(i, candidate, new ResultReason(specificSelection)));
				}
			}
		}

		return results;
	}

	@Override
	public String toString() {
		return "Linear elimination";
	}
}
