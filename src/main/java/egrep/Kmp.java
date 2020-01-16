package egrep;

import java.awt.Point;
import java.util.ArrayList;

public class Kmp {
	private char[] regEx;
	private int[] memoTable;
	
	// CONSTRUCTOR
	public Kmp(String regex) {
		this.regEx = regex.toCharArray();
		getMemoTable(this.regEx);
	}
	
	public void getMemoTable(char[] factor) {
		memoTable = new int[(factor.length + 1)];
		memoTable[0] = -1;
		memoTable[factor.length] = 0;
		for (int i = 0; i < factor.length; i++) {
			if (factor[i] == factor[0]) {
				memoTable[i] = -1;
				continue;
			}
			int weight = 0;
			int suffixLength = 1;
			for (int j = (i - suffixLength); j > 0; j--) {
				boolean isValid = true;
				for (int t = 0; t < suffixLength; t++) {
					if (factor[t] != factor[j + t]) {
						isValid = false;
						break;
					}
					if (factor[suffixLength] == factor[j + suffixLength]) {
						isValid = false;
					}
				}
				if (isValid) {
					weight = Math.max(weight, suffixLength);
				}
				;
				suffixLength++;
			}
			memoTable[i] = weight;
		}
		return ;
	}

	public void show(int[] MemoTable) {
		for (int i : MemoTable) {
			System.out.print(i + ",");
		}
	}

	public ArrayList<Integer> matchString(String s) {
		char[] text = s.toLowerCase().toCharArray();
		ArrayList<Integer> result = new ArrayList<Integer>();
		int i = 0;
		int j = 0;
		while (i < text.length) {	
			//System.out.println(" found regex in postion: " +text.length + "."+regEx.length + "."+regEx[j]+ "."+text[i]);
			if (text[i] == regEx[j]) {
				i++;
				j++;
				if (j == regEx.length) {
					result.add(i - regEx.length);
					//return (i - regEx.length);
					j=0;
				}
			} else {
				if (memoTable[j] == -1) {
					i++;
					j = 0;
				} else {
					j = memoTable[j];
				}
			}
		}
		return result;
	}
	
	public ArrayList<Point> find (ArrayList<String> text) {
		ArrayList<Point> result = new ArrayList<Point>();
			for(int i=0;i<text.size();i++) {
				ArrayList<Integer> po = new ArrayList<Integer>();
				po = matchString(text.get(i));
				for(int y : po) {
					Point p =new Point(i,y);
					result.add(p);
				}
			}
		return result;
	}
	
}
