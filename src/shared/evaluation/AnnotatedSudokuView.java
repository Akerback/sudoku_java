package shared.evaluation;

import java.util.List;

import shared.model.SudokuSelection;

public class AnnotatedSudokuView {
	private AnnotatedSudoku source;

	public AnnotatedSudokuView(AnnotatedSudoku _source) {
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
		return source.getSudoku().valueFilter(value, selection);
	}
}
