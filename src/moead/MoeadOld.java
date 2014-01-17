package moead;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MoeadOld {

	private Random rand = new Random();

	public void moead(int maxIterations, double precision,
			List<List<Double>> weightVectors, int neighborCount, int genomeSize) {
		List<List<Integer>> neighbors = findNeighbors(weightVectors,
				neighborCount);
		List<List<Double>> population = initPopulation(weightVectors.size(),
				genomeSize);
		List<Double> paretoFront = initParetoFront(population, weightVectors);

		boolean end = false;
		int counter = 0;
		while (!end) {
			for (int i = 0; i < weightVectors.size(); i++) {
				// randomly select 2 neighbors
				int n1 = rand.nextInt(neighborCount);
				int n2 = rand.nextInt(neighborCount);

				List<Double> newGenome = recombination(
						population.get(neighbors.get(i).get(n1)),
						population.get(neighbors.get(i).get(n2)));
				List<Double> mutatedGenome = mutate(newGenome);

				double f1 = Functions.f1(mutatedGenome);
				double f2 = Functions.f2(mutatedGenome);
				
				for (int j = 0; j < weightVectors.size(); j++) {
					if (neighbors.get(i).contains(j) || rand.nextDouble() > 0.9) {
						double weightVectorSolution = computeSolution(mutatedGenome, weightVectors.get(j), f1, f2);
						if (weightVectorSolution < paretoFront.get(j)) {
							population.set(j, mutatedGenome);
							paretoFront.set(j, weightVectorSolution);
						}
					}
				}
			}

			// termination
			counter++;
			if (counter >= maxIterations) {
				end = true;
			}
		}

		String sol = "";
		for (int i = 0; i < weightVectors.size(); i++) {
//			if (Functions.f1(population.get(i)) < 1.0 && Functions.f2(population.get(i)) < 1.0) { 
				sol += Functions.f1(population.get(i)) + " "
						+ Functions.f2(population.get(i)) + "\n";
//			}
		}
		System.out.println("\nSolution");
		System.out.println(sol);

		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(
					"/tmp/moead-sol.txt"));
			br.write(sol);
			br.flush();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<List<Integer>> findNeighbors(List<List<Double>> weightVectors,
			int neighborCount) {
		System.out.println("\nFind neighbors:");

		List<List<Integer>> result = new ArrayList<List<Integer>>();
		int neighborHalf = neighborCount / 2;

		for (int i = 0; i < weightVectors.size(); i++) {
			List<Integer> neighbors = new ArrayList<Integer>();
			int start = Math.max(0, i - neighborHalf);
			start = Math.min(start, weightVectors.size() - neighborCount - 1);
			for (int j = start; j < start + neighborCount + 1; j++) {
				if (j != i) {
					neighbors.add(j);
				}
			}

			result.add(neighbors);
			System.out.println(i + neighbors.toString());
		}
		return result;

	}

	private List<List<Double>> initPopulation(int populationSize, int genomeSize) {
		List<List<Double>> result = new ArrayList<List<Double>>();
		for (int i = 0; i < populationSize; i++) {
			List<Double> genome = new ArrayList<Double>();
			for (int j = 0; j < genomeSize; j++) {
				genome.add(rand.nextDouble());
			}
			result.add(genome);
		}

		return result;
	}
	
	// For each weight vector MOEA/D records the best solution found
	private List<Double> initParetoFront(List<List<Double>> population,
			List<List<Double>> weightVectors) {
		List<Double> result = new ArrayList<Double>();

		for (int i = 0; i < population.size(); i++) {
			List<Double> genomeResults = new ArrayList<Double>();
			for (int j = 0; j < weightVectors.size(); j++) {
				genomeResults.add(computeSolution(population.get(i),
						weightVectors.get(j)));
			}
			result.add(Collections.min(genomeResults));
		}

		return result;
	}

	private List<Double> recombination(List<Double> parent1,
			List<Double> parent2) {
		List<Double> result = new ArrayList<Double>();
		for (int i = 0; i < parent1.size(); i++) {
			result.add((parent1.get(i) + parent2.get(i)) / 2);
		}
		return result;
	}

	private List<Double> mutate(List<Double> newGenome) {
		int pos = rand.nextInt(newGenome.size() *2/3);//rand.nextInt(newGenome.size());
		double oldVal = newGenome.get(pos);
		double newVal = rand.nextDouble();//(oldVal + rand.nextDouble()) / 2;
		newGenome.set(pos, newVal);
		return newGenome;
	}
	
	private double computeSolution(List<Double> genome,
			List<Double> weightVector) {
		double f1 = Functions.f1(genome);
		double f2 = Functions.f2(genome);
		return computeSolution(genome, weightVector, f1, f2);
	}
	
	private double computeSolution(List<Double> genome,
			List<Double> weightVector, double f1, double f2) {
		double f1Value = f1 * weightVector.get(0);
		double f2Value = f2 * weightVector.get(1);
		return f1Value + f2Value;
	}
}
