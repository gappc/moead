package moead;

import java.util.List;

public class Printer {

	public static void printWeightVectors(double[][] weightVectors) {
		System.out.println("-----Weight vectors-----");
		printDoubles(weightVectors);
		System.out.println("------------------------");
	}
	
	public static void printNeighbors(int[][] neighbors) {
		System.out.println("-----Neighbors-----");
		printInts(neighbors);
		System.out.println("-------------------");
	}
	
	public static void printPopulation(double[][] population) {
		System.out.println("-----Population-----");
		printDoubles(population);
		System.out.println("--------------------");
	}
	
	public static void printSolution(List<List<Double>> solution) {
		System.out.println("-----Solution-----");
		for (List<Double> l : solution) {
			System.out.println(l);
		}
		System.out.println("------------------");
	}
	
	private static void printDoubles(double[][] values) {
		for (int i = 0; i < values.length; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < values[i].length; j++) {
				sb.append(values[i][j] + ", ");
			}
			System.out.println(sb);
		}
	}
	
	private static void printInts(int[][] values) {
		for (int i = 0; i < values.length; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < values[i].length; j++) {
				sb.append(values[i][j] + ", ");
			}
			System.out.println(sb);
		}
	}
}
