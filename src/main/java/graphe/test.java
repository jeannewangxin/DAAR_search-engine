package graphe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class test {

	public static void main(String[] args) {
		// testDisJaccard();

		ArrayList<String> files = new ArrayList<String>();
		final File folder = new File("src/main/resources/static/books/");
		for (final File fileEntry : folder.listFiles()) {
			files.add("/static/books/"+fileEntry.getName());
		}

		double[][] graphe = testGrapheJaccard(files);
		testCloseness(graphe);
		testBetweeness(graphe);
		testPageRank(graphe);
	}

	public static void testDisJaccard() {
		double dis = Tools.jaccardDistance2("/static/books/61.txt.utf-8", "/static/books/21-0.txt");
		System.out.println("distance jacard is : " + dis);
	}

	public static double[][] testGrapheJaccard(ArrayList<String> files) {
		double[][] graphe = Tools.grapheJaccard(files);
		System.out.println("----------------------Graphe Jaccard---------------------------------");
		printTab2(graphe);
		return graphe;
			
	}

	public static void testCloseness(double[][] GrapheJaccard) {
		double[] closenessTab = Tools.closeness(Tools.floydWarshall1(GrapheJaccard));
		System.out.println("----------------------Closeness table--------------------------------");
		printTab1(closenessTab);

	}

	public static void testBetweeness(double[][] GrapheJaccard) {
		/*int[][] Path = Tools.calculShortestPaths(Tools.grapheJaccard(files));
		System.out.println("----------------------Betweeness table-------------------------------");
		System.out.println("----------------------step 1-path----------------------------------------");
		printTab21(Path);
		System.out.println("----------------------step 1 version 2-----------------------------------------");
		printPaths(Tools.floydWarshall2(Tools.grapheJaccard(files)));
		*/
		double[] betwennesse = Tools.betwennesse_v2(Tools.floydWarshall2(GrapheJaccard));
		System.out.println("----------------------Betweeness version 2-----------------------------------------");
		printTab1(betwennesse);

		/*
		System.out.println("----------------------step 2 (to do)---------------------------------");
		double[] bet = Tools.betwennesse(Tools.grapheJaccard(files));
		printTab1(bet);
		double[] betN = Tools.betwennesseNormaliser(bet);
		System.out.println("----------------------Normaliser--------------------------------");
		printTab1(betN);
		// to do : Use DFS on Path table, then get list of shortest path for every pair
		// of nodes
		 */
	}

	public static void testPageRank(double[][] GrapheJaccard) {
		double[] pageRankTab = Tools.pageRank(Tools.floydWarshall1(GrapheJaccard));
		System.out.println("----------------------PageRank table --------------------------------");
		printTab1(pageRankTab);

	}

	public static double[][] testJacDis() {
		double[][] testJaDis = new double[4][4];
		testJaDis[0][0] = 0;
		testJaDis[1][1] = 0;
		testJaDis[2][2] = 0;
		testJaDis[3][3] = 0;

		testJaDis[0][1] = 0.5;
		testJaDis[0][2] = 0.5;
		testJaDis[0][3] = 1;

		testJaDis[1][0] = 0.5;
		testJaDis[1][2] = 1;
		testJaDis[1][3] = 0.5;

		testJaDis[2][1] = 1;
		testJaDis[2][3] = 0.5;
		testJaDis[2][0] = 0.5;

		testJaDis[3][0] = 1;
		testJaDis[3][1] = 0.5;
		testJaDis[3][2] = 0.5;

		return testJaDis;
	}

	public static void printTab2(double[][] graphe) {
		for (int i = 0; i < graphe.length; i++) {
			for (int j = 0; j < graphe.length; j++) {
				System.out.print(graphe[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void printTab21(int[][] graphe) {
		for (int i = 0; i < graphe.length; i++) {
			for (int j = 0; j < graphe.length; j++) {
				System.out.print(graphe[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void printTab1(double[] graphe) {
		for (int i = 0; i < graphe.length; i++) {
			System.out.print(graphe[i] + " ");
			System.out.println();
		}
	}

	public static void printPaths(Map<Integer, HashMap<Integer, Set<Integer>>> paths) {
		for (Entry<Integer, HashMap<Integer, Set<Integer>>> e : paths.entrySet()) {
			for (Entry<Integer, Set<Integer>> f : e.getValue().entrySet()) {
				System.out.print(f.getValue() + " ");
			}
			System.out.println();
		}
	}
}
