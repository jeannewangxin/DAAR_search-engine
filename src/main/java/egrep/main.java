package egrep;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import graphe.Tools;

public class main {

    
    public static void main(String arg[]) throws FileNotFoundException {
    	
		String book1File = "./src/test1.txt";
    	String book2File = "./src/test2.txt";   	
        double d = Tools.jaccardDistance(book1File,book2File);
        System.out.print(book1File+book2File+"--->distance = "+d);
    }
	
			

	// MAIN
	/*public static void main(String arg[]) {
		String regEx = arg[0];
		String filename=arg[1];
		/*System.out.println("Welcome to egrep clone!");	
		System.out.println(">> Please enter a regEx: ");
		Scanner scanner = new Scanner(System.in);
		regEx = scanner.next();
		System.out.println("  >> ...");
		System.out.print("  >> Please enter source file name with absolute path: ");
		filename = scanner.next();
		*/
	/*
		if (filename.length() < 1) {
			System.err.println("  >> ERROR: empty filename.");
		} 
		if (regEx.length() < 1) {
			System.err.println("  >> ERROR: empty regEx.");
		} else {
			try {
				System.out.println("Looking for "+regEx);
				EgrepClone egrepC =  new EgrepClone(regEx.toLowerCase(),filename);
				egrepC.find();
			} catch (Exception e) {
				System.err.println("  >> ERROR: syntax error for regEx \"" + regEx + "\"." + e.getMessage());
			}
		}

		System.out.println("  >> ...");
		System.out.println("  >> Search completed.");
		System.out.println("Goodbye .");
		/*
		testKmp();
		testAutomata();
		testRedix();
		*/
/*	}
*/
 
	public static void showPosition(ArrayList<Integer> pos) {
		if (pos.size() > 0) {
			for (int i : pos) {
				System.out.println(" found regex in postion: " + i+1 + ".");
			}
		}else {
			System.out.println("Not found !");
		}
	}
	public static void testKmp() {
		System.out.println("------------------------Kmp test-------------------------------------");
		String regex = "why";
		Kmp kmp = new Kmp(regex);
		System.out.println("Looking for "+regex);
		ArrayList<Integer> pos=kmp.matchString("Ur.");
		showPosition(pos);
	}

	public static void testAutomata() {
		System.out.println("------------------------Automata test--------------------------------");
		String regex = "a|bc*";
		Automate r = new Automate(regex);
		System.out.println("Looking for "+regex);
		ArrayList<Integer> pos = r.matchString("labcbddddddddzab");
		showPosition(pos);
	}

	public static void testRedix() throws FileNotFoundException {
		System.out.println("------------------------Radix test-----------------------------------");
		Radix in;
		try {
			in = new Radix();
			String regex = "conclusions";
			ArrayList<Point> po = in.find(regex);
			System.out.println("Looking for "+regex);
			if(po!=null && po.size()>0) {
			for (Point p : po) {
				System.out.println((int) p.getX() + " " + (int) p.getY());
			}
			}else {
				System.out.println("Not found !");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
