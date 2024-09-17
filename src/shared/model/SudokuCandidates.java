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

	public boolean canBe(int index, int candidate) {
		return candidates[indexInCandidates(index, candidate)];
	}

	public SudokuSelection getNonEmpty() {
		SudokuSelection selection = new SudokuSelection();

		for (int i = 0; i < 81; i++) {
			int trueIndex = indexInCandidates(i);
			if (ArrayExtras.frequencyRange(candidates, trueIndex, trueIndex + 9, true) > 0) {
				selection.add(i);
			}
		}

		return selection;
	}

	public boolean[] getMask(int index) {
		int trueIndex = indexInCandidates(index);
		return Arrays.copyOfRange(candidates, trueIndex, trueIndex + 9);
	}

	public boolean[] getSharedMask(int firstIndex, int secondIndex) {
		boolean[] firstMask = getMask(firstIndex);
		boolean[] secondMask = getMask(secondIndex);

		boolean[] sharedMask = new boolean[9];
		for (int i = 0; i < 9; i++) {
			sharedMask[i] = firstMask[i] & secondMask[i];
		}

		return sharedMask;
	}

	public boolean[] getSharedMask(SudokuSelection selection) {
		boolean[] sharedMask = new boolean[9];
		Arrays.fill(sharedMask, true);

		for (Integer index : selection) {
			boolean[] mask = getMask(index);

			for (int i = 0; i < 9; i++) {
				sharedMask[i] &= mask[i];
			}
		}

		return sharedMask;
	}

	public void reset(int index) {
		fill(index, true);
	}

	public void resetAll() {
		Arrays.fill(candidates, true);
	}

	public void removeAllCandidates() {
		Arrays.fill(candidates, false);
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
		RuntimeAssert.inRange(index, 0, 81);

		int trueIndex = indexInCandidates(index);
		Arrays.fill(candidates, trueIndex, trueIndex + 9, value);
	}

	private boolean set(int index, int candidate, boolean value) {
		int trueIndex = indexInCandidates(index, candidate);
		boolean result = candidates[trueIndex] != value;

		candidates[trueIndex] = value;
		return result;
	}

	private int indexInCandidates(int sudokuIndex, int candidate) {
		RuntimeAssert.inRange(sudokuIndex, 0, 81);
		RuntimeAssert.inRange(candidate, 1, 10);

		return sudokuIndex * 9 + candidate - 1;
	}

	private int indexInCandidates(int sudokuIndex) {
		return indexInCandidates(sudokuIndex, 1);
	}
}
