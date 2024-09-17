package shared.utility;

public class ArrayExtras {
	/**Get the number of times desiredObject appears within a given range of an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param startIndex	Start of the checked range (inclusive)
	 * @param stopIndex		End of the checked range (exclusive)
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequencyRange(boolean[] array, int startIndex, int stopIndex, boolean desiredValue) {
		int result = 0;

		for (int i = startIndex; i < stopIndex; i++) {
			if (array[i] == desiredValue) {
				result++;
			}
		}

		return result;
	}

	/**Get the number of times desiredObject appears within an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequency(boolean[] array, boolean desiredValue) {
		return frequencyRange(array, 0, array.length, desiredValue);
	}

	/**Get the number of times desiredObject appears within a given range of an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param startIndex	Start of the checked range (inclusive)
	 * @param stopIndex		End of the checked range (exclusive)
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequencyRange(long[] array, int startIndex, int stopIndex, long desiredValue) {
		int result = 0;

		for (int i = startIndex; i < stopIndex; i++) {
			if (array[i] == desiredValue) {
				result++;
			}
		}

		return result;
	}

	/**Get the number of times desiredObject appears within an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequency(long[] array, long desiredValue) {
		return frequencyRange(array, 0, array.length, desiredValue);
	}

	/**Get the number of times desiredObject appears within a given range of an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param startIndex	Start of the checked range (inclusive)
	 * @param stopIndex		End of the checked range (exclusive)
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequencyRange(int[] array, int startIndex, int stopIndex, int desiredValue) {
		int result = 0;

		for (int i = startIndex; i < stopIndex; i++) {
			if (array[i] == desiredValue) {
				result++;
			}
		}

		return result;
	}

	/**Get the number of times desiredObject appears within an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequency(int[] array, int desiredValue) {
		return frequencyRange(array, 0, array.length, desiredValue);
	}

	/**Get the number of times desiredObject appears within a given range of an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param startIndex	Start of the checked range (inclusive)
	 * @param stopIndex		End of the checked range (exclusive)
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequencyRange(short[] array, int startIndex, int stopIndex, short desiredValue) {
		int result = 0;

		for (int i = startIndex; i < stopIndex; i++) {
			if (array[i] == desiredValue) {
				result++;
			}
		}

		return result;
	}

	/**Get the number of times desiredObject appears within an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequency(short[] array, short desiredValue) {
		return frequencyRange(array, 0, array.length, desiredValue);
	}

	/**Get the number of times desiredObject appears within a given range of an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param startIndex	Start of the checked range (inclusive)
	 * @param stopIndex		End of the checked range (exclusive)
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequencyRange(byte[] array, int startIndex, int stopIndex, byte desiredValue) {
		int result = 0;

		for (int i = startIndex; i < stopIndex; i++) {
			if (array[i] == desiredValue) {
				result++;
			}
		}

		return result;
	}

	/**Get the number of times desiredObject appears within an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequency(byte[] array, byte desiredValue) {
		return frequencyRange(array, 0, array.length, desiredValue);
	}

	/**Get the number of times desiredObject appears within a given range of an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param startIndex	Start of the checked range (inclusive)
	 * @param stopIndex		End of the checked range (exclusive)
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequencyRange(double[] array, int startIndex, int stopIndex, double desiredValue) {
		int result = 0;

		for (int i = startIndex; i < stopIndex; i++) {
			if (array[i] == desiredValue) {
				result++;
			}
		}

		return result;
	}

	/**Get the number of times desiredObject appears within an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequency(double[] array, double desiredValue) {
		return frequencyRange(array, 0, array.length, desiredValue);
	}

	/**Get the number of times desiredObject appears within a given range of an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param startIndex	Start of the checked range (inclusive)
	 * @param stopIndex		End of the checked range (exclusive)
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequencyRange(float[] array, int startIndex, int stopIndex, float desiredValue) {
		int result = 0;

		for (int i = startIndex; i < stopIndex; i++) {
			if (array[i] == desiredValue) {
				result++;
			}
		}

		return result;
	}

	/**Get the number of times desiredObject appears within an array.
	 *
	 * @param array		The array to count appearances in.
	 * @param desiredValue	The value to search for.
	 * @return
	 */
	public static int frequency(float[] array, float desiredValue) {
		return frequencyRange(array, 0, array.length, desiredValue);
	}

	/**Get the number of times desiredObject appears within a given range of an array.
	 *
	 * @param <T>	Any Object subclass.
	 * @param array	The array to count appearances in.
	 * @param startIndex	Start of the checked range (inclusive)
	 * @param stopIndex		End of the checked range (exclusive)
	 * @param desiredObject	The object to search for.
	 * @return
	 */
	public static <T extends Object> int frequencyRange(T[] array, int startIndex, int stopIndex, T desiredObject) {
		int result = 0;

		for (int i = startIndex; i < stopIndex; i++) {
			if (array[i].equals(desiredObject)) {
				result++;
			}
		}

		return result;
	}

	/**Get the number of times desiredObject appears within an array.
	 *
	 * @param <T>	Any Object subclass.
	 * @param array	The array to count appearances in.
	 * @param desiredObject	The object to search for.
	 * @return
	 */
	public static <T extends Object> int frequency(T[] array, T desiredObject) {
		return frequencyRange(array, 0, array.length, desiredObject);
	}
}
