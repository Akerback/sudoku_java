package app;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.evaluation.ASudokuStrategy;
import shared.evaluation.SudokuSolver;
import shared.evaluation.strategies.LastInCellStrategy;
import shared.evaluation.strategies.LastInHouseStrategy;
import shared.evaluation.strategies.LinearEliminationStrategy;
import shared.evaluation.strategies.NakedPairsStrategy;
import shared.generation.IFilledSudokuGenerator;
import shared.generation.ASudokuHoleMaker;
import shared.generation.PerSquareEvenHoleMaker;
import shared.generation.SudokuGenerator;
import shared.generation.WaveCollapseGenerator;
import shared.generation.holemodifiers.DiagonalSymmetryModifier;
import shared.model.ASudokuFormatter;
import shared.model.GameplayFormatter;

//TODO: visual solver
//TODO: split the app into several modes/scenes, each with their own views and controllers
//TODO: theme selector so dark/light can be chosen at runtime
public class SudokuAppLauncher extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		ASudokuFormatter formatter = new GameplayFormatter();
		IFilledSudokuGenerator fillGenerator = new WaveCollapseGenerator();
		ASudokuHoleMaker holeMaker = new PerSquareEvenHoleMaker();
		
		holeMaker.addModifier(new DiagonalSymmetryModifier());

		ASudokuStrategy[] strats = {
			new LinearEliminationStrategy(),
			new NakedPairsStrategy(),
			new LastInHouseStrategy(),
			new LastInCellStrategy()
		};

		SudokuSolver grader = new SudokuSolver(strats);
		
		SudokuGenerator generator = new SudokuGenerator(fillGenerator, holeMaker, grader);

		//Make the scene
		Scene scene = new Scene(new MainView(generator, formatter));
		scene.getStylesheets().addAll(getStyleList());

		//Launch the stage!
		primaryStage.setTitle("I am stage");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public List<String> getStyleList() {
		List<String> results = new ArrayList<>();

		System.out.println(getClass().getResource(""));

		results.add(getClass().getResource("application.css").toExternalForm());
		results.add(getClass().getResource("light.css").toExternalForm());

		return results;
	}
}
