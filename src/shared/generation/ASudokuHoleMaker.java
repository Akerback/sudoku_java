package shared.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shared.evaluation.SudokuSolver;
import shared.model.Sudoku;
import shared.model.SudokuSelection;
import shared.utility.RuntimeAssert;

public abstract class ASudokuHoleMaker {
	private Random randomizer;
	private SudokuSolver solver = null;
	private List<ISudokuModifier> modifiers = new ArrayList<>();

	/**Get the randomizer. If it hasn't been initialized this also initializes it.
	 *
	 * @return	Random number generator.
	 */
	protected Random getRandomizer() {
		if (randomizer == null) {
			randomizer = new Random(System.currentTimeMillis());
		}

		return randomizer;
	}

	public final void setGrader(SudokuSolver _solver) {
		solver = _solver;
	}

	public final void makeHoles(Sudoku sudoku) {
		RuntimeAssert.notNull(solver);

		SudokuSelection remainingOptions = SudokuSelection.all();

		while (true) {
			SudokuSelection newHoles = getNextHole(remainingOptions);
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
	protected abstract SudokuSelection getNextHole(SudokuSelection holeOptions);
}
