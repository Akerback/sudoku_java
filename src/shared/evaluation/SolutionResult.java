package shared.evaluation;

/**A strategy result representing an index getting solved to a specific value.
 * If possible, it is preferable to use ASudokuStrategy.makeSolutionResult() instead of manually creating instances.
 *
 */
public class SolutionResult extends AStrategyResult {
	public SolutionResult(int _index, int _value, ASudokuStrategy _source, ResultReason _reasoning) {
		super(_index, _value, _source, _reasoning);
	}

	@Override
	public boolean apply(SudokuEvalData target) {
		return target.setValue(index, value);
	}

	@Override
	public String toString() {
		return "Solution";
	}

	@Override
	public void undo(SudokuEvalData target) {
		target.unsetValue(index, 0);
	}
}
