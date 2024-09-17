package shared.utility;

public class Memo<KEY_T, VAL_T> {
	private final KEY_T key;
	private final VAL_T value;

	public Memo(KEY_T _key, VAL_T _value) {
		key = _key;
		value = _value;
	}

	/**See if a memo has a value for a given key. Also checks if the memo is null
	 *
	 * @param <KT>	Key type
	 * @param <VT>	Value type
	 * @param memo	The memo to check for the given key
	 * @param requestedKey	The key to look for
	 * @return	true if there is a value for this key. false otherwise, or the given memo is null
	 */
	public static <KT, VT> boolean safeHasValueFor(Memo<KT, VT> memo, KT requestedKey) {
		return ((memo != null) && (memo.hasValueFor(requestedKey)));
	}

	/**See if a memo has a value for a given key.
	 *
	 * @param requestedKey	The key to look for.
	 * @return	true if there is a value for this key. false otherwise
	 */
	public boolean hasValueFor(KEY_T requestedKey) {
		return key.equals(requestedKey);
	}

	public VAL_T getValue() {
		return value;
	}
}
