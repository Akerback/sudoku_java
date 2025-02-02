package shared.model;

public interface ISudokuDisplayObserver {
	public void onDisplayedSudokuChanged(Sudoku newSudoku);
	
	public void onCellChange(int index, int oldValue, int newValue);

	public void onCellSelected(int index, boolean wasSelected);

	public void onCellDeselected(int index, boolean wasSelected);
}
