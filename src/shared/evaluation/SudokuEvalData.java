package shared.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import shared.model.Sudoku;
import shared.model.SudokuCandidates;
import shared.model.SudokuSelection;
import shared.utility.RuntimeAssert;

public class SudokuEvalData {
	private Sudoku sudoku;
	private SudokuCandidates candidates;

	public SudokuEvalData(Sudoku _sudoku) {
		RuntimeAssert.notNull(_sudoku);

		sudoku = _sudoku.clone();

		candidates = new SudokuCandidates();
	}

	public SudokuEvalView getView() { return new SudokuEvalView(this); }
	public Sudoku getSudoku() { return sudoku; }

	public int getValue(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		return sudoku.get(index);
	}

	public List<Integer> getCandidates(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		if (sudoku.get(index) > 0) {
			return new ArrayList<>(0);
		}

		List<Integer> results = new ArrayList<>(9);

		boolean[] candidateMask = candidates.getMask(index);
		for (int i = 0; i < 9; i++) {
			if (candidateMask[i]) {
				results.add(i + 1);
			}
		}

		return results;
	}

	public int[] getAppearanceCount(SudokuSelection area) {
		int[] appearanceCounts = new int[9];
		Arrays.fill(appearanceCounts, 0);

		for (int i = 0; i < 9; i++) {
			appearanceCounts[i] = sudoku.indicesOf(i + 1, area).size();
		}

		return appearanceCounts;
	}

	public boolean setValue(int index, int value) {
		RuntimeAssert.inRange(index, 0, 81);
		RuntimeAssert.inRange(value, 1, 10);

		int currentVal = sudoku.get(index);
		try {
			RuntimeAssert.areEqual(currentVal, 0); //No reassignment!
		}
		catch (AssertionError e) {
			RuntimeAssert.areEqual(currentVal, value);
		}

		boolean madeChange = true;
		if (currentVal == value) {
			madeChange = false;
		}
		sudoku.set(index, value);

		return madeChange;
	}

	public void unsetValue(int index, int fromValue) {
		RuntimeAssert.inRange(index, 0, 81);
		RuntimeAssert.inRange(fromValue, 1, 10);

		RuntimeAssert.areEqual(fromValue, sudoku.get(index));

		sudoku.set(index, 0);
	}

	public boolean removeCandidate(int index, int candidate) {
		RuntimeAssert.inRange(index, 0, 81);
		RuntimeAssert.inRange(candidate, 1, 10);
		RuntimeAssert.areEqual(sudoku.get(index), 0);
		RuntimeAssert.areDifferent(getCandidates(index).size(), 1);

		return candidates.removeCandidate(index, candidate);
	}

	public void addCandidate(int index, int candidate) {
		RuntimeAssert.inRange(index, 0, 81);
		RuntimeAssert.inRange(candidate, 1, 10);

		candidates.addCandidate(index, candidate);
	}

	private List<AStrategyResult> solveLog = new ArrayList<>();
	public int applyResults(List<AStrategyResult> results) {
		int changes = 0;
		for (AStrategyResult result : results) {
			try {
				if (result.apply(this)) {
					solveLog.add(result);
					changes++;
				}
			}
			catch (AssertionError e) {
				solveLog.add(result);
				throw new AssertionError("Assertion failed when applying result \"" + result.toString() + "\" from \"" + result.getSource().toString() + "\"", e);
			}
		}

		return changes;
	}

	public List<AStrategyResult> getLog() {
		return new ArrayList<>(solveLog);
	}

	public boolean isSolved() {
		return sudoku.isSolved();
	}

	public void clearLog() {
		solveLog.clear();
	}
}
