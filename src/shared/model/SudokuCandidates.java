package shared.model;

import java.util.Arrays;

import shared.utility.ArrayExtras;
import shared.utility.RuntimeAssert;

public class SudokuCandidates {
	/**Layout: 9 consecutive flags for if a value can be at a given index.*/
	private boolean[] candidates = new boolean[81 * 9];

	public SudokuCandidates() {
		candidates = new boolean[81 * 9];
		Arrays.fill(candidates, true);
	}

	public SudokuCandidates(Sudoku sudoku) {
		this();

		for (int i = 0; i < 81; i++) {
			if (sudoku.get(i) != 0) {
				removeAllCandidatesAt(i);
			}
		}
	}

	public boolean hasCandidate(int index, int candidate) {
		return candidates[getInternalIndex(index, candidate)];
	}

	public SudokuSelection getNonEmpty() {
		SudokuSelection selection = new SudokuSelection();

		for (int i = 0; i < 81; i++) {
			int trueIndex = internalIndex(i);
			if (ArrayExtras.frequencyRange(candidates, trueIndex, trueIndex + 9, true) > 0) {
				selection.add(i);
			}
		}

		return selection;
	}

	public boolean[] getMask(int index) {
		int trueIndex = internalIndex(index);
		return Arrays.copyOfRange(candidates, trueIndex, trueIndex + 9);
	}
	
	public void addAllCandidates() {
		Arrays.fill(candidates, true);
	}

	public void removeAllCandidates() {
		Arrays.fill(candidates, false);
	}

	public void addAllCandidatesAt(int index) {
		fill(index, true);
	}

	public void removeAllCandidatesAt(int index) {
		fill(index, false);
	}

	public boolean removeCandidate(int index, int candidate) {
		return set(index, candidate, false);
	}

	public boolean addCandidate(int index, int candidate) {
		return set(index, candidate, true);
	}

	private void fill(int index, boolean value) {
		int internalIndex = internalIndex(index);
		Arrays.fill(candidates, internalIndex, internalIndex + 9, value);
	}

	private boolean set(int index, int candidate, boolean value) {
		int internalIndex = getInternalIndex(index, candidate);
		boolean result = candidates[internalIndex] != value;

		candidates[internalIndex] = value;
		return result;
	}

	private int getInternalIndex(int sudokuIndex, int candidate) {
		RuntimeAssert.inRange(sudokuIndex, 0, 81);
		RuntimeAssert.inRange(candidate, 1, 10);

		return sudokuIndex * 9 + candidate - 1;
	}

	private int internalIndex(int sudokuIndex) {
		return getInternalIndex(sudokuIndex, 1);
	}
}
