package view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.geometry.Pos;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import shared.model.ASudokuFormatter;
import shared.utility.Constraints;
import shared.utility.RuntimeAssert;

public class SudokuViewCell extends StackPane {
	private Text[] notes;
	private TextField cell;
	private int representedIndex;
	private List<SudokuView> listeners;
	
	public SudokuViewCell(int index, ASudokuFormatter formatter) {
		RuntimeAssert.inRange(index, 0, 81);
		RuntimeAssert.notNull(formatter);
		
		representedIndex = index;
		listeners = new ArrayList<SudokuView>();
		
		cell = constructCell(formatter);
		this.getChildren().add(cell);
		this.getChildren().add(constructNotes());
	}
	
	public TextField getCellText() { return cell; }
	
	public void addListener(SudokuView newListener) {
		RuntimeAssert.notNull(newListener);
		
		listeners.add(newListener);
	}
	
	public void setFormatter(ASudokuFormatter formatter) {
		RuntimeAssert.notNull(formatter);
		
		cell.setTextFormatter(new TextFormatter<>(formatter.getConverter(), 0, formatter.getFilter()));
	}
	
	public void removeListener(SudokuView removedListener) {
		//In this case, null is fine
		listeners.remove(removedListener);
	}
	
	public void clearNotes() {
		for (int i = 0; i < 9; i++) {
			notes[i].setVisible(false);
		}
	}
	
	public void toggleNote(int candidate) {
		RuntimeAssert.inRange(candidate, 1, 10);
		
		Text note = notes[candidate - 1];
		note.setVisible(!note.isVisible());
	}
	
	public void setNoteVisibility(int candidate, boolean visible) {
		RuntimeAssert.inRange(candidate, 1, 10);
		
		Text note = notes[candidate - 1];
		note.setVisible(visible);
	}
	
	private TextField constructCell(ASudokuFormatter formatter) {
		TextField field = new TextField();
		field.setMinSize(50, 50);
		field.setMaxSize(50, 50);
		field.setAlignment(Pos.CENTER);
		
		//Change bindings
		field.setTextFormatter(new TextFormatter<>(formatter.getConverter(), 0, formatter.getFilter()));
		field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {
				notifyCellChange(newVal);
			}
		});

		//Focus bindings
		field.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean wasSelected, Boolean isSelected) {
				if (isSelected) {
					notifySelected();
				}
				else {
					notifyDeselected();
				}
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
	
	private void notifyCellChange(String newValue) {
		for (SudokuView view : listeners) {
			view.onCellChange(representedIndex, newValue);
		}
	}
	
	private void notifySelected() {
		for (SudokuView view : listeners) {
			view.onCellSelected(representedIndex);
		}
	}
	
	private void notifyDeselected() {
		for (SudokuView view : listeners) {
			view.onCellDeselected(representedIndex);
		}
	}
}
