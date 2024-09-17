package shared.generation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import shared.model.Sudoku;
import shared.model.SudokuSelection;

public class WaveCollapseGenerator implements ISudokuGenerator {
	private final Random rng;
	private QuantumNode[] nodes = new QuantumNode[81];

	public WaveCollapseGenerator() {
		rng = new Random(System.currentTimeMillis());

		//Create the nodes
		for (int i = 0; i < 81; i++) {
			nodes[i] = new QuantumNode();
		}

		//Entangle the nodes
		for (int i = 0; i < 81; i++) {
			for (Integer index : SudokuSelection.affectedBy(i)) {
				nodes[i].entangle(nodes[index]);
			}
		}
	}

	@Override
	public void generate(Sudoku target) {
		boolean success = false;
		int limit = 50;
		int attempt = 0;

		//Keep trying until there is success, or the limit is reached
		while (!success && (attempt++ < limit)) {
			System.out.print("Wave Function Collapse generator: attempt #" + attempt + "...");
			try {
				//Clear any leftovers from previous run
				for (int i = 0; i < 81; i++) {
					nodes[i].resetState();
				}

				//Generate
				QuantumNode curNode = pickNext();
				while (curNode != null) {
					//Collapse to a random legal value
					List<Integer> options = curNode.getAvailableOptions();
					int chosen = options.get(rng.nextInt(options.size()));

					curNode.collapse(chosen);
					curNode = pickNext();
				}

				//Verify it's a legal board
				Sudoku verifier = new Sudoku();
				assignFromNodes(verifier);

				if (verifier.isLegalBoardState()) {
					System.out.println("\tSUCCEEDED");
					success = true;
				}
				else {
					//Illegal board, throw to reach the failure logic.
					throw new IllegalStateException("Sudoku failed validation! Invalid board state!");
				}
			}
			catch (IllegalStateException e) {
				System.out.println("\tFAILED");
				System.out.println(e.getMessage());
				System.out.println();
			}
		}

		assignFromNodes(target);
	}

	/**Assign the current node state to a target sudoku.
	 *
	 * @param target	Target sudoku that will recieve the current node values.
	 */
	private void assignFromNodes(Sudoku target) {
		for (int i = 0; i < 81; i++) {
			target.set(i, nodes[i].getValue());
		}
	}

	/**Get the next node to collapse. Nodes with fewer options left are given priority.
	 *
	 * @return	The next node to collapse.
	 */
	private QuantumNode pickNext() {
		List<Integer> bestCandidates = new ArrayList<>();
		int leastOptions = 10;//9 options, should never find anything bigger

		for (int i = 0; i < 81; i++) {
			if (nodes[i].hasValue()) {
				continue;
			}

			int optionCount = nodes[i].optionCount();
			//New lowest?
			if (optionCount < leastOptions) {
				//Clear old lowest indices, and set own option count as lowest.
				bestCandidates.clear();
				bestCandidates.add(i);
				leastOptions = optionCount;
			}
			else if (optionCount == leastOptions) {
				//Same as current lowest options count, add current to the list.
				bestCandidates.add(i);
			}
		}

		//No candidates?
		if (bestCandidates.size() == 0) {
			return null;
		}
		else {
			//Pick a random candidate
			int indexIndex = rng.nextInt(bestCandidates.size());
			return nodes[bestCandidates.get(indexIndex)];
		}
	}

	private class QuantumNode {
		private Set<QuantumNode> entangledNodes;
		private boolean[] options = new boolean[9];
		private Integer value = null;

		public QuantumNode() {
			entangledNodes = new HashSet<>();
			resetState();
		}

		public boolean hasValue() { return value != null; }
		public int getValue() { return hasValue() ? value : -1; }

		/**Collapse this node to a given value. Will also remove the given value as an option from entangled nodes,
		 * and potentially collapse them as well if they only have 1 option left.
		 *
		 * @param _value	The value to collapse to.
		 * @throws IllegalStateException	Thrown if the given value is null.
		 */
		public void collapse(Integer _value) throws IllegalStateException {
			if (hasValue()) {
				//Already collapsed, do nothing
				return;
			}

			if (_value == null) {
				throw new IllegalStateException("QuantumNode was asked to collapse to a null value!");
			}

			//Assign the value and remove it as an option from all entangled nodes
			value = _value;
			for (QuantumNode node : entangledNodes) {
				node.removeOption(value);
			}
		}

		public void collapse() throws IllegalStateException {
			collapse(getAvailableOptions().get(0));
		}

		/**Remove an option from this node.
		 *
		 * @param option
		 * @return
		 * @throws IllegalStateException
		 */
		public void removeOption(int option) throws IllegalStateException {
			if (hasValue()) {
				//Already collapsed, do nothing
				return;
			}

			//Only remove the option if it's available
			if (options[option - 1]) {
				options[option - 1] = false;

				//Collapse if it's reduced to only 1 option
				if (optionCount() == 1) {
					collapse();
				}
				//If it somehow reached 0 options, throw.
				else if (optionCount() == 0) {
					throw new IllegalStateException("QuantumNode left with no legal options!");
				}
			}
		}

		public void entangle(QuantumNode node) {
			if (node == this) {
				return;
			}
			else entangledNodes.add(node);
		}

		/**Reset options to everything being available, and the value to be undetermined.*/
		public void resetState() {
			value = null;

			for (int i = 0; i < 9; i++) {
				options[i] = true;
			}
		}

		/**Get the amount of remaining options*/
		public int optionCount() {
			return getAvailableOptions().size();
		}

		/**Get all remaining options for this node.
		 *
		 * @return	List of remaining legal values to collapse to.
		 */
		public List<Integer> getAvailableOptions() {
			List<Integer> result = new ArrayList<>(9);

			for (int i = 0; i < 9; i++) {
				if (options[i]) {
					result.add(i + 1);
				}
			}

			return result;
		}
	}
}
