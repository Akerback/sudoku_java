package shared.evaluation;

public class RemovedCandidateResult extends AStrategyResult {
	public RemovedCandidateResult(int _index, int _value, ASudokuStrategy _source, ResultReason _reasoning) {
		super(_index, _value, _source, _reasoning);
	}

	@Override
	public boolean apply(SudokuEvalData target) {
		return target.removeCandidate(index, value);
	}

	@Override
	public String toString() {
		return "Removed Candidate";
	}

	@Override
	public void undo(SudokuEvalData target) {
		target.addCandidate(index, value);
	}
}
