package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import shared.model.ISudokuDisplayObserver;
import shared.model.Sudoku;
import shared.model.SudokuSelection;
import view.SudokuDisplay;

public class SudokuGameController implements ISudokuDisplayObserver {
	private SudokuDisplay display;
	private Sudoku currentSudoku;
	private BooleanProperty hasWon = new SimpleBooleanProperty(false);

	public SudokuGameController(SudokuDisplay _display) {
		display = _display;
		currentSudoku = new Sudoku();
		
		display.addObserver(this);
	}
	
	public void setDisplay(SudokuDisplay _display) {
		display = _display;
	}

	public void startGame(Sudoku _sudoku) {
		hasWon.set(false);
		currentSudoku = _sudoku;
	}

	public void setCell(int index, int value) {
		if (currentSudoku == null) {
			throw new IllegalStateException("Cannot use setCell because no game has been started!");
		}

		//Checks passed, set the cell and inform the view
		currentSudoku.set(index, value);
		updateSolveStatus();
	}
	
	public BooleanProperty hasWonProperty() {
		return hasWon;
	}

	private void updateSolveStatus() {
		if (!hasWon.get()) {
			if (currentSudoku.isSolved()) {
				hasWon.set(true);
			}
		}
	}

	public Sudoku getCurrentSudoku() {
		return currentSudoku;
	}
	
	private void updateSameHighlight() {
		display.clearHighlight(SudokuSelection.all(), "samedigit");
		int currentSelectionIndex = display.getSelectedIndex();
		if (currentSelectionIndex == -1) {
			return;
		}
		
		int valueAtSelection = currentSudoku.get(currentSelectionIndex);
		if (valueAtSelection != 0) {
			display.highlight(currentSudoku.valueFilter(valueAtSelection, SudokuSelection.all()), "samedigit");
		}
	}

	private void updateErrorHighlight() {
		display.clearHighlight(SudokuSelection.all(), "issue");
		display.highlight(currentSudoku.getIssues(), "issue");
	}
	
	@Override
	public void onDisplayedSudokuChanged(Sudoku newSudoku) {
		SudokuSelection emptyCells = newSudoku.valueFilter(0, SudokuSelection.all());
		SudokuSelection nonEmpty = emptyCells.getInverse();
		display.setCellsEditable(emptyCells, true);
		display.setCellsEditable(nonEmpty, false);
		display.clearHighlight(emptyCells, "prefilled");
		display.highlight(nonEmpty, "prefilled");
		
		updateSameHighlight();
		updateErrorHighlight();
	}

	@Override
	public void onCellChange(int index, int oldValue, int newValue) {
		//System.out.printf("Cell %d changed to: %d\n", index, newValue);
		setCell(index, newValue);
		updateSameHighlight();
		updateErrorHighlight();
	}

	@Override
	public void onCellSelected(int index, boolean wasSelected) {
		//System.out.printf("Selected cell %d\n", index);
		updateSameHighlight();
	}

	@Override
	public void onCellDeselected(int index, boolean wasSelected) {
		//System.out.printf("Deselected cell %d\n", index);
		updateSameHighlight();
	}
}
