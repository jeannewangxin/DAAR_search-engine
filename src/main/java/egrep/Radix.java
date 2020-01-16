package egrep;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Radix {
	private RadixTree<ArrayList<Point>> radixTree;

	public ArrayList<Point> find(String regex) {
		ArrayList<Point> result = new  ArrayList<Point>();
		ArrayList<String> neignbours = radixTree.findNeignbours(regex,radixTree.getRoot());
		if(neignbours.size()>0) {
			for(String r : neignbours) {
			ArrayList<Point> partiel_result = radixTree.getV2((regex+r),radixTree.getRoot());
			if(partiel_result!=null)
				result.addAll(partiel_result);
			System.out.println("Word similar found =>"+regex+r);
			}
		}
		ArrayList<Point> partiel_result = radixTree.getV2(regex,radixTree.getRoot());
			if(partiel_result!=null)
				result.addAll(partiel_result);
		return result;
		
	} 
	
	public Radix(String fileName) throws FileNotFoundException {
		//creatTreeFromIndex("index.txt");
	}
	
	public Radix() {
		
	}
	
	private ArrayList<String> readFromFile(String filename) {
		String line;
		ArrayList<String> text = new ArrayList<String>();
		BufferedReader input = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
		try {
			while ((line = input.readLine()) != null) {
				text.add(line);
				
			}
		} catch (IOException e) {
			System.err.println("Exception: interrupted I/O.");
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				System.err.println("I/O exception: unable to close " + filename);
			}
		}
		return text;
	}

	// black list :1.words les than two letters 2. words too frequent 3. the this
	// that...
	private static final char[] NATURAL_LETTERS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '-', '\'' };
	private static final String[] BLACKLIST_WORDS = { "the", "has", "and", "have", "this", "from", "been", "there", "Here" };

	private Map<String, ArrayList<Point>> indexTable(ArrayList<String> text) {
		Map<String, ArrayList<Point>> result = new HashMap<String, ArrayList<Point>>();
		Map<String, ArrayList<Point>> black_list = new HashMap<String, ArrayList<Point>>();

		for (int i = 0; i < text.size(); i++) {
			char[] letters = text.get(i).toLowerCase().toCharArray();
			// System.out.println("letters :"+letters.length);
			String word = "";
			for (int j = 0; j < letters.length; j++) {
				if (new String(NATURAL_LETTERS).indexOf(letters[j]) > -1) {
					word += letters[j];
				} else {
					ArrayList<Point> positions = new ArrayList<Point>();
					if (word.length() > 2) {

						if (result.get(word) != null) {
							positions = result.get(word);
						}
						positions.add(new Point(i, j));
						result.put(word, positions);
					} else {
						if (black_list.get(word) != null) {
							positions = black_list.get(word);
						}
						positions.add(new Point(i, j));
						black_list.put(word, positions);
					}
					word = "";

				}
			}
		}
		// System.out.println(result.keySet());
		// System.out.println(black_list.keySet());
		return result;
	}
	
	public Map<String, ArrayList<Point>> getIndexTable(String filename){
		return indexTable(readFromFile(filename));
	}
	
	public Map<String, Integer> getIndexTable2(String filename){
		return indexTable2(readFromFile(filename));
	}
	
	private Map<String, Integer> indexTable2(ArrayList<String> text) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		Map<String, Integer> black_list = new HashMap<String, Integer>();

		for (int i = 0; i < text.size(); i++) {
			char[] letters = text.get(i).toLowerCase().toCharArray();
			// System.out.println("letters :"+letters.length);
			String word = "";
			for (int j = 0; j < letters.length; j++) {
				if (new String(NATURAL_LETTERS).indexOf(letters[j]) > -1) {
					word += letters[j];
					if(j==letters.length-1) {
						int occurence = 0;
						if (word.length() > 2) {
							if (result.containsKey(word)) {
								occurence = result.get(word);
							}
							occurence++;
							result.put(word, occurence);
						} else {
							if (black_list.containsKey(word)) {
								occurence = black_list.get(word);
							}
							occurence++;
							black_list.put(word, occurence);
						}
						word = "";
					}
				} else {
					int occurence = 0;
					if (word.length() > 2) {
						if (result.containsKey(word)) {
							occurence = result.get(word);
						}
						occurence++;
						result.put(word, occurence);
					} else {
						if (black_list.containsKey(word)) {
							occurence = black_list.get(word);
						}
						occurence++;
						black_list.put(word, occurence);
					}
					word = "";

				}
			}
		}
		// System.out.println(result.keySet());
		// System.out.println(black_list.keySet());
		return result;
	}

	public Map<String, ArrayList<Point>> sortHashMapByValues(Map<String, ArrayList<Point>> hm) {

		// Create a list from elements of HashMap
		List<Map.Entry<String, ArrayList<Point>>> list = new LinkedList<Map.Entry<String, ArrayList<Point>>>(
				hm.entrySet());
		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, ArrayList<Point>>>() {
			public int compare(Map.Entry<String, ArrayList<Point>> o1, Map.Entry<String, ArrayList<Point>> o2) {
				return (((ArrayList<Point>) o1.getValue()).size() - ((ArrayList<Point>) o2.getValue()).size());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, ArrayList<Point>> temp = new LinkedHashMap<String, ArrayList<Point>>();
		for (Map.Entry<String, ArrayList<Point>> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}


	private void printToFile(String filename, Map<String, ArrayList<Point>> indexMap) {
		try {
			PrintStream output = new PrintStream(new FileOutputStream(filename));
			Set set2 = indexMap.entrySet();
			Iterator iterator2 = set2.iterator();
			while (iterator2.hasNext()) {
				Map.Entry mentry2 = (Map.Entry) iterator2.next();
				//System.out.println("Key is: " + mentry2.getKey() + " & Value is: " + mentry2.getValue());
				String postion = "";
				for (Point p : ((ArrayList<Point>) mentry2.getValue())) {
					postion += ":" + (int) p.getX() + "," + (int) p.getY();
				}
				output.println(mentry2.getKey() + postion);

			}
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("I/O exception: unable to create " + filename);
		}
	}

	private void creatTreeFromText() {
		radixTree = new RadixTree<>();
		Map<String, ArrayList<Point>> indexMap = sortHashMapByValues(indexTable(readFromFile("./src/text1.txt")));
		Iterator iterator2 = indexMap.entrySet().iterator();
		//printToFile("./src/index.txt", indexMap);
		while (iterator2.hasNext()) {
			Map.Entry mentry2 = (Map.Entry) iterator2.next();
			radixTree.set(mentry2.getKey().toString(), (ArrayList<Point>)mentry2.getValue());

		}
	}
	
	private void creatTreeFromIndex(String filename) throws FileNotFoundException {
		radixTree = new RadixTree<>();
		Map<String, ArrayList<Point>> indexMap = new HashMap<String, ArrayList<Point>>() ;	
		String line;
		InputStream in = this.getClass().getResourceAsStream(filename);
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		//BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		try {
			while ((line = input.readLine()) != null) {	
				if(line.length()<=0)continue;
				String[] separated = line.split(":");
				if(separated.length<2)continue;
				String key = separated[0];
				ArrayList<Point> pos= new ArrayList<Point>();
				for(int i=1; i<separated.length;i++) {
					if(separated[i].length()<=0)continue;
					//System.err.println("positions : " +separated[i]);
					String[] postion = separated[i].split(",");
					//if(postion.length!=2)continue;
					Point p = new Point(Integer.parseInt(postion[0]),Integer.parseInt(postion[1]));
					pos.add(p);
				}
		
				indexMap.put(key,pos);
			}
		} catch (IOException e) {
			System.err.println("Exception: interrupted I/O.");
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				System.err.println("I/O exception: unable to close " + filename);
			}
		}
		
		Iterator iterator2 = indexMap.entrySet().iterator();
		//printToFile("./src/index.txt", indexMap);
		while (iterator2.hasNext()) {
			Map.Entry mentry2 = (Map.Entry) iterator2.next();
			radixTree.set(mentry2.getKey().toString(), (ArrayList<Point>)mentry2.getValue());

		}
	}
	
	

}
