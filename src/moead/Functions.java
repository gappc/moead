package moead;

import java.util.List;

public class Functions {

	public static double f1(List<Double> x) {
		double result = x.get(0);
		int n = x.size();
		
		double sum = 0;
		double count = 0; // length of J
		for (int j = 3; j < n; j += 2) {
			double pow = 0.5 * (1.0 + (3 * (j - 2)) / (n + 2));
			double term = Math.pow(x.get(j) - Math.pow(x.get(0), pow), 2);
			sum += term;
			count++;
		}
		
		return result + (2 / count) * sum;
	}

	public static double f2(List<Double> x) {
		double result = 1 - Math.sqrt(x.get(0));
		int n = x.size();
		
		double sum = 0;
		double count = 0; // length of J
		for (int j = 2; j < n; j += 2) {
			double pow = 0.5 * (1.0 + (3 * (j - 2)) / (n + 2));
			double term = Math.pow(x.get(j) - Math.pow(x.get(0), pow), 2);
			sum += term;
			count++;
		}
		
		return result + (2 / count) * sum;
	}
	
	public static double f1(double[] x) {
		double result = x[0];
		int n = x.length;
		
		double sum = 0;
		double count = 0; // length of J
		for (int j = 3; j < n; j += 2) {
			double pow = 0.5 * (1.0 + (3 * (j - 2)) / (n + 2));
			double term = Math.pow(x[j] - Math.pow(x[0], pow), 2);
			sum += term;
			count++;
		}
		
		return result + (2 / count) * sum;
	}

	public static double f2(double[] x) {
		double result = 1 - Math.sqrt(x[0]);
		int n = x.length;
		
		double sum = 0;
		double count = 0; // length of J
		for (int j = 2; j < n; j += 2) {
			double pow = 0.5 * (1.0 + (3 * (j - 2)) / (n + 2));
			double term = Math.pow(x[j] - Math.pow(x[0], pow), 2);
			sum += term;
			count++;
		}
		
		return result + (2 / count) * sum;
	}
	
	public static double f1Norm(double[] x, double min, double max) {
		return f1(x) / (max - min);
	}

	public static double f2Norm(double[] x, double min, double max) {
		return f2(x) / (max - min);		
	}
}
