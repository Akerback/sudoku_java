package shared.evaluation.strategies;

import java.util.ArrayList;
import java.util.List;

import shared.evaluation.StrategyResult;
import shared.evaluation.ASudokuStrategy;
import shared.evaluation.Difficulty;
import shared.evaluation.ResultReason;
import shared.evaluation.AnnotatedSudokuView;

public class LastInCellStrategy extends ASudokuStrategy {
	@Override
	public List<StrategyResult> apply(AnnotatedSudokuView sudokuEvalView) {
		List<StrategyResult> results = new ArrayList<>();

		for (int i = 0; i < 81; i++) {
			List<Integer> candidates = sudokuEvalView.getCandidates(i);

			if (candidates.size() == 1) {
				ResultReason reasoning = new ResultReason();
				reasoning.addNote(i, candidates.get(0));
				results.add(makeSolutionResult(i, candidates.get(0), reasoning));
			}
		}

		return results;
	}

	@Override
	public Difficulty getDifficulty() { return Difficulty.MEDIUM; }

	@Override
	public String toString() { return "Last-in-cell strategy"; }

}
