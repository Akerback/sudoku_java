package shared.evaluation;

import java.util.List;

import shared.model.SudokuSelection;

public class SudokuEvalView {
	private SudokuEvalData source;

	public SudokuEvalView(SudokuEvalData _source) {
		source = _source;
	}

	public int getValue(int index) {
		return source.getValue(index);
	}

	public List<Integer> getCandidates(int index) {
		return source.getCandidates(index);
	}

	public int[] getAppearanceCounts(SudokuSelection selection) {
		return source.getAppearanceCount(selection);
	}

	public SudokuSelection indicesOf(int value, SudokuSelection selection) {
		return source.getSudoku().indicesOf(value, selection);
	}
}
