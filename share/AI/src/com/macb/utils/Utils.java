package com.macb.utils;

public class Utils {

	public int[] getMaxMin(int[] x) {

		int max = x[0];
		int min = x[0];
		int imax = 0;
		int imin = 0;

		for (int i = 1; i < x.length; i++) {

			if (x[i] > max) {
				max = x[i];
				imax = i;
			}
			if (x[i] < min) {
				min = x[i];
				imin = i;
			}
		}
		return new int[] { max, min, imax, imin };
	}

	public double getArrayMean(double[] array) {

		double mean = 0;
		for (double d : array) {
			mean += d;
		}
		return mean * 1.0 / (array.length);
	}

	public double getArrayVariance(double[] array) {
		double mean = getArrayMean(array);
		double variance = 0;
		for (double d : array) {
			variance += (mean - d) * (mean - d);
		}
		return Math.sqrt(variance * 1.0 / array.length);
	}

	public int[] getRandomArray(int min, int max, int n) {
		if (n > (max - min + 1) || max < min) {
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while (count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if (num == result[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				result[count] = num;
				count++;
			}
		}
		return result;
	}

	public double[] String2Double(String str) {

		String[] s = str.split(" ");
		double[] fv = new double[s.length - 1];
		for (int i = 0; i < fv.length; i++) {
			fv[i] = Double.valueOf(s[i + 1]);
		}
		return fv;
	}
}
