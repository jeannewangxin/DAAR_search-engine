package egrep;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class EgrepClone {
	private String regEx;
	private String filename;
	private ArrayList<String> text;
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	public static final String RED_WORD = "\u001b[31m";
	public static final String RESET_WORD = "\u001b[0m";
	EgrepClone(String regEx,String filename){
		this.regEx=regEx;
		this.filename=filename;
	}
	
	public void find() throws FileNotFoundException {	
		ArrayList<Point> pos = new ArrayList<Point>();
		text = getTextFromFile();
		/*if(true){
			System.out.println("Stategie chooesed : Indexing");
			Radix in = new Radix();
			pos = in.find(regEx);
		}else */
		if(regEx.indexOf('*')>0||regEx.indexOf('|')>0) {
			System.out.println("Stategie chooesed : Automata");
			Automate r = new Automate(regEx);
			pos = r.find(text);		
		}else if(filename.contains("49345.txt")) {
			System.out.println("Stategie chooesed : Indexing");
			Radix in = new Radix();
			pos = in.find(regEx);
		}else {
			System.out.println("Stategie chooesed : KMP");
			Kmp kmp = new Kmp(regEx);		
			pos=kmp.find(text);
		}
		showPosition(pos);
	}
	
	private void showPosition(ArrayList<Point> pos) {
		if (pos.size() > 0) {
			System.out.println("Found <"+this.regEx+"> "+pos.size()+" occurrences");
			System.out.println("Found <"+this.regEx+"> in postion(s): ");
			for (Point p : pos) {
				String line = text.get((int)(p.getX()));
				System.out.print(RESET_WORD+"Line :"+((int) p.getX()+1) + ", Postion :" + ((int) p.getY()+1)+" => ");
				
				System.out.print(line.substring(0, (int) p.getY()));
				if((int) p.getY()+this.regEx.length()<line.length()) {
				System.out.print(RED_WORD+line.substring((int) p.getY(), (int) p.getY()+this.regEx.length()));
				System.out.print(RESET_WORD+line.substring((int) p.getY()+this.regEx.length()));
				}
				else
					System.out.print(RED_WORD+line.substring((int) p.getY()));
					
				System.out.println(RESET_WORD);
			}
		}else {
			System.out.println("Not Found !");

		}
	}
	

	private ArrayList<String> getTextFromFile() {
		String line;
		ArrayList<String> text = new ArrayList<String>();
		try {
			BufferedReader input;
			
				input = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF-8"));
			
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
		} catch (FileNotFoundException e) {
			System.err.println("Input file not found.");
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return text;
	}
}
