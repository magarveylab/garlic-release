package ca.mcmaster.magarveylab.matching.algorithm.alignment;

import java.util.Random;

public class Permutation {
	
	static Random random = new Random(12345);
	
	/**
	 * Get an array of permutations of 0 to (n-1). If n >= max, return a stratified sample of !max permutations.
	 * @param abstraction
	 * @return
	 */
	public static int[][] getPermutations(int n, int max) {
		// For max or less, return all n! permutations
		int[] array = new int[n];
		for(int i = 0; i < n; i++) {
			array[i] = i;
		}
		if(max > n) {
			return getPermutations(array);
		}
		// return max! permutations 
		return getSampledPermutations(array, factorial(max));
	}
	
	/**
	 * Return all permutations of an array
	 * @param array
	 * @return
	 */
	private static int[][] getPermutations(int[] array) {
		int[][] permutations = new int[factorial(array.length)][array.length];
		// Exit conditions of recursion
		if(array.length == 0) {
			return permutations;
		}
		if(array.length == 1) {
			permutations[0][0] = array[0];
			return permutations;
		}
		
		for(int i = 0; i < array.length; i++) {
			int[] arrayExcludingPresent = new int[array.length-1];
			for(int j = 0; j < i; j++) {
				arrayExcludingPresent[j] = array[j];
			}
			for(int j = i; j < array.length - 1; j++) {
				arrayExcludingPresent[j] = array[j+1];
			}
			
			// Get all permutations of the array excluding the ith observation
			int[][] subPermutations = getPermutations(arrayExcludingPresent);
			
			for(int j = 0; j < subPermutations.length; j++) {
				permutations[j + subPermutations.length * i][0] = array[i];
				for(int k = 0; k < subPermutations[j].length; k++) {
					permutations[j + subPermutations.length * i][k+1] = subPermutations[j][k];
				}
			}
		}
		return permutations;
	}

	/**
	 * Return a sample of permutations of an array
	 * @param array
	 * @return
	 */
	public static int[][] getSampledPermutations(int[] array, int n) {
		int[][] permutations = new int[n][array.length];
		
		if(array.length == 0) {
			return permutations;
		}
		if(array.length == 1) {
			permutations[0][0] = array[0];
			return permutations;
		}
		
		for(int i = 0; i < n; i++) {
			// Knuth shuffle
			int[] currentPermutation = new int[array.length];
			for(int j = 0; j < array.length; j++) {
				currentPermutation[j] = array[j];
			}
			for(int j = currentPermutation.length-1; j > 0; j--) {
				int randIndex = random.nextInt(j+1);
				// swap j with randIndex
				int temp = currentPermutation[j];
				currentPermutation[j] = currentPermutation[randIndex];
				currentPermutation[randIndex] = temp;
			}
			permutations[i] = currentPermutation;
		}
		return permutations;
	}
	/**
	 * 
	 * @return
	 */
	private static int factorial(int n) {
		if(n <= 1) {
			return(1);
		}
		return(n * factorial(n-1));
	}
}
