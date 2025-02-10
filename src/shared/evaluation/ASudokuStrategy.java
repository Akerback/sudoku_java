package shared.evaluation;

import java.util.List;

public abstract class ASudokuStrategy {
	public abstract List<StrategyResult> apply(AnnotatedSudokuView sudokuEvalView);
	public abstract Difficulty getDifficulty();
	@Override
	public abstract String toString();

	/**Construct a result indicating the removal of a candidate from a given index.
	 * It is preferrable to use this over manually constructing a new RemovedCandidateResult
	 *
	 * @param index		The index that a candidate value has been removed from.
	 * @param value		The candidate value that was removed from the given index.
	 * @return			AStrategyResult representing the removal of a candidate.
	 */
	public StrategyResult makeRemovalResult(int index, int value, ResultReason reasoning) {
		return StrategyResult.RemovedCandidate(index, value, this, reasoning);
	}

	/**Construct a result indicating that a given index has been solved to a specific value.
	 * It is preferrable to use this over manually constructing a new SolutionResult
	 *
	 * @param index		The index that has been solved.
	 * @param value		The value that the given index will be solved to.
	 * @return			AStrategyResult representing an index being solved to a value.
	 */
	public StrategyResult makeSolutionResult(int index, int value, ResultReason reasoning) {
		return StrategyResult.Solved(index, value, this, reasoning);
	}
}
