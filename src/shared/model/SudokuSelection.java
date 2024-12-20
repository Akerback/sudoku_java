package shared.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import shared.utility.RuntimeAssert;

public class SudokuSelection implements Iterable<Integer> {
	private boolean[] isIncluded = new boolean[81];
	private int includeCount = 0;

	/**Construct an empty selection.*/
	public SudokuSelection() {}

	/**Construct a selection with only one index.
	 *
	 * @param _index	The index that will be the only member of the selection.
	 */
	public SudokuSelection(int _index) {
		this.add(_index);
	}

	/**Construct a selection from a Collection of indices.
	 *
	 * @param _indices	The collection of indices to include. Duplicates will be ignored.
	 * @throws NullPointerException	thrown if given collection is null.
	 */
	public SudokuSelection(Iterable<Integer> _indices) throws NullPointerException {
		for (Integer index : _indices) {
			this.add(index);
		}
	}

	/**Construct a selection containing all cells.
	 *
	 * @return	The selection containing all cells.
	 */
	public static SudokuSelection all() {
		SudokuSelection result = new SudokuSelection();
		Arrays.fill(result.isIncluded, true);
		result.includeCount = 81;

		return result;
	}

	/**Construct a selection for the given row.
	 *
	 * @param row	The row index to construct a selection for.
	 * @return	The selection containing only the given row.
	 */
	public static SudokuSelection row(int row) {
		RuntimeAssert.inRange(row, 0, 9);

		SudokuSelection result = new SudokuSelection();
		
		int baseInd = row * 9;
		for (int col = 0; col < 9; col++) {
			int index = baseInd + col;
			result.add(index);
		}

		return result;
	}

	/**Construct a selection for the given column.
	 *
	 * @param col	The column index to construct a selection for.
	 * @return	The selection containing only the given column.
	 */
	public static SudokuSelection column(int col) {
		RuntimeAssert.inRange(col, 0, 9);

		SudokuSelection result = new SudokuSelection();
		
		int index = col;
		while (index < 81) {
			result.add(index);
			index += 9;
		}

		return result;
	}

	/**Construct a selection for the given square index.
	 *
	 * @param sqr	The square index to construct a selection for.
	 * @return	The selection containing only the given square.
	 */
	public static SudokuSelection square(int sqr) {
		final int[] OFFSETS = {0, 1, 2, 9, 10, 11, 18, 19, 20};
		RuntimeAssert.inRange(sqr, 0, 9);

		SudokuSelection result = new SudokuSelection();

		int squareX = sqr % 3;
		int squareY = sqr / 3;

		int baseIndex = squareY * 27 + squareX * 3;

		for (int offsetIndex = 0; offsetIndex < 9; offsetIndex++) {
			int index = baseIndex + OFFSETS[offsetIndex];
			result.add(index);
		}

		return result;
	}

	/**Get the selection containing all cells affected by the cell at the given index. Does not contain the index itself.
	 *
	 * @param index	Given index.
	 * @return	Selection equivalent to the union between the given index's row, column, and square selections.
	 */
	public static SudokuSelection affectedBy(int index) {
		int row = Sudoku.indexToRow(index);
		int col = Sudoku.indexToColumn(index);
		int sqr = Sudoku.indexToSquare(index);
		
		return row(row).getUnionWith(column(col)).getUnionWith(square(sqr)).getDifferenceWith(new SudokuSelection(index));
	}

	/**Retrieve a 81 element array representing the selection as a mask, where true values are part of the selection.
	 *
	 * @return	The mask array (boolean[81])
	 */
	public boolean[] getAsMask() {
		return Arrays.copyOf(isIncluded, 81);
	}
	
	public int getRandom(Random randomizer) {
		int chosenIndex = randomizer.nextInt(size());

		int counter = 0;
		for (Integer index : this) {
			if (counter == chosenIndex) {
				return index;
			}

			counter++;
		}

		throw new IllegalStateException("SudokuSelection.getRandom() has failed spectacularly!");
	}

	/**Get the union of this selection and another selection without modifying either selection.
	 *
	 * @param other	The selection to join with
	 * @return	The union of the selections (all indices from both are included)
	 */
	public SudokuSelection getUnionWith(SudokuSelection other) {
		SudokuSelection joinedSet = new SudokuSelection(this);
		joinedSet.addAll(other);

		return joinedSet;
	}

	/**Get the intersection of this selection and another selection without modifying either selection.
	 *
	 * @param other	The selection to intersect with.
	 * @return	The intersection of the selections (only indices present in both are included)
	 */
	public SudokuSelection getIntersectionWith(SudokuSelection other) {
		SudokuSelection slicedSet = new SudokuSelection(this);
		slicedSet.retainAll(other);
		//slicedSet.removeIf(index -> (!other.indices.contains(index)));//Remove all elements NOT present in other

		return slicedSet;
	}

	/**Get the difference between this selection and another selection without modifying either selection.
	 *
	 * @param other	The set to subtract.
	 * @return	The difference of the selections (only indices present in this, but not in other are included)
	 */
	public SudokuSelection getDifferenceWith(SudokuSelection other) {
		SudokuSelection diffedSet = new SudokuSelection(this);
		//diffedSet.removeIf(index -> (other.indices.contains(index)));//Remove all elements present in other
		diffedSet.removeAll(other);

		return diffedSet;
	}

	/**Get the inverse of the current selection. Current selection is NOT modified.
	 *
	 * @return	The inverted selection.
	 */
	public SudokuSelection getInverse() {
		return SudokuSelection.all().getDifferenceWith(this);
	}

	//--Collection methods----------------------------------------------------------
	@Override
	public Iterator<Integer> iterator() {
		return new Iter(this);
	}
	
	public class Iter implements Iterator<Integer> {
		SudokuSelection target;
		int nextIncluded;
		
		public Iter(SudokuSelection _target) {
			target = _target;
			nextIncluded = getNextIncluded(0);
		}
		
		@Override
		public boolean hasNext() {
			return nextIncluded >= 0;
		}

		@Override
		public Integer next() {
			int result = nextIncluded;
			
			nextIncluded = getNextIncluded(nextIncluded + 1);
			return result;
		}
		
		private int getNextIncluded(int startingFrom) {
			for (int i = startingFrom; i < 81; i++) {
				if (target.isIncluded[i]) {
					return i;
				}
			}
			
			return -1;
		}
	}

	public int size() {
		return includeCount;
	}

	public boolean isEmpty() {
		return (size() == 0);
	}

	public boolean contains(int index) {
		RuntimeAssert.inRange(index, 0, 81);
		return isIncluded[index];
	}

	public boolean add(int index) {
		RuntimeAssert.inRange(index, 0, 81);
		
		if (!isIncluded[index]) {
			isIncluded[index] = true;
			includeCount++;
			return true;
		}
		else {
			return false;
		}
	}

	public boolean remove(int index) {
		RuntimeAssert.inRange(index, 0, 81);
		
		if (isIncluded[index]) {
			isIncluded[index] = false;
			includeCount--;
			return true;
		}
		else {
			return false;
		}
	}

	public boolean containsAll(Iterable<Integer> c) {
		for (Integer obj : c) {
			if (!contains(obj)) {
				return false;
			}
		}
		
		return true;
	}

	/**Works as a union operation, where the current selection is modified.*/
	public boolean addAll(Iterable<Integer> c) {
		boolean modified = false; 
		
		for (Integer index : c) {
			//Skip bad values
			if ((index == null) || (index < 0) || (index >= 81)) {
				continue;
			}
			
			modified |= add(index);
		}
		
		return modified;
	}

	/**Works as a difference operation, where the current selection is modified.*/
	public boolean removeAll(Iterable<Integer> c) {
		boolean modified = false;
		
		for (Integer index : toAcceptableIndices(c)) {
			modified |= remove(index);
		}
		
		return modified;
	}

	/**Works as an intersection operation, where the current selection is modified.*/
	public boolean retainAll(Iterable<Integer> c) {
		boolean modified = false;
		
		List<Integer> retained = toAcceptableIndices(c);
		for (int i = 0; i < 81; i++) {
			if (!retained.contains(i)) {
				modified |= remove(i);
			}
		}
		
		return modified;
	}

	public void clear() {
		Arrays.fill(isIncluded, false);
		includeCount = 0;
	}
	
	/**Filter out all null values, and values that can't be converted to an integer in range 0 (inclusive) to 81 (exclusive)
	 * 
	 * @param collection	Source collection.
	 * @return				Filtered list of indices that aren't null, and are in range 0 (inclusive) to 81 (exclusive)
	 */
	private List<Integer> toAcceptableIndices(Iterable<Integer> collection) {
		List<Integer> acceptable = new ArrayList<Integer>();
		
		for (Integer candidate : collection) {
			if ((candidate == null) || (candidate < 0) || (candidate >= 81)) {
				continue;
			}
			
			acceptable.add(candidate);
		}
		
		return acceptable;
	}
}
