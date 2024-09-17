package shared.evaluation;

import shared.model.SudokuCandidates;
import shared.model.SudokuSelection;
import shared.utility.ArrayExtras;
import shared.utility.RuntimeAssert;

public class ResultReason {
	private SudokuSelection cells;
	private SudokuCandidates notes;

	public ResultReason() {
		this(new SudokuSelection());
	}

	public ResultReason(SudokuSelection _cells) {
		cells = _cells;

		notes = new SudokuCandidates();
		notes.removeAllCandidates();
	}

	public ResultReason(SudokuCandidates _notes) {
		notes = _notes;
		cells = _notes.getNonEmpty();
	}

	public boolean contains(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		return cells.contains(index);
	}

	public int size() {
		return cells.size();
	}

	public boolean hasNotesFor(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		return ArrayExtras.frequency(notes.getMask(index), true) > 0;
	}

	public void addNote(int index, int candidate) {
		RuntimeAssert.inRange(index, 0, 81);
		RuntimeAssert.inRange(candidate, 1, 10);

		if (!cells.contains(index)) {
			cells.add(index);
		}

		notes.addCandidate(index, candidate);
	}

	public void addReason(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		cells.add(index);
	}
}
