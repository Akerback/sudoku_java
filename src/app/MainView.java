package app;

import controller.SudokuGameController;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import shared.evaluation.Difficulty;
import shared.generation.SudokuGenerator;
import shared.model.ASudokuFormatter;
import view.SudokuDisplay;
import view.TimerDisplay;

public class MainView extends BorderPane {
	private SudokuGameController controller;
	private SudokuDisplay sudokuDisplay;
	private TimerDisplay timer;
	
	private SudokuGenerator generator;
	private ASudokuFormatter formatter;
	
	public MainView(SudokuGenerator _generator, ASudokuFormatter _formatter) {
		generator = _generator;
		formatter = _formatter;
		sudokuDisplay = new SudokuDisplay(formatter);
		
		controller = new SudokuGameController(sudokuDisplay);
		controller.hasWonProperty().addListener((actor, oldVal, newVal) -> {
			if ((oldVal == false) && (newVal == true)) {
				onGameWon();
			}
		});
		
		this.setTop(makeTopControls());
		this.setCenter(sudokuDisplay);
	}
	
	private Node makeTopControls() {
		HBox topControls = new HBox();
		topControls.setAlignment(Pos.CENTER_LEFT);
		topControls.setPrefWidth(sudokuDisplay.getWidth());
		topControls.getStyleClass().add("top_controls");

		Button newGameButton = new Button("New Game");
		newGameButton.setAlignment(Pos.CENTER_LEFT);
		newGameButton.setOnAction(ev -> onNewGame(ev));

		timer = new TimerDisplay(90);
		timer.getStyleClass().add("text");
		timer.setTextAlignment(TextAlignment.RIGHT);

		Region padding = new Region();
		padding.setPrefWidth(0);
		padding.setMinWidth(0);

		topControls.getChildren().add(newGameButton);
		topControls.getChildren().add(padding);//Padding to force timer to the the right edge
		topControls.getChildren().add(timer);

		HBox.setHgrow(padding, Priority.ALWAYS);
		
		return topControls;
	}
	
	public void forceNewGame() {
		controller.startGame(generator.generate(Difficulty.ANY));
		sudokuDisplay.display(controller.getCurrentSudoku(), true);
		sudokuDisplay.focusOn(40);
		
		timer.start();
	}

	/**Callback for when a new game was started*/
	private void onNewGame(ActionEvent event) {
		forceNewGame();
		
		event.consume();
	}

	private void onGameWon() {
		timer.stop();
	}
}
