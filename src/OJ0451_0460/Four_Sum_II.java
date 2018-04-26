package OJ0451_0460;

import java.util.Arrays;
import java.util.HashMap;

public class Four_Sum_II {
	/*
	 * https://leetcode.com/problems/4sum-ii/discuss/93962/Awesome-beautiful-java-python-code
	 * 
	 * To improve the solution we can divide arrays into two parts. 
	 * Then make calculation of sums of one part (A[i] + B[j]) and store their sum's 
	 * occurrences counter in a HashMap. While calculating second part arrays' sum 
	 * (secondSum = C[k] + D[h]) we can check whether map contains secondSum*(-1);
	 * 
	 * A[i] + B[j] == - C[k] - D[h]
	 * A[i] + B[j] == - (C[k] + D[h])
	 * 
	 * Rf :
	 * https://leetcode.com/problems/4sum-ii/discuss/93973/Dividing-arrays-into-two-parts.-Full-thinking-process-from-naive(N4)-to-effective(N2)-solution
	 * https://leetcode.com/problems/4sum-ii/discuss/93920/Clean-java-solution-O(n2)
	 */
	public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
		HashMap<Integer, Integer> hashMap = new HashMap<>();
		for (int a : A) {
			for (int b : B) {
				hashMap.put(a + b, hashMap.getOrDefault(a + b, 0) + 1);
			}
		}
		
		int result = 0;
		for (int c : C) {
			for (int d : D) {
				result += hashMap.getOrDefault(-c - d, 0);
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/4sum-ii/discuss/114123/Java-solution-using-sort-not-map-beats-93-O(n2*log(n))-time
	 * 
	 * Comparing with map, this solution using sorting costs O(n^2*log(n)) time but 
	 * actually runs faster because it avoids map overheads
	 * 
	 * sorting, O(n^2*log(n)) time, O(n^2) space, no map
	 */
	public int fourSumCount_sort_2_array(int[] A, int[] B, int[] C, int[] D) {
		int nAB = A.length * B.length;
		int[] sumAB = new int[nAB];
		int i = 0;
		for (int a : A) {
			for (int b : B) {
				sumAB[i++] = a + b;
			}
		}
		Arrays.sort(sumAB);
		
		int nCD = C.length * D.length;
		int[] negSumCD = new int[nCD];
		i = 0;
		for (int c : C) {
			for (int d : D) {
				negSumCD[i++] = -(c + d);
			}
		}
		Arrays.sort(negSumCD);
		
		// if sumAB = negSumCD, then 4 sum = 0
		i = 0;
		int j = 0;
		int res = 0;
		while (i < nAB && j < nCD) {
			if (sumAB[i] < negSumCD[j])
				i++;
			else if (sumAB[i] > negSumCD[j])
				j++;
			else {
				// sumAB[i] == negSumCD[j]
				// need to count number of same consecutive values, and multiply them
				int countAB = 1, countCD = 1;
				while (++i < nAB && sumAB[i - 1] == sumAB[i])
					countAB += 1;
				while (++j < nCD && negSumCD[j - 1] == negSumCD[j])
					countCD += 1;
				
				res += countAB * countCD;
			}
		}
		return res;
	}

}
