package shared.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import shared.utility.RuntimeAssert;

public class SudokuSelection implements Set<Integer> {
	private Set<Integer> indices;

	/**Construct an empty selection.*/
	public SudokuSelection() {
		indices = new HashSet<>();
	}

	/**Construct a selection with only one index.
	 *
	 * @param _index	The index that will be the only member of the selection.
	 */
	public SudokuSelection(int _index) {
		indices = new HashSet<>();
		indices.add(_index);
	}

	/**Construct a selection from a Collection of indices.
	 *
	 * @param _indices	The collection of indices to include. Duplicates will be ignored.
	 * @throws NullPointerException	thrown if given collection is null.
	 */
	public SudokuSelection(Collection<Integer> _indices) throws NullPointerException {
		indices = new HashSet<>(_indices);
	}

	/**Construct a selection containing all cells.
	 *
	 * @return	The selection containing all cells.
	 */
	public static SudokuSelection all() {
		Set<Integer> _indices = new HashSet<>(81);

		for (int i = 0; i < 81; i++) {
			_indices.add(i);
		}

		return new SudokuSelection(_indices);
	}

	/**Construct a selection for the given row.
	 *
	 * @param row	The row index to construct a selection for.
	 * @return	The selection containing only the given row.
	 */
	public static SudokuSelection row(int row) {
		RuntimeAssert.inRange(row, 0, 9);

		Set<Integer> _indices = new HashSet<>(9);

		for (int col = 0; col < 9; col++) {
			int index = row * 9 + col;
			_indices.add(index);
		}

		return new SudokuSelection(_indices);
	}

	//TODO: make row, column, and square iterables that go through all selections of each category.

	/**Construct a selection for the given column.
	 *
	 * @param col	The column index to construct a selection for.
	 * @return	The selection containing only the given column.
	 */
	public static SudokuSelection column(int col) {
		RuntimeAssert.inRange(col, 0, 9);

		Set<Integer> _indices = new HashSet<>(9);

		for (int row = 0; row < 9; row++) {
			int index = row * 9 + col;
			_indices.add(index);
		}

		return new SudokuSelection(_indices);
	}

	/**Construct a selection for the given square index.
	 *
	 * @param sqr	The square index to construct a selection for.
	 * @return	The selection containing only the given square.
	 */
	public static SudokuSelection square(int sqr) {
		final int[] OFFSETS = {0, 1, 2, 9, 10, 11, 18, 19, 20};
		RuntimeAssert.inRange(sqr, 0, 9);

		Set<Integer> _indices = new HashSet<>(9);

		int squareX = sqr % 3;
		int squareY = sqr / 3;

		int baseIndex = squareY * 27 + squareX * 3;

		for (int offsetIndex = 0; offsetIndex < 9; offsetIndex++) {
			int index = baseIndex + OFFSETS[offsetIndex];
			_indices.add(index);
		}

		return new SudokuSelection(_indices);
	}

	/**Construct a selection from a given function of index.
	 * Indices where the given function returns true are included in the selection.
	 *
	 * @param function	Indices for which this function return true, are included in the selection.
	 * @return	A selection constructed from a function.
	 */
	public static SudokuSelection fromIndexFunction(Predicate<Integer> function) {
		Set<Integer> _indices = new HashSet<>();

		for (int i = 0; i < 81; i++) {
			if (function.test(i)) {
				_indices.add(i);
			}
		}

		return new SudokuSelection(_indices);
	}

	/**Get the selection containing all cells affected by the cell at the given index.
	 *
	 * @param index	Given index.
	 * @return	Selection equivalent to the union between the given index's row, column, and square selections.
	 */
	public static SudokuSelection affectedBy(int index) {
		int row = Sudoku.indexToRow(index);
		int col = Sudoku.indexToColumn(index);
		int sqr = Sudoku.indexToSquare(index);

		return row(row).getUnionWith(column(col)).getUnionWith(square(sqr));
	}

	/**Retrieve a 81 element array representing the selection as a mask, where true values are part of the selection.
	 *
	 * @return	The mask array (boolean[81])
	 */
	public boolean[] getAsMask() {
		boolean[] mask = new boolean[81];

		for (int i = 0; i < 81; i++) {
			mask[i] = this.contains(i);
		}

		return mask;
	}
	
	public int getRandom(Random randomizer) {
		int chosenIndex = randomizer.nextInt(indices.size());

		int counter = 0;
		for (Integer index : indices) {
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
		Set<Integer> joinedSet = new HashSet<>(this.indices);
		joinedSet.addAll(other.indices);

		return new SudokuSelection(joinedSet);
	}

	/**Get the intersection of this selection and another selection without modifying either selection.
	 *
	 * @param other	The selection to intersect with.
	 * @return	The intersection of the selections (only indices present in both are included)
	 */
	public SudokuSelection getIntersectionWith(SudokuSelection other) {
		Set<Integer> slicedSet = new HashSet<>(this.indices);
		slicedSet.retainAll(other.indices);
		//slicedSet.removeIf(index -> (!other.indices.contains(index)));//Remove all elements NOT present in other

		return new SudokuSelection(slicedSet);
	}

	/**Get the difference between this selection and another selection without modifying either selection.
	 *
	 * @param other	The set to subtract.
	 * @return	The difference of the selections (only indices present in this, but not in other are included)
	 */
	public SudokuSelection getDifferenceWith(SudokuSelection other) {
		Set<Integer> diffedSet = new HashSet<>(this.indices);
		//diffedSet.removeIf(index -> (other.indices.contains(index)));//Remove all elements present in other
		diffedSet.removeAll(other.indices);

		return new SudokuSelection(diffedSet);
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
		return indices.iterator();
	}

	@Override
	public int size() {
		return indices.size();
	}

	@Override
	public boolean isEmpty() {
		return indices.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return indices.contains(o);
	}

	@Override
	public Object[] toArray() {
		return indices.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return indices.toArray(a);
	}

	@Override
	public boolean add(Integer index) {
		return indices.add(index);
	}

	@Override
	public boolean remove(Object o) {
		return indices.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return indices.containsAll(c);
	}

	/**Works as a union operation, where the current selection is modified.*/
	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		return indices.addAll(c);
	}

	/**Works as a difference operation, where the current selection is modified.*/
	@Override
	public boolean removeAll(Collection<?> c) {
		return indices.removeAll(c);
	}

	/**Works as an intersection operation, where the current selection is modified.*/
	@Override
	public boolean retainAll(Collection<?> c) {
		return indices.retainAll(c);
	}

	@Override
	public void clear() {
		indices.clear();
	}
}
