package shared.evaluation;

import shared.utility.RuntimeAssert;

public abstract class AStrategyResult {
	protected final int index;
	protected final int value;
	protected final ASudokuStrategy source;
	protected final ResultReason reasoning;

	public AStrategyResult(int _index, int _value, ASudokuStrategy _source, ResultReason _reasoning) {
		RuntimeAssert.inRange(_index, 0, 81);
		RuntimeAssert.inRange(_value, 1, 10);
		RuntimeAssert.notNull(_source);
		RuntimeAssert.areDifferent(_reasoning.size(), 0);

		index = _index;
		value = _value;
		source = _source;
		reasoning = _reasoning;
	}

	public abstract boolean apply(SudokuEvalData target);
	public abstract void undo(SudokuEvalData target);
	@Override
	public abstract String toString();
	public ASudokuStrategy getSource() { return source; }
	public int getIndex() { return index; }
	public int getValue() { return value; }
}
