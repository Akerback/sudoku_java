package shared.generation.holemodifiers;

import shared.generation.ISudokuModifier;
import shared.model.SudokuSelection;

public abstract class ASymmetryModifier implements ISudokuModifier {
	protected abstract int getMirrorIndex(int index);

	@Override
	public void apply(SudokuSelection selection) {
		SudokuSelection addedHoles = new SudokuSelection();

		for (Integer index : selection) {
			int mirrorIndex = getMirrorIndex(index);
			addedHoles.add(mirrorIndex);
		}

		selection.addAll(addedHoles);
	}
}
