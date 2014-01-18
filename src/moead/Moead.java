package moead;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Moead {

	private double minF1 = Double.MAX_VALUE;
	private double maxF1 = -Double.MAX_VALUE;
	private double minF2 = Double.MAX_VALUE;
	private double maxF2 = -Double.MAX_VALUE;

	public List<List<Double>> moead(int iterations, int N, int neighborSize, int genomeSize) {
		double[][] weightVectors = Initializer.generateWeightVectors(N);
		
		// 1.1
		// Dosen't work as expected, so was commented out
		// Instead full list is returned
		//List<List<Double>> EP = new ArrayList<List<Double>>(); // external population
		// 1.2
		int[][] B = Initializer.getNeighbors(weightVectors, neighborSize); // neighbors
		// 1.3
		double[][] population = Initializer.getRandomPopulation(N, genomeSize);
		double[][] functionValues = Initializer.computeFunctionValues(population);
		// 1.4
		double[] z = Initializer.getReferencePoint(functionValues); // reference point

		int count = 0;
		boolean end = false;
		while (!end) {
			updateMinMax(functionValues);
			for (int i = 0; i < N; i++) {
				// 2.1
				double[] y = reproduce(population, B[i], i);
				// 2.2 - no repair needed
				// 2.3
				updateReferencePoint(z, y, weightVectors[i]);
				// 2.4
				updateNeighborhood(population, functionValues, B[i], weightVectors, z, y);
				// 2.5
//				Dosen't work as expected, so was commented out
//				updateEP(EP, y, weightVectors[i], z);
			}
			
			if (count % 100 == 0) {
				System.out.println(count);
			}
			if (++count >= iterations) {
				end = true;
			}
		}

		List<List<Double>> result = new ArrayList<List<Double>>();
		for (int i = 0; i < functionValues.length; i++) {
			List<Double> l = new ArrayList<Double>();
			double val1 = (functionValues[i][0] - z[0]) / (maxF1 - minF1);
			double val2 = (functionValues[i][1] - z[1]) / (maxF2 - minF2);
			l.add(val1);
			l.add(val2);
			result.add(l);
			System.out.println(functionValues[i][0] + " " + functionValues[i][1]);
		}
		
		return result;
		
//		for (List<Double> l : EP) {
//			double val1 = l.get(0);
//			double val2 = l.get(1);
//			double weightVector1 = l.get(2);
//			double weightVector2 = l.get(3);
//			
//			val1 = (val1 - z[0]) / (maxF1 - minF1);
//			val2 = (val2 - z[1]) / (maxF2 - minF2);
//			
//			l.set(0, val1);
//			l.set(1, val2);
//			l.remove(3);
//			l.remove(2);
//		}
//		return EP;
	}

	private void updateMinMax(double[][] functionValues) {
		List<Double> f1Values = new ArrayList<Double>();
		List<Double> f2Values = new ArrayList<Double>();
		for (int i = 0; i < functionValues.length; i++) {
			f1Values.add(functionValues[i][0]);
			f2Values.add(functionValues[i][1]);
		}
		
		minF1 = Collections.min(f1Values);
		maxF1 = Collections.max(f1Values);
		minF2 = Collections.min(f2Values);
		maxF2 = Collections.max(f2Values);
	}
	
	private double[] reproduce(double[][] population, int[] neighbors, int index) {
		Random rand = new Random();
		int parent1Index = neighbors[rand.nextInt(neighbors.length)];
		int parent2Index = neighbors[rand.nextInt(neighbors.length)];
		double[] parent1 = population[parent1Index];
		double[] parent2 = population[parent2Index];

		double[] newSolution = new double[parent1.length];
		for (int i = 0; i < parent1.length; i++) {
			if (rand.nextDouble() < 0.01) {
				// mutate (see paper p. 718)
				newSolution[rand.nextInt(parent1.length)] = rand.nextDouble();
			}
			else {
				newSolution[i] = (parent1[i] + parent2[i]) / 2;
			}
		}
		
		return newSolution;
	}

	private void updateReferencePoint(double[] z, double[] y, double[] weightVector) {
		double f1Value = Functions.f1(y);
		double f2Value = Functions.f2(y);
		z[0] = Math.min(z[0], f1Value);
		z[1] = Math.min(z[1], f2Value);
	}

	private void updateNeighborhood(double[][] population,
			double[][] functionValues, int[] neighbors,
			double[][] weightVectors, double[] z, double[] y) {

		double y1Val = Functions.f1(y);
		double y2Val = Functions.f2(y);

		for (int i = 0; i < neighbors.length; i++) {
			int index = neighbors[i];

			double gNeighbor = computeMaxCombinedValues(functionValues[index][0], functionValues[index][1], weightVectors[index], z);
			double gY = computeMaxCombinedValues(y1Val, y2Val, weightVectors[index], z);
			if (gY <= gNeighbor) {
				population[index] = y;
				functionValues[index][0] = y1Val;
				functionValues[index][1] = y2Val;
			}
		}
	}
	
	private double computeMaxCombinedValues(double f1Val, double f2Val, double[] weightVector,
			double[] z) {
//		First possibility: use max
		return Math.max(
				(f1Val - z[0]) / (maxF1 - minF1) * weightVector[0],
				(f2Val - z[1]) / (maxF2 - minF2) * weightVector[1]);
//		Second possibility: don't use max but instead addition
//		return ((f1Val - z[0]) / (maxF1 - minF1) * weightVector[0]) + ((f2Val - z[1]) / (maxF2 - minF2) * weightVector[1]);
	}

//	Dosen't work as expected, so was commented out
//	private void updateEP(List<List<Double>> EP, double[] y, double[] weightVector, double[] z) {
//		// remove all vectors dominated by F(y)
//		// check if any vector in EP dominates F(y)
//		double f1Val = Functions.f1(y);
//		double f2Val = Functions.f2(y);
//		boolean yIsDominated = false;
//		for (Iterator<List<Double>> it = EP.iterator(); it.hasNext();) {
//			List<Double> epVector = it.next();
//
//			double[] yValue = new double[2];
//			yValue[0] = f1Val;
//			yValue[1] = f2Val;
//
//			double[] epValue = new double[2];
//			epValue[0] = epVector.get(0);
//			epValue[1] = epVector.get(1);
//
//			if (dominates(yValue, epValue)) {
//				it.remove();
//			}
//			if (dominates(epValue, yValue)) {
//				yIsDominated = true;
//			}
//			if (yValue[0] == epValue[0] && yValue[1] == epValue[1]) {
//				yIsDominated = true;
//			}
//		}
//		if (!yIsDominated) {
//			List<Double> newExternal = new ArrayList<Double>();
//			newExternal.add(f1Val);
//			newExternal.add(f2Val);
//			newExternal.add(weightVector[0]);
//			newExternal.add(weightVector[1]);
//			EP.add(newExternal);
//		}
//	}
//
//	/**
//	 * @param a
//	 * @return true if a dominates b
//	 */
//	private boolean dominates(double[] a, double[] b) {
//		return (a[0] <= b[0] && a[1] <= b[1]) && (a[0] < b[0] || a[1] < b[1]);
//	}

}
