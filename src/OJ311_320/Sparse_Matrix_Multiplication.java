package OJ311_320;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Sparse_Matrix_Multiplication {
	// https://discuss.leetcode.com/topic/30625/easiest-java-solution
	private int[][] multiply(int[][] A, int[][] B) {
		int row = A.length;
		int internal = B.length;
		int col = B[0].length;
		int[][] result = new int[row][col];
		for (int i = 0; i < row; i++) {
			for (int k = 0; k < internal; k++) {
				if (A[i][k] != 0) {					
					for (int j = 0; j < col; j++) {
						if (B[k][j] != 0) {						
							result[i][j] += A[i][k] * B[k][j];
						}
					}
				}
			}
		}
		return result;
	}
	
	// http://www.cs.cmu.edu/~scandal/cacm/node9.html
	/*
	 * A sparse matrix can be represented as a sequence of rows, 
	 * each of which is a sequence of (column-number, value) 
	 * pairs of the nonzero values in the row.
	 */
	private int[][] multiply_CMU(int[][] A, int[][] B) {
	    int m = A.length, n = A[0].length, nB = B[0].length;
	    int[][] result = new int[m][nB];

	    List[] indexA = new List[m];
	    for(int i = 0; i < m; i++) {
	        List<Integer> numsA = new ArrayList<Integer>();
	        for(int j = 0; j < n; j++) {
	            if(A[i][j] != 0){
	                numsA.add(j); 
	                numsA.add(A[i][j]);
	            }
	        }
	        indexA[i] = numsA;
	    }

	    for(int i = 0; i < m; i++) {
	        List<Integer> numsA = indexA[i];
	        for(int p = 0; p < numsA.size() - 1; p += 2) {
	            int colA = numsA.get(p);
	            int valA = numsA.get(p + 1);
	            for(int j = 0; j < nB; j ++) {
	                int valB = B[colA][j];
	                result[i][j] += valA * valB;
	            }
	        }
	    }

	    return result;   
	}
	
	// like multiply_CMU, but use Map to implement, only one table for B
	int[][] multiply_one_table(int[][] A, int[][] B) {
        if (A == null || A[0] == null || B == null || B[0] == null) return null;
        int m = A.length, n = A[0].length, l = B[0].length;
        int[][] C = new int[m][l];
        Map<Integer, HashMap<Integer, Integer>> tableB = new HashMap<>();
        
        for(int k = 0; k < n; k++) {
            tableB.put(k, new HashMap<Integer, Integer>());
            for(int j = 0; j < l; j++) {
                if (B[k][j] != 0){
                    tableB.get(k).put(j, B[k][j]);
                }
            }
        }

        for(int i = 0; i < m; i++) {
            for(int k = 0; k < n; k++) {
                if (A[i][k] != 0){
                    for (Integer j: tableB.get(k).keySet()) {
                        C[i][j] += A[i][k] * tableB.get(k).get(j);
                    }
                }
            }
        }
        return C;   
    }
	
	// table of (column-number, value), use two tables
	// https://discuss.leetcode.com/topic/30626/java-and-python-solutions-with-and-without-tables/
	int[][] multiply_two_table(int[][] A, int[][] B) {
        if (A == null || B == null) return null;
        if (A[0].length != B.length) 
            throw new IllegalArgumentException("A's column number must be equal to B's row number.");
        Map<Integer, HashMap<Integer, Integer>> tableA = new HashMap<>();
        Map<Integer, HashMap<Integer, Integer>> tableB = new HashMap<>();
        int[][] C = new int[A.length][B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                if (A[i][j] != 0) {
                    if(tableA.get(i) == null) tableA.put(i, new HashMap<Integer, Integer>());
                    tableA.get(i).put(j, A[i][j]);
                }
            }
        }
        
        for (int i = 0; i < B.length; i++) {
            for (int j = 0; j < B[i].length; j++) {
                if (B[i][j] != 0) {
                    if(tableB.get(i) == null) tableB.put(i, new HashMap<Integer, Integer>());
                    tableB.get(i).put(j, B[i][j]);
                }
            }
        }
        
        for (Integer i: tableA.keySet()) {
            for (Integer k: tableA.get(i).keySet()) {
                if (!tableB.containsKey(k)) continue;
                for (Integer j: tableB.get(k).keySet()) {
                    C[i][j] += tableA.get(i).get(k) * tableB.get(k).get(j);
                }
            }
        }
        return C;
    }
	
	public static void main(String[] args) {
		Sparse_Matrix_Multiplication sparsematrix = new Sparse_Matrix_Multiplication();
		int[][] A = {{5, 0, 0}, {-1, 0, 3}};
		int[][] B = {{0, 2, 0}, {0, 0, 1}, {0, 0, 0}};
		int[][] C = sparsematrix.multiply(A, B);
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < C[i].length; j++) {
				System.out.print(C[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		int[][] C1 = sparsematrix.multiply_CMU(A, B);
		for (int i = 0; i < C1.length; i++) {
			for (int j = 0; j < C1[i].length; j++) {
				System.out.print(C1[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
