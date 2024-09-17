package view;

import controller.SudokuAppController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import shared.model.ASudokuFormatter;
import shared.model.Sudoku;
import shared.model.SudokuSelection;

import shared.utility.RuntimeAssert;

//TODO: make this view independant from mode so it can be reused for the visual solver
public class SudokuView extends GridPane {
	private SudokuAppController controller;
	private ASudokuFormatter formatter;
	private SudokuViewCell[] cells;
	private int selectedIndex = -1;

	private boolean noEvents = false;

	public SudokuView(SudokuAppController _controller, ASudokuFormatter _formatter) {
		cells = new SudokuViewCell[81];
		controller = _controller;
		formatter = _formatter;

		construct();

		this.setSpacing(10);
		this.setAlignment(Pos.CENTER);
	}

	public TextField getField(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		return cells[index].getCellText();
	}

	public void setSpacing(int value) {
		this.setHgap(value);
		this.setVgap(value);
		this.setPadding(new Insets(value, value, value, value));
	}

	public ASudokuFormatter getFormatter() {
		return formatter;
	}

	public void setFormatter(ASudokuFormatter _formatter) {
		formatter = _formatter;
	}

	/**Equivalent to display(Sudoku sudoku, boolean clearNotes = false)
	 * 
	 * @param sudoku
	 */
	public void display(Sudoku sudoku) {
		display(sudoku, false);
	}
	
	/**Display a sudoku, and optionally clear notes.
	 * 
	 * @param sudoku
	 * @param clearNotes
	 */
	public void display(Sudoku sudoku, boolean clearNotes) {
		try {
			//Prevent these assignments from triggering controller events
			noEvents = true;
			//Loop through cells
			for (int i = 0; i < 81; i++) {
				TextField cell = getField(i);

				cell.setEditable(true);
				cell.setText(formatter.getConverter().toString(sudoku.get(i)));

				cell.getStyleClass().remove("prefilled");
				if (sudoku.get(i) != 0) {
					cell.setEditable(false);
					cell.getStyleClass().add("prefilled");
				}
				if (clearNotes) {
					cells[i].clearNotes();
				}
			}
			
			updateIssueHighlighting();
		}
		//Not trying to catch any error, just want noEvents to become false again.
		finally {
			noEvents = false;
		}
	}

	public void updateIssueHighlighting() {
		boolean[] issueMask = controller.getIssueMask();

		for (int index = 0; index < 81; index++) {
			TextField cell = getField(index);

			cell.getStyleClass().remove("issue");
			if (issueMask[index]) {
				cell.getStyleClass().add("issue");
			}
		}
	}
	
	public void onCellChange(int index, String newValue) {
		if (noEvents) {
			return;
		}

		Integer value = formatter.getConverter().fromString(newValue);
		if (value == null) {
			controller.setCell(index, 0);
		}
		else {
			controller.setCell(index, value);
		}

		updateIssueHighlighting();
		updateSameHighlighting();
	}
	
	public void onCellSelected(int index) {
		selectedIndex = index;
		updateSameHighlighting();
	}
	
	public void onCellDeselected(int index) {
		selectedIndex = -1;
	}

	private void updateSameHighlighting() {
		//Selection featuring all with the same digit
		Sudoku sudoku = controller.getCurrentSudoku();

		SudokuSelection sameDigit = new SudokuSelection();
		if ((selectedIndex != -1) && (sudoku.get(selectedIndex) > 0)) {
			sameDigit = sudoku.indicesOf(sudoku.get(selectedIndex), SudokuSelection.all());
		}

		boolean[] sameDigitMask = sameDigit.getAsMask();
		for (int index = 0; index < 81; index++) {
			TextField cell = getField(index);

			cell.getStyleClass().remove("samedigit");
			if (sameDigitMask[index]) {
				cell.getStyleClass().add("samedigit");
			}
		}
	}

	/**Construct the element*/
	private void construct() {
		//For each sudoku square (3x3)
		for (int sqrY = 0; sqrY < 3; sqrY++) {
			for (int sqrX = 0; sqrX < 3; sqrX++) {
				//Construct that square and add it
				GridPane subPane = makeSquare(sqrX, sqrY);

				this.add(subPane, sqrX, sqrY);
			}
		}

		this.addEventFilter(KeyEvent.KEY_PRESSED, this::keyHandling);
	}

	private GridPane makeSquare(int squareX, int squareY) {
		//For each cell (1x1) in this square (3x3)
		GridPane pane = new GridPane(3, 3);
		for (int intY = 0; intY < 3; intY++) {
			for (int intX = 0; intX < 3; intX++) {
				//Construct the cell and attach it to the GridPane
				int index = Sudoku.squarePositionToIndex(squareX, squareY, intX, intY);
				SudokuViewCell entry = new SudokuViewCell(index, formatter);
				entry.addListener(this);
				cells[index] = entry;

				pane.add(entry, intX, intY);
			}
		}

		return pane;
	}

	private void keyHandling(KeyEvent event) {
		if (selectedIndex < 0 || selectedIndex >= 81) {
			System.out.println("[WARNING!] A key press filter event fired without having a cell selected!");
			return;
		}

		//--Arrow key navigation
		if (event.getCode().isArrowKey()) {
			int step = 1;
			if (event.isControlDown()) {
				step = 3;
			}

			int selectedX = selectedIndex % 9;
			int selectedY = selectedIndex / 9;

			//Select direction
			switch (event.getCode()) {
				case UP:
					selectedY -= step;
					break;
				case DOWN:
					selectedY += step;
					break;
				case LEFT:
					selectedX -= step;
					break;
				case RIGHT:
					selectedX += step;
					break;
				default:
					break;
			}

			selectedX = selectedX % 9;
			selectedY = selectedY % 9;

			//If the value itself went negative, the modulo also went negative
			if (selectedX < 0) {
				selectedX += 9;
			}
			if (selectedY < 0) {
				selectedY += 9;
			}

			//Normal 2d -> 1d array math
			getField(selectedY * 9 + selectedX).requestFocus();
			event.consume();
			//No need to update selectedIndex, because the cell itself will set it when it gains focus
		}
		//--Delete/backspace should always clear a cell if it's editable
		else if ((event.getCode() == KeyCode.DELETE) || (event.getCode() == KeyCode.BACK_SPACE)) {
			if (getField(selectedIndex).isEditable()) {
				getField(selectedIndex).setText("");
				event.consume();
			}
		}
	}
}
