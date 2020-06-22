package OJ0051_0060;

import java.util.List;
import java.util.LinkedList;

public class Permutation_Sequence {
	/*
	 * https://discuss.leetcode.com/topic/5081/an-iterative-solution-for-reference
	 * 
	 * n = 4, you have {1, 2, 3, 4}
	 * 
	 * If you were to list out all the permutations you have
	 * 1 + (permutations of 2, 3, 4)
	 * 2 + (permutations of 1, 3, 4)
	 * 3 + (permutations of 1, 2, 4)
	 * 4 + (permutations of 1, 2, 3)
	 * 
	 * We know how to calculate the number of permutations of n numbers... n! So each 
	 * of those with permutations of 3 numbers means there are 6 possible permutations. 
	 * Meaning there would be a total of 24 permutations in this particular one. So if 
	 * you were to look for the (k = 14) 14th permutation, it would be in the
	 * 
	 * 3 + (permutations of 1, 2, 4) subset.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/17348/explain-like-i-m-five-java-solution-in-o-n
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/65646/java-recursive-and-iterative
	 */
	public String getPermutation(int n, int k) {
		List<Integer> num = new LinkedList<Integer>();
		for (int i = 1; i <= n; i++)
			num.add(i);
		
		int[] fact = new int[n]; // factorial
		fact[0] = 1;
		for (int i = 1; i < n; i++)
			fact[i] = i * fact[i - 1];
		
		k = k - 1;  // start at 0
		
		StringBuilder sb = new StringBuilder();
		for (int i = n - 1; i >= 0; i--) {
			int ind = k / fact[i];
			k = k % fact[i];
			
			sb.append(num.get(ind));
			num.remove(ind);
		}
		return sb.toString();
	}
	
	/*
	 * https://discuss.leetcode.com/topic/21159/clean-java-solution
	 * 
	 * The basic idea is to decide which is the correct number starting from the highest digit.
     * Use k divide the factorial of (n-1), the result represents the ith not used number.
     * Then update k and the factorial to decide next digit.
     * 
     * Rf : https://discuss.leetcode.com/topic/5081/an-iterative-solution-for-reference/2
	 */
	public String getPermutation2(int n, int k) {
		LinkedList<Integer> notUsed = new LinkedList<Integer>();
		int weight = 1;
		for (int i = 1; i <= n; i++) {
			notUsed.add(i);
			if (i == n)
				break;
			weight = weight * i;
		}

		String res = "";
		k = k - 1;
		while (true) {
			res = res + notUsed.remove(k / weight);
			k = k % weight;
			if (notUsed.isEmpty())
				break;
			weight = weight / notUsed.size();
		}
		return res;
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public String getPermutation_nextPermutation(int n, int k) {
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i + 1;
        }
        int count = k;
        while (count != 1) {
            nextPermutation(nums);
            count--;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nums.length; i++) {
            sb.append(nums[i]);
        }
        return sb.toString();
    }
    public void nextPermutation(int[] nums) {
        int j = nums.length - 2;
        while (j >= 0 && nums[j] >= nums[j + 1])
            j--;
        if (j >= 0) {
            int k = nums.length - 1;
            while (nums[j] >= nums[k])
                k--;
            swap(nums, j, k);
        }
        int last = nums.length - 1;
        while (j + 1 < last) {
            swap(nums, j + 1, last);
            last--;
            j++;
        }
    }
    
    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Permutation_Sequence permutationSequence = new Permutation_Sequence();
		int n = 5;
		int k = 15;
		System.out.println(permutationSequence.getPermutation(n, k));

	}

}
