package shared.generation;

import java.util.Random;

import shared.model.Sudoku;

public interface IFilledSudokuGenerator {
	public abstract Sudoku generate(Random randomizer);
}
