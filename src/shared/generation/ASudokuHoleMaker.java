package shared.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shared.evaluation.Difficulty;
import shared.evaluation.SudokuSolver;
import shared.model.Sudoku;
import shared.model.SudokuSelection;
import shared.utility.RuntimeAssert;

public abstract class ASudokuHoleMaker {
	private SudokuSolver solver = null;
	private List<ISudokuModifier> modifiers = new ArrayList<>();

	public final void setGrader(SudokuSolver _solver) {
		solver = _solver;
	}

	public final void makeHoles(Sudoku sudoku, Difficulty targetDifficulty, Random randomizer) {
		RuntimeAssert.notNull(solver);

		SudokuSelection remainingOptions = SudokuSelection.all();

		while (true) {
			SudokuSelection newHoles = getNextHole(remainingOptions, randomizer);
			for (ISudokuModifier modifier : modifiers) {
				modifier.apply(newHoles);
			}

			remainingOptions.removeAll(newHoles);

			if (remainingOptions.size() < 40) {
				Sudoku evalSudoku = sudoku.clone();
				evalSudoku.fill(remainingOptions.getInverse(), 0);

				if (!solver.hasUniqueSolution(evalSudoku)) {
					remainingOptions = remainingOptions.getUnionWith(newHoles);
					break;
				}
			}
		}

		sudoku.fill(remainingOptions.getInverse(), 0);
		solver.grade(sudoku);
	}

	public final void addModifier(ISudokuModifier modifier) {
		modifiers.add(modifier);
	}

	public final void removeModifier(ISudokuModifier modifier) {
		modifiers.remove(modifier);
	}

	public final void clearModifiers() {
		modifiers.clear();
	}

	/**Generate a single hole.
	 *
	 * @return 	SudokuSelection, where all values in the selection will become holes.
	 */
	protected abstract SudokuSelection getNextHole(SudokuSelection holeOptions, Random randomizer);
}
