package view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import shared.model.ASudokuFormatter;
import shared.model.ISudokuDisplayObserver;
import shared.model.Sudoku;

import shared.utility.RuntimeAssert;

public class SudokuDisplay extends GridPane {
	private List<ISudokuDisplayObserver> observers;
	
	private ASudokuFormatter formatter;
	private SudokuDisplayCell[] cells;
	private IntegerProperty selectedIndex = new SimpleIntegerProperty(-1);

	private boolean noEvents = false;

	public SudokuDisplay(ASudokuFormatter _formatter) {
		cells = new SudokuDisplayCell[81];
		formatter = _formatter;
		observers = new ArrayList<>();

		construct();

		this.setSpacing(10);
		this.setAlignment(Pos.CENTER);
	}
	
	public void focusOn(int index) {
		getField(index).requestFocus();
	}
	
	public int getSelectedIndex() {
		return selectedIndex.get();
	}

	public void setSpacing(int value) {
		this.setHgap(value);
		this.setVgap(value);
		this.setPadding(new Insets(value, value, value, value));
	}

	public void setFormatter(ASudokuFormatter _formatter) {
		formatter = _formatter;
		for (SudokuDisplayCell cell : cells) {
			cell.setFormatter(formatter);
		}
	}
	
	public void addObserver(ISudokuDisplayObserver observer) {
		observers.add(observer);
	}
	
	public void removeObserver(ISudokuDisplayObserver observer) {
		observers.remove(observer);
	}
	
	/**Add a style class to a given cell unless the cell already has this styleclass
	 * 
	 * @param cellIndex
	 * @param styleClass
	 * @return	true if added, false otherwise.
	 */
	public boolean addCellStyle(int cellIndex, String styleClass) {
		RuntimeAssert.inRange(cellIndex, 0, 81);
		RuntimeAssert.notNull(styleClass);
		
		return cells[cellIndex].addStyle(styleClass);
	}
	
	/**Remove a style class from a given cell unless the cell doesn't have this style class
	 * 
	 * @param cellIndex
	 * @param styleClass
	 * @return	true if removed, false otherwise.
	 */
	public boolean removeCellStyle(int cellIndex, String styleClass) {
		RuntimeAssert.inRange(cellIndex, 0, 81);
		RuntimeAssert.notNull(styleClass);
		
		return cells[cellIndex].removeStyle(styleClass);
	}
	
	public boolean highlight(Iterable<Integer> selection, String styleClass) {
		boolean madeChange = false;
		for (Integer index : selection) {
			madeChange |= addCellStyle(index, styleClass);
		}
		
		return madeChange;
	}
	
	public boolean clearHighlight(Iterable<Integer> selection, String styleClass) {
		boolean madeChange = false;
		for (Integer index : selection) {
			madeChange |= removeCellStyle(index, styleClass);
		}
		
		return madeChange;
	}
	
	public void setCellEditable(int cellIndex, boolean editable) {
		RuntimeAssert.inRange(cellIndex, 0, 81);
		
		getField(cellIndex).setEditable(editable);
	}
	
	public void setCellsEditable(Iterable<Integer> selection, boolean editable) {
		for (Integer index : selection) {
			setCellEditable(index, editable);
		}
	}
	
	/**Display a sudoku, and clear all notes. Equivalent to display(Sudoku sudoku, boolean clearNotes = true)
	 * 
	 * @param sudoku
	 */
	public void display(Sudoku sudoku) {
		display(sudoku, true);
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
				
				if (clearNotes) {
					cells[i].clearNotes();
				}
			}
			
			//Notify observers
			observers.forEach(obs -> obs.onDisplayedSudokuChanged(sudoku));
		}
		//Not trying to catch any error, just want noEvents to become false again.
		finally {
			noEvents = false;
		}
	}
	
	public void onCellChange(int index, int oldValue, int newValue) {
		if (noEvents) {
			return;
		}
		
		//Notify observers
		observers.forEach(obs -> obs.onCellChange(index, oldValue, newValue));
	}
	
	public void onCellSelected(int index, boolean wasSelected) {
		selectedIndex.set(index);
		
		//Notify observers
		observers.forEach(obs -> obs.onCellSelected(index, wasSelected));
	}
	
	public void onCellDeselected(int index, boolean wasSelected) {
		selectedIndex.set(-1);
		
		//Notify observers
		observers.forEach(obs -> obs.onCellDeselected(index, wasSelected));
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
				SudokuDisplayCell entry = new SudokuDisplayCell(index, formatter);
				entry.setView(this);
				cells[index] = entry;

				pane.add(entry, intX, intY);
			}
		}

		return pane;
	}

	private TextField getField(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		return cells[index].getCellText();
	}

	private void keyHandling(KeyEvent event) {
		RuntimeAssert.inRange(selectedIndex.get(), 0, 81);

		//--Arrow key navigation
		if (event.getCode().isArrowKey()) {
			arrowNavigation(event);
		}
		//--Delete/backspace should always clear a cell if it's editable
		else if ((event.getCode() == KeyCode.DELETE) || (event.getCode() == KeyCode.BACK_SPACE)) {
			clearCell(event);
		}
	}
	
	private void arrowNavigation(KeyEvent event) {
		int step = 1;
		if (event.isControlDown()) {
			step = 3;
		}

		int selectedX = selectedIndex.get() % 9;
		int selectedY = selectedIndex.get() / 9;

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
	
	private void clearCell(KeyEvent event) {
		TextField selectedField = getField(selectedIndex.get());
		
		if (selectedField.isEditable()) {
			selectedField.setText("");
			event.consume();
		}
	}
}
