package shared.generation;

import java.util.Random;

import shared.model.Sudoku;

public interface IFilledSudokuGenerator {
	public abstract void generate(Sudoku target, Random randomizer);
}
