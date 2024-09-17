package app;

import java.util.ArrayList;
import java.util.List;

import controller.SudokuAppController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import shared.evaluation.ASudokuStrategy;
import shared.evaluation.SudokuSolver;
import shared.evaluation.strategies.LastInCellStrategy;
import shared.evaluation.strategies.LastInHouseStrategy;
import shared.evaluation.strategies.LinearEliminationStrategy;
import shared.evaluation.strategies.NakedPairsStrategy;
import shared.generation.ISudokuGenerator;
import shared.generation.ASudokuHoleMaker;
import shared.generation.PerSquareEvenHoleMaker;
import shared.generation.WaveCollapseGenerator;
import shared.generation.holemodifiers.DiagonalSymmetryModifier;
import shared.model.ASudokuFormatter;
import shared.model.GameplayFormatter;
import view.SudokuView;
import view.TimerDisplay;

//TODO: split the app into several modes/scenes, each with their own views and controllers
//TODO: theme selector so dark/light can be chosen at runtime
public class SudokuApp extends Application {
	private SudokuAppController controller;
	private SudokuView sudokuView;
	private TimerDisplay timer;
	private ASudokuFormatter formatter;

	private ISudokuGenerator generator;
	private ASudokuHoleMaker holemaker;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		controller = new SudokuAppController(this);
		generator = new WaveCollapseGenerator();
		holemaker = new PerSquareEvenHoleMaker();
		formatter = new GameplayFormatter();
		
		holemaker.addModifier(new DiagonalSymmetryModifier());

		ASudokuStrategy[] strats = {
			new LinearEliminationStrategy(),
			new NakedPairsStrategy(),
			new LastInHouseStrategy(),
			new LastInCellStrategy()
		};

		holemaker.setGrader(new SudokuSolver(strats));

		BorderPane mainLayout = new BorderPane();

		sudokuView = new SudokuView(controller, formatter);
		mainLayout.setTop(makeTopControls());
		mainLayout.setCenter(sudokuView);
		controller.startGame(generator, holemaker, 0.5f);

		//Make the scene
		Scene scene = new Scene(mainLayout);
		scene.getStylesheets().addAll(getStyle());

		//Launch the stage!
		primaryStage.setTitle("I am stage");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Node makeTopControls() {
		HBox topControls = new HBox();
		topControls.setAlignment(Pos.CENTER_LEFT);
		topControls.setPrefWidth(sudokuView.getWidth());
		topControls.getStyleClass().add("top_controls");


		Button newGameButton = new Button("New Game");
		newGameButton.setAlignment(Pos.CENTER_LEFT);
		newGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//Start a new game
				controller.startGame(generator, holemaker, 0.5f);
				//Try to focus on the middle square
				sudokuView.getField(40).requestFocus();
			}
		});

		timer = new TimerDisplay(90);
		timer.getStyleClass().add("text");
		timer.setTextAlignment(TextAlignment.RIGHT);

		Region padding = new Region();

		topControls.getChildren().add(newGameButton);
		topControls.getChildren().add(padding);//Padding to force timer to the the right edge
		topControls.getChildren().add(timer);

		HBox.setHgrow(padding, Priority.ALWAYS);
		
		return topControls;
	}

	public List<String> getStyle() {
		List<String> results = new ArrayList<>();

		System.out.println(getClass().getResource(""));

		results.add(getClass().getResource("application.css").toExternalForm());
		results.add(getClass().getResource("dark.css").toExternalForm());

		return results;
	}

	/**Callback for when a new game was started*/
	public void onNewGame() {
		sudokuView.display(controller.getCurrentSudoku(), true);
		timer.start();
	}

	public void onGameWon() {
		timer.stop();
	}

	/**Callback for when the sudoku was changed.*/
	public void onSudokuEdited(int editedIndex) {

	}
}
