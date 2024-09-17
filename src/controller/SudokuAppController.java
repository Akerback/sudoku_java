package controller;

import app.SudokuApp;
import shared.generation.ISudokuGenerator;
import shared.generation.ASudokuHoleMaker;
import shared.model.Sudoku;
import shared.utility.RuntimeAssert;

public class SudokuAppController {
	private SudokuApp view;
	private Sudoku currentSudoku;
	private boolean hasWon = false;

	public SudokuAppController(SudokuApp _view) {
		view = _view;
		currentSudoku = null;
	}

	public void startGame(ISudokuGenerator generator, ASudokuHoleMaker holemaker, float holeAmount) {
		RuntimeAssert.notNull(generator);
		RuntimeAssert.notNull(holemaker);
		
		hasWon = false;
		currentSudoku = new Sudoku();
		generator.generate(currentSudoku);
		holemaker.makeHoles(currentSudoku);

		view.onNewGame();
	}

	public boolean[] getIssueMask() {
		return currentSudoku.getIssueMask();
	}

	public void setCell(int index, int value) {
		if (currentSudoku == null) {
			throw new IllegalStateException("Cannot use setCell because no game has been started!");
		}

		//Checks passed, set the cell and inform the view
		currentSudoku.set(index, value);
		view.onSudokuEdited(index);
		checkWinStatus();
	}

	private void checkWinStatus() {
		if (!hasWon) {
			if (currentSudoku.isSolved()) {
				hasWon = true;
				view.onGameWon();
			}
		}
	}

	public Sudoku getCurrentSudoku() {
		return currentSudoku;
	}
}
