package moead;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Initializer {
	
	public static double[][] generateWeightVectors(int N) {
		// fixed objective size to 2 
		double[][] weightVectors = new double[N][2];

		double slice = 1.0 / (N - 1);
		for (int i = 0; i < N; i++) {
			weightVectors[i][0] = i * slice;
			weightVectors[i][1] = (1 - i * slice);
		}

		return weightVectors;
	}
	
	public static int[][] getNeighbors(double[][] weightVectors, int neighborSize) {
		int N = weightVectors.length;
		double[][] distances = getDistances(weightVectors);

		int[][] neighbors = new int[N][neighborSize];
		for (int i = 0; i < N; i++) {
			neighbors[i] = getSmallestValues(distances[i], neighborSize);
		}
		return neighbors;
	}
	
	public static double[][] getRandomPopulation(int N, int genomeSize) {
		Random rand = new Random();
		double[][] population = new double[N][genomeSize];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < genomeSize; j++) {
				population[i][j] = rand.nextDouble();
			}
		}
		return population;
	}
	
	public static double[][] computeFunctionValues(double[][] population) {
		double[][] values = new double[population.length][2];
		for (int i = 0; i < population.length; i++) {
			values[i][0] = Functions.f1(population[i]);
			values[i][1] = Functions.f2(population[i]);
		}
		return values;
	}
	
	public static double[] getReferencePoint(double[][] functionValues) {
		double[] referencePoint = new double[2];

		List<Double> f1Values = new ArrayList<Double>();
		List<Double> f2Values = new ArrayList<Double>();
		for (int i = 0; i < functionValues.length; i++) {
			f1Values.add(functionValues[i][0]);
			f2Values.add(functionValues[i][1]);
		}
		
		referencePoint[0] = Collections.min(f1Values);
		referencePoint[1] = Collections.min(f2Values);
		return referencePoint;
	}
	
	private static double[][] getDistances(double[][] weightVectors) {
		int N = weightVectors.length;
		double[][] distances = new double[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				double distance = Math
						.sqrt(Math.pow(weightVectors[i][0]
								- weightVectors[j][0], 2)
								+ Math.pow(weightVectors[i][1]
										- weightVectors[j][1], 2));
				distances[i][j] = distance;
				distances[j][i] = distance;
			}
		}
		return distances;
	}
	
	private static int[] getSmallestValues(double[] distances, int neighborSize) {
		List<NeighborHelper> sortingHelper = new ArrayList<NeighborHelper>();
		Initializer initializer = new Initializer();
		for (int i = 0; i < distances.length; i++) {
			NeighborHelper n = initializer.new NeighborHelper(distances[i], i);
			sortingHelper.add(n);
		}
		Collections.sort(sortingHelper);
		
		
		int[] neighbors = new int[neighborSize];
		for (int i = 0; i < neighborSize; i++) {
			neighbors[i] = sortingHelper.get(i).index;
		}
		return neighbors;
	}
	
	private class NeighborHelper implements Comparable<NeighborHelper>{
		double distance;
		int index;
		public NeighborHelper(double distance, int index) {
			this.distance = distance;
			this.index = index;
		}
		@Override
		public int compareTo(NeighborHelper o) {
			double diff = this.distance - o.distance;
			return diff == 0 ? 0 : (diff < 0 ? -1 : 1);
		}
	}




}
