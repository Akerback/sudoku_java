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
	private List<ISudokuModifier> modifiers = new ArrayList<>();

	public final SudokuSelection getNextHoles(SudokuSelection remainingOptions, Random randomizer) {
		SudokuSelection newHoles = new SudokuSelection(getNextHole(remainingOptions, randomizer));
		
		modifiers.forEach(mod -> mod.apply(newHoles));
		
		return newHoles;
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
	protected abstract int getNextHole(SudokuSelection holeOptions, Random randomizer);
}
