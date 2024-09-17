package shared.model;

import java.util.Arrays;

import shared.utility.RuntimeAssert;

/**Compact representation of a sudoku, where each digit is represented as a 4-bit nibble
 *
 */
public class CompactSudoku {
	byte[] contents;

	public CompactSudoku(Sudoku sudoku) {
		contents = new byte[41];
		Arrays.fill(contents, (byte)0);

		for (int i = 0; i < 81; i++) {
			set(i, sudoku.get(i));
		}
	}

	private void set(int index, int value) {
		RuntimeAssert.inRange(index, 0, 81);

		byte targetByte = getSourceByte(index);

		if (isUpperNibble(index)) {
			contents[targetByte] = (byte)((contents[targetByte] & 0x0f) | ((value & 0x0f) << 4));
		}
		else {
			contents[targetByte] = (byte)((contents[targetByte] & 0xf0) | (value & 0x0f));
		}
	}

	public int get(int index) {
		RuntimeAssert.inRange(index, 0, 81);

		byte sourceByte = getSourceByte(index);

		if (isUpperNibble(index)) {
			return (contents[sourceByte] & 0xf0) >> 4;
		}
		else {
			return (contents[sourceByte] & 0x0f);
		}
	}

	private byte getSourceByte(int index) {
		return (byte)(index / 2);
	}

	private boolean isUpperNibble(int index) {
		return index % 2 == 0;
	}
}
