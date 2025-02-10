package shared.evaluation;

import shared.utility.RuntimeAssert;

public final class StrategyResult {
	protected final Type type;
	protected final int index;
	protected final int value;
	protected final ASudokuStrategy source;
	protected final ResultReason reasoning;

	private StrategyResult(Type _type, int _index, int _value, ASudokuStrategy _source, ResultReason _reasoning) {
		RuntimeAssert.inRange(_index, 0, 81);
		RuntimeAssert.inRange(_value, 1, 10);
		RuntimeAssert.notNull(_source);
		RuntimeAssert.areDifferent(_reasoning.size(), 0);

		type = _type;
		index = _index;
		value = _value;
		source = _source;
		reasoning = _reasoning;
	}
	
	public static StrategyResult Solved(int _index, int _value, ASudokuStrategy _source, ResultReason _reasoning) {
		RuntimeAssert.inRange(_index, 0, 81);
		RuntimeAssert.inRange(_value, 1, 10);
		RuntimeAssert.notNull(_source);
		RuntimeAssert.areDifferent(_reasoning.size(), 0);
		
		return new StrategyResult(Type.SOLUTION, _index, _value, _source, _reasoning);
	}
	
	public static StrategyResult RemovedCandidate(int _index, int _candidate, ASudokuStrategy _source, ResultReason _reasoning) {
		RuntimeAssert.inRange(_index, 0, 81);
		RuntimeAssert.inRange(_candidate, 1, 10);
		RuntimeAssert.notNull(_source);
		RuntimeAssert.areDifferent(_reasoning.size(), 0);
		
		return new StrategyResult(Type.REMOVE_CANDIDATE, _index, _candidate, _source, _reasoning);
	}

	public boolean apply(AnnotatedSudoku target) {
		switch (type) {
		case SOLUTION:
			return target.setValue(index, value);
		case REMOVE_CANDIDATE:
			return target.removeCandidate(index, value);
		}
		
		throw new UnsupportedOperationException("Unimplemented solution type: " + type);
	}
	
	public void undo(AnnotatedSudoku target) {
		switch (type) {
		case SOLUTION:
			target.unsetValue(index, value);
			break;
		case REMOVE_CANDIDATE:
			target.addCandidate(index, value);
			break;
		}

		throw new UnsupportedOperationException("Unimplemented solution type: " + type);
	}
	
	@Override
	public String toString() {
		switch (type) {
		case SOLUTION:
			return "Solution";
		case REMOVE_CANDIDATE:
			return "Removed candidate";
		}

		throw new UnsupportedOperationException("Unimplemented solution type: " + type);
	}
	
	public Type getType() { return type; }
	public ASudokuStrategy getSource() { return source; }
	public int getIndex() { return index; }
	public int getValue() { return value; }
	
	public enum Type {
		SOLUTION,
		REMOVE_CANDIDATE,
	}
}
