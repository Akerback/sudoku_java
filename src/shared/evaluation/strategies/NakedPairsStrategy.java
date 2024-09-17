package shared.evaluation.strategies;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import shared.evaluation.AStrategyResult;
import shared.evaluation.ASudokuStrategy;
import shared.evaluation.Difficulty;
import shared.evaluation.ResultReason;
import shared.evaluation.SudokuEvalView;
import shared.model.SudokuSelection;

public class NakedPairsStrategy extends ASudokuStrategy {
	@Override
	public List<AStrategyResult> apply(SudokuEvalView sudokuEvalView) {
		List<AStrategyResult> results = new ArrayList<>();

		for (int squareInd = 0; squareInd < 9; squareInd++) {
			SudokuSelection selection = SudokuSelection.square(squareInd);

			List<Pair<Integer, Integer>> pairs = findPairs(selection, sudokuEvalView);

			pairsToResults(pairs, selection, sudokuEvalView, results);
		}

		for (int col = 0; col < 9; col++) {
			SudokuSelection selection = SudokuSelection.column(col);

			List<Pair<Integer, Integer>> pairs = findPairs(selection, sudokuEvalView);

			pairsToResults(pairs, selection, sudokuEvalView, results);
		}

		for (int row = 0; row < 9; row++) {
			SudokuSelection selection = SudokuSelection.row(row);

			List<Pair<Integer, Integer>> pairs = findPairs(selection, sudokuEvalView);

			pairsToResults(pairs, selection, sudokuEvalView, results);
		}

		return results;
	}

	private void pairsToResults(List<Pair<Integer, Integer>> pairs, SudokuSelection selection, SudokuEvalView sudokuEvalView, List<AStrategyResult> results) {
		for (Pair<Integer, Integer> pair : pairs) {
			List<Integer> eliminatedCands = sudokuEvalView.getCandidates(pair.getKey());

			for (Integer index : selection) {
				if ((index == pair.getKey()) || (index == pair.getValue()) || (sudokuEvalView.getValue(index) != 0)) {
					continue;
				}

				ResultReason reasoning = new ResultReason();
				reasoning.addNote(pair.getKey(), eliminatedCands.get(0));
				reasoning.addNote(pair.getKey(), eliminatedCands.get(1));
				reasoning.addNote(pair.getValue(), eliminatedCands.get(0));
				reasoning.addNote(pair.getValue(), eliminatedCands.get(1));

				results.add(makeRemovalResult(index, eliminatedCands.get(0), reasoning));
				results.add(makeRemovalResult(index, eliminatedCands.get(1), reasoning));
			}
		}
	}

	private List<Pair<Integer, Integer>> findPairs(SudokuSelection selection, SudokuEvalView dataSource) {
		//Pick out all indices with only 2 candidates remaining
		List<Integer> pairCandidates = new ArrayList<>();

		for (Integer index : selection) {
			if (dataSource.getCandidates(index).size() == 2) {
				pairCandidates.add(index);
			}
		}

		//Out of the 2-candidate indices, check if any are the exact same 2
		List<Pair<Integer, Integer>> pairs = new ArrayList<>();

		List<Integer> firstCands = new ArrayList<>();
		List<Integer> secondCands = new ArrayList<>();
		for (Integer first : pairCandidates) {
			firstCands = dataSource.getCandidates(first);

			for (Integer second : pairCandidates) {
				if (first == second) {
					continue;
				}

				secondCands = dataSource.getCandidates(second);

				if (firstCands.equals(secondCands)) {
					pairs.add(new Pair<>(first, second));
				}
			}
		}

		return pairs;
	}

	@Override
	public Difficulty getDifficulty() { return Difficulty.HARD; }

	@Override
	public String toString() { return "Naked Pairs strategy"; }

}
