package shared.utility;

import java.util.Collection;

/**A wrapper for common checks that should throw on failure.
 *
 */
public class RuntimeAssert {
	public static <T extends Comparable<T>> void areEqual(T first, T second) {
		if (!first.equals(second)) {
			throw new AssertionError(first + " is not equal to " + second);
		}
	}

	public static <T extends Comparable<T>> void areDifferent(T first, T second) {
		if (first.equals(second)) {
			throw new AssertionError(first + " is not different to " + second);
		}
	}

	/**Ensure a value is within the given range. Throws an AssertionError on failure.
	 *
	 * @param <T>	A value type that implements Comparable.
	 * @param value	Given value
	 * @param minimum	Lower bound of the range (inclusive)
	 * @param maximum	Upper bound of the range (exclusive)
	 * @throws AssertionError	Thrown if the assertion fails.
	 */
	public static <T extends Comparable<T>> void inRange(T value, T minimum, T maximum) throws AssertionError {
		if ((value.compareTo(minimum) < 0) || (value.compareTo(maximum) >= 0)) {
			throw new AssertionError("Value: " + value.toString() + " outside of range [" + minimum.toString() + " - " + maximum.toString() + "]");
		}
	}

	/**Ensure a value is not null. Throws an AssertionError on failure.
	 *
	 * @param <T>	Any subclass of java.lang.Object
	 * @param obj	The value that should not be null
	 * @throws AssertionError	Thrown if the assertion fails
	 */
	public static <T extends Object> void notNull(T obj) throws AssertionError {
		if (obj == null) {
			throw new AssertionError("Value was null!");
		}
	}

	/**Ensure a collection is not null and has atleast 1 item. Throws an AssertionError on failure.
	 *
	 * @param <T>	Any implementation of java.util.Collection
	 * @param collection	Collection to check
	 * @throws AssertionError	Thrown if the assertion fails
	 */
	public static <T extends Collection<?>> void hasElements(T collection) throws AssertionError {
		RuntimeAssert.notNull(collection);

		if (collection.isEmpty()) {
			throw new AssertionError("Collection has no elements!");
		}
	}
}
