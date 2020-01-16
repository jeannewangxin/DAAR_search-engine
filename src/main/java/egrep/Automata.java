package egrep;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class Automata {
	private static int STATE_LENGTH = 258;
	private RegExTree regExTree;
	private AtomicReference<ArrayList<int[]>> states;
	private Map<Integer, int[]> states_min;
	private int[] state_rempalce_table;
	private AtomicReference<Map<Integer, ArrayList<Integer>>> Etable;

	public Automata(RegExTree regExTree) {
		this.regExTree = regExTree;
		toAutomata(this.regExTree);
	}

	public ArrayList<Point> matchStringArr(String[] texts) {
		for (int i = 0; i < texts.length; i++) {

		}
		return null;
	}

	public ArrayList<Integer> matchString(String s) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		int no_current_state = -1;
		int start_state = -1;
		if (states_min.size() > 0) {
			// find start state
			for (Map.Entry<Integer, int[]> entry : states_min.entrySet()) {
				int[] convert_to = (int[]) entry.getValue();
				if (convert_to[256] == 0) {
					start_state = entry.getKey();
				}
			}
			no_current_state = start_state;
			// start match
			char[] s_char = s.toLowerCase().toCharArray();
			int current_no_letter_match=0;
			for (int k = 0; k < s_char.length; k++) {
				int counter = 0;
				int[] current_state = states_min.get(no_current_state);
				if ((int) s_char[k] > 0 && (int) s_char[k] < 255 && current_state[(int) s_char[k]] > -1) {
					no_current_state = current_state[(int) s_char[k]];
					counter++;
					current_no_letter_match++;
					if (states_min.get(no_current_state)[257] == 0) {
						// found !
						//System.out.println("counter : "+counter);
						positions.add(k-current_no_letter_match+1);
						counter = 0;
						current_no_letter_match=0;
						no_current_state = start_state;
					}
				} else {
					// Can't go ahead !
					current_no_letter_match=0;
					no_current_state = start_state;
					k -= (counter);
				}
			}

		}
		return positions;

	}

	private void toAutomata(RegExTree ret) {
		states = new AtomicReference<ArrayList<int[]>>(new ArrayList<int[]>());
		Etable = new AtomicReference<Map<Integer, ArrayList<Integer>>>(new HashMap<Integer, ArrayList<Integer>>());
		findleaves(ret, states, Etable);
		initReplaceTable();
		//printStates();
		//printEtable();
		minimize();
		//printStatesMin();
		return;
	}

	private void combineStates(int key, ArrayList<Integer> sous_states) {
		int[] originState = states.get().get(key);
		int[] sous_state;
		for (int j = 0; j < sous_states.size(); j++) {
			int sous_s = sous_states.get(j);
			sous_state = states.get().get(sous_s);
			for (int i = 0; i < sous_state.length; i++) {
				if (sous_state[i] > -1) {
					originState[i] = sous_state[i];
				}
			}
			ArrayList<Integer> sous_states_found = getEqualStates(sous_s);
			for (int s : sous_states_found) {
				sous_state = states.get().get(s);
				for (int i = 0; i < sous_state.length; i++) {
					if (sous_state[i] > -1) {
						originState[i] = sous_state[i];
					}
				}
			}
			state_rempalce_table[sous_s] = state_rempalce_table[key];
		}

	}

	private ArrayList<Integer> getEqualStates(int state_no) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (Map.Entry<Integer, ArrayList<Integer>> entry : Etable.get().entrySet()) {
			ArrayList<Integer> convert_to = (ArrayList<Integer>) entry.getValue();
			if (state_no == entry.getKey()) {
				for (int sous_state : convert_to) {
					result.addAll(getEqualStates(sous_state));
					result.addAll(convert_to);
				}
			}
		}
		return result;
	}

	private void initReplaceTable() {
		state_rempalce_table = new int[states.get().size()];
		for (int i = 0; i < states.get().size(); i++) {
			state_rempalce_table[i] = i;
		}
	}

	private void minimize() {
		states_min = new HashMap<Integer, int[]>();
		for (Map.Entry<Integer, ArrayList<Integer>> entry : Etable.get().entrySet()) {
			ArrayList<Integer> convert_to = (ArrayList<Integer>) entry.getValue();
			combineStates(entry.getKey(), convert_to);
		}
		int j = 0;
		for (int[] state : states.get()) {
			if (state_rempalce_table[j] == j) {
				states_min.put(j, state.clone());
			}
			j++;
		}
	}

	private static ArrayList<Integer> findleaves(RegExTree ret, AtomicReference<ArrayList<int[]>> states,
			AtomicReference<Map<Integer, ArrayList<Integer>>> Etable) {
		ArrayList<int[]> refstates = states.get();
		Map<Integer, ArrayList<Integer>> etable = Etable.get();
		int left_old_start = -1;
		int left_old_end = -1;
		int right_old_start = -1;
		int right_old_end = -1;
		if (ret.subTrees.size() == 2) {
			ArrayList<Integer> leftChildStates = findleaves(ret.subTrees.get(0), states, Etable);
			ArrayList<Integer> rightChildStates = findleaves(ret.subTrees.get(1), states, Etable);
			switch (ret.root) {
			case Automate.ALTERN:
				//System.out.println("root : |");
				// add new start and end
				int[] state1 = new int[STATE_LENGTH];
				int[] state2 = new int[STATE_LENGTH];
				Arrays.fill(state1, -1);
				Arrays.fill(state2, -1);
				refstates.add(state1);
				refstates.add(state2);
				int new_start = refstates.size() - 2;
				int new_end = refstates.size() - 1;
				refstates.get(new_start)[256] = 0;
				refstates.get(new_end)[257] = 0;
				states.set(refstates);
				// cancel old start and old end
				for (int c : leftChildStates) {
					if (refstates.get(c)[257] == 0) {
						left_old_end = c;
						refstates.get(c)[257] = -1;
					}
					if (refstates.get(c)[256] == 0) {
						left_old_start = c;
						refstates.get(c)[256] = -1;
					}
				}
				for (int c : rightChildStates) {
					if (refstates.get(c)[256] == 0) {
						right_old_start = c;
						refstates.get(c)[256] = -1;
					}
					if (refstates.get(c)[257] == 0) {
						right_old_end = c;
						refstates.get(c)[257] = -1;
					}
				}

				// update e table
				ArrayList<Integer> newEAltern1 = new ArrayList<Integer>();
				ArrayList<Integer> newEAltern2 = new ArrayList<Integer>();
				ArrayList<Integer> newEAltern3 = new ArrayList<Integer>();
				newEAltern1.add(left_old_start);
				newEAltern1.add(right_old_start);
				etable.put(new_start, newEAltern1);
				newEAltern2.add(new_end);
				etable.put(left_old_end, newEAltern2);
				newEAltern3.add(new_end);
				etable.put(right_old_end, newEAltern3);
				
				ArrayList<Integer> childstates = new ArrayList<Integer>();
				childstates.addAll(leftChildStates);
				childstates.addAll(rightChildStates);
				childstates.add(new_start);
				childstates.add(new_end);
				return childstates;
				//break;
			case Automate.CONCAT:
				//System.out.println("root : .");
				// update e table
				ArrayList<Integer> newERecords = new ArrayList<Integer>();
				for (int c : leftChildStates) {
					if (refstates.get(c)[257] == 0) {
						left_old_end = c;
						refstates.get(c)[257] = -1;
					}
				}
				for (int c : rightChildStates) {
					if (refstates.get(c)[256] == 0) {
						newERecords.add(c);
						refstates.get(c)[256] = -1;
					}
				}
				etable.put(left_old_end, newERecords);
				break;
			default:
				break;
			}
			ArrayList<Integer> childstates = new ArrayList<Integer>();
			childstates.addAll(leftChildStates);
			childstates.addAll(rightChildStates);
			return childstates;
		} else if (ret.subTrees.size() == 1) {
			ArrayList<Integer> childStates = findleaves(ret.subTrees.get(0), states, Etable);
			switch (ret.root) {
			case Automate.ETOILE:
				//System.out.println("root : *");
				// update states table
				int[] state1 = new int[STATE_LENGTH];
				int[] state2 = new int[STATE_LENGTH];
				Arrays.fill(state1, -1);
				Arrays.fill(state2, -1);
				refstates.add(state1);
				refstates.add(state2);
				int new_start = refstates.size() - 2;
				int new_end = refstates.size() - 1;
				refstates.get(new_start)[256] = 0;
				refstates.get(new_end)[257] = 0;
				states.set(refstates);
				for (int c : childStates) {
					if (refstates.get(c)[256] == 0) {
						left_old_start = c;
						refstates.get(c)[256] = -1;
					}
					if (refstates.get(c)[257] == 0) {
						left_old_end = c;
						refstates.get(c)[257] = -1;
					}
				}
				// update Etable
				ArrayList<Integer> newERecords1 = new ArrayList<Integer>();
				ArrayList<Integer> newERecords2 = new ArrayList<Integer>();
				newERecords1.add(left_old_start);
				newERecords1.add(new_end);
				etable.put(new_start, newERecords1);

				newERecords2.add(left_old_start);
				newERecords2.add(new_end);
				etable.put(left_old_end, newERecords2);

				childStates.add(new_start);
				childStates.add(new_end);
				break;
			default:
				break;
			}
			return childStates;
		}

		ArrayList<Integer> newstates = new ArrayList<Integer>();
		if ((65 <= ret.root && ret.root <= 90) || (97 <= ret.root && ret.root <= 122)) {
			//System.out.println("root : " + ret.root);
			int[] state1 = new int[STATE_LENGTH];
			int[] state2 = new int[STATE_LENGTH];
			Arrays.fill(state1, -1);
			Arrays.fill(state2, -1);
			refstates.add(state1);
			refstates.add(state2);
			newstates.add(refstates.size() - 2);
			newstates.add(refstates.size() - 1);
			refstates.get((refstates.size() - 2))[ret.root] = (refstates.size() - 1);
			refstates.get((refstates.size() - 2))[256] = 0;
			refstates.get((refstates.size() - 1))[257] = 0;
			states.set(refstates);
		}
		return newstates;

	}

	private void printStates() {
		System.out.println("------------------------states--------------------------------");
		int j = 0;
		for (int[] state : states.get()) {
			System.out.print("State " + j + " => ");
			for (int i = 0; i < state.length; i++) {
				if (state[i] > -1) {
					System.out.print(i + " : " + state[i] + "|");
				}

			}
			System.out.println();
			j++;
		}
	}

	private void printStatesMin() {
		System.out.println("------------------------states min--------------------------------");
		for (Map.Entry<Integer, int[]> entry : states_min.entrySet()) {
			System.out.print(entry.getKey() + ":");
			int[] convert_to = (int[]) entry.getValue();
			for (int s : convert_to) {
				if (s > -1) {
					System.out.print(entry.getKey() + "=>" + s + "|");
				}
			}
			System.out.println("");
		}
	}

	private void printEtable() {
		System.out.println("--------------------------e table-------------------------------");
		for (Map.Entry<Integer, ArrayList<Integer>> entry : Etable.get().entrySet()) {
			System.out.print(entry.getKey() + "/");
			ArrayList<Integer> convert_to = (ArrayList<Integer>) entry.getValue();
			for (int s : convert_to) {
				System.out.print(s + ",");
			}
			System.out.println("");
		}
	}

}
