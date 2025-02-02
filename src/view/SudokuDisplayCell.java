package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.geometry.Pos;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import shared.model.ASudokuFormatter;
import shared.utility.Constraints;
import shared.utility.RuntimeAssert;

public class SudokuDisplayCell extends StackPane {
	private Text[] notes;
	private TextField cell;
	private SudokuDisplay view;
	private int representedIndex;
	
	public SudokuDisplayCell(int index, ASudokuFormatter formatter) {
		RuntimeAssert.inRange(index, 0, 81);
		RuntimeAssert.notNull(formatter);
		
		representedIndex = index;
		view = null;
		
		cell = constructCell(formatter);
		this.getChildren().add(cell);
		this.getChildren().add(constructNotes());
	}
	
	public TextField getCellText() { return cell; }
	
	public void setView(SudokuDisplay newListener) {
		view = newListener;
	}
	
	public void setFormatter(ASudokuFormatter formatter) {
		RuntimeAssert.notNull(formatter);
		
		cell.setTextFormatter(new TextFormatter<>(formatter.getConverter(), 0, formatter.getFilter()));
	}
	
	/**Add a style class unless it already exists.
	 * 
	 * @param styleClass
	 * @return	true if added, false otherwise.
	 */
	public boolean addStyle(String styleClass) {
		if (!cell.getStyleClass().contains(styleClass)) {
			//System.out.printf("Added style %s to cell at index %d\n", styleClass, representedIndex);
			cell.getStyleClass().add(styleClass);
			return true;
		}
		return false;
	}
	
	/**Remove a style class unless it doesn't exists.
	 * 
	 * @param styleClass
	 * @return	true if removed, false otherwise.
	 */
	public boolean removeStyle(String styleClass) {
		if (cell.getStyleClass().contains(styleClass)) {
			//System.out.printf("Removed style %s from cell at index %d\n", styleClass, representedIndex);
			cell.getStyleClass().remove(styleClass);
			return true;
		}
		return false;
	}
	
	public void clearNotes() {
		for (int i = 0; i < 9; i++) {
			notes[i].setVisible(false);
		}
	}
	
	public void setNoteVisible(int candidate, boolean visible) {
		RuntimeAssert.inRange(candidate, 1, 10);
		
		Text note = notes[candidate - 1];
		note.setVisible(visible);
	}
	
	public boolean isNoteVisible(int candidate) {
		RuntimeAssert.inRange(candidate, 1, 10);
		
		Text note = notes[candidate - 1];
		return note.isVisible();
	}
	
	private TextField constructCell(ASudokuFormatter formatter) {
		TextField field = new TextField();
		field.setMinSize(50, 50);
		field.setMaxSize(50, 50);
		field.setAlignment(Pos.CENTER);
		
		//Change bindings
		field.setTextFormatter(new TextFormatter<>(formatter.getConverter(), 0, formatter.getFilter()));
		field.textProperty().addListener((observable, oldVal, newVal) -> {
			StringConverter<Integer> converter = formatter.getConverter();
			notifyCellChange(converter.fromString(oldVal), converter.fromString(newVal));
		});

		//Focus bindings
		field.focusedProperty().addListener((observable, wasSelected, isSelected) -> {
			if (isSelected) {
				notifySelected(wasSelected);
			}
			else {
				notifyDeselected(wasSelected);
			}
		});
		
		return field;
	}
	
	private GridPane constructNotes() {
		GridPane pane = new GridPane();
		pane.setMinSize(50, 50);
		pane.setMaxSize(50, 50);
		
		pane.getColumnConstraints().add(Constraints.percentWidth(33.333)); //First column
		pane.getColumnConstraints().add(Constraints.percentWidth(33.333)); //Second column
		
		pane.getRowConstraints().add(Constraints.percentHeight(33.333)); //First row
		pane.getRowConstraints().add(Constraints.percentHeight(33.333)); //Second row
		
		//pane.setPadding(new Insets(2));
		pane.setAlignment(Pos.CENTER);
		
		pane.setPickOnBounds(false);
		
		notes = new Text[9];
		for (int i = 0; i < 9; i++) {
			int cand = i + 1;
			
			int x = i % 3;
			int y = i / 3;
			
			Text candText = new Text(Integer.toString(cand));
			candText.setVisible(false);
			candText.setPickOnBounds(false);
			candText.setTextAlignment(TextAlignment.CENTER);
			candText.getStyleClass().add("notes");
			
			notes[i] = candText;
			pane.add(candText, x, y);
		}
		
		return pane;
	}
	
	private void notifyCellChange(int oldValue, int newValue) {
		view.onCellChange(representedIndex, oldValue, newValue);
	}
	
	private void notifySelected(boolean wasSelected) {
		view.onCellSelected(representedIndex, wasSelected);
	}
	
	private void notifyDeselected(boolean wasSelected) {
		view.onCellDeselected(representedIndex, wasSelected);
	}
}
