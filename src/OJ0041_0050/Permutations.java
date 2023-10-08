package OJ0041_0050;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/*
 * http://www.csie.ntnu.edu.tw/~u91029/Permutation.html
 * http://zhouyichu.com/algorithm/Permutation-Generation-1/
 * http://zhouyichu.com/algorithm/Permutation-Generation-2/
 * https://en.wikipedia.org/wiki/Steinhaus%E2%80%93Johnson%E2%80%93Trotter_algorithm
 * http://www.cnblogs.com/william-cheung/p/3472300.html
 * 
 * https://discuss.leetcode.com/topic/46162/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partioning
 * https://discuss.leetcode.com/topic/6740/share-my-three-different-solutions/6
 * 
 * Others :
 * PQ tree
 */

public class Permutations {
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/5578/my-java-accepted-solution-without-additional-space
	 * 
	 * At each position, Specify the element by swapping with values with a larger index. 
	 * The value at the first position can swap with position 1,2,...,n-1, 
	 * after each swap, I will do a recursion for the rest of the array.
	 * 
	 * Rf : 
	 * http://zhouyichu.com/algorithm/Permutation-Generation-1/#固定首元素
	 */
	public List<List<Integer>> permute_back_swap(int[] num) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		permute_back(result, num, 0);
		return result;
	}
	private void permute_back(List<List<Integer>> result, int[] array, int start) {
		if (start == array.length) {
			List<Integer> current = new ArrayList<Integer>();
			for (int a : array) {
				current.add(a);
			}
			result.add(current);
            return;
		}
		for (int i = start; i < array.length; i++) {
			swap(array, start, i);
			permute_back(result, array, start + 1);
			swap(array, start, i);
		}
	}

	private void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : 
	 * https://en.wikipedia.org/wiki/Heap's_algorithm
	 * https://stackoverflow.com/questions/29042819/heaps-algorithm-permutation-generator
	 * http://www.sanfoundry.com/java-program-implement-heaps-algorithm-permutation-n-numbers/
	 */
	public List<List<Integer>> permute_heap(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
		permute_heap(result, nums, nums.length);
		return result;
    }
    private void permute_heap(List<List<Integer>> result, int[] array, int end) {
		if (end == 1) {
			List<Integer> current = new ArrayList<Integer>();
			for (int a : array) {
				current.add(a);
			}
			result.add(current);
            return;
		}
	    for (int i = 0; i < end; i++) {
			permute_heap(result, array, end - 1);
            if (end % 2 == 0) {          // (end % 2 == 1) ?
                swap(array, 0, end - 1);
            }
            else {
                swap(array, i, end - 1);
            }
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/23036/java-clean-code-two-recursive-solutions
	 * 
	 * Rf : http://zhouyichu.com/algorithm/Permutation-Generation-1/#插入法
	 * 
	 * Other code : 
	 * https://discuss.leetcode.com/topic/22890/java-solution-easy-to-understand-backtracking
	 */
	public List<List<Integer>> permute_insert(int[] nums) {
		List<List<Integer>> permutations = new ArrayList<>();
		if (nums.length == 0) {
			return permutations;
		}

		collectPermutations(nums, 0, new ArrayList<>(), permutations);
		return permutations;
	}
	private void collectPermutations(int[] nums, int start, List<Integer> permutation,
			List<List<Integer>> permutations) {

		if (permutation.size() == nums.length) {
			permutations.add(permutation);
			return;
		}

		for (int i = 0; i <= permutation.size(); i++) {     // including end position
			List<Integer> newPermutation = new ArrayList<>(permutation);
			newPermutation.add(i, nums[start]);
			collectPermutations(nums, start + 1, newPermutation, permutations);
		}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/6377/my-ac-simple-iterative-java-python-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/10812/share-my-short-iterative-java-solution
	 */
	public List<List<Integer>> permute_iterative(int[] num) {
		List<List<Integer>> ans = new ArrayList<List<Integer>>();
		if (num.length == 0)
			return ans;
		List<Integer> l0 = new ArrayList<Integer>();
		l0.add(num[0]);
		ans.add(l0);
		for (int i = 1; i < num.length; ++i) {
			List<List<Integer>> new_ans = new ArrayList<List<Integer>>();
			for (int j = 0; j <= i; ++j) {
				for (List<Integer> l : ans) {
					List<Integer> new_l = new ArrayList<Integer>(l);
					new_l.add(j, num[i]);
					new_ans.add(new_l);
				}
			}
			ans = new_ans;
		}
		return ans;
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/11294/accepted-recursive-solution-in-java
	 * https://discuss.leetcode.com/topic/30812/java-backtracking-solution
	 */
	public List<List<Integer>> permute_exclude_duplicate(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        generate(nums, nums.length, new ArrayList<>(), results);
        return results;
    }
    private void generate(int[] nums, int start, List<Integer> next, List<List<Integer>> results) {
        if (start == 0) {
            List<Integer> add = new ArrayList<>(next);
            results.add(add);
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (next.contains(nums[i])) {
                continue;
            }
            next.add(nums[i]);
            generate(nums, start - 1, next, results);
            next.remove(next.size() - 1);
        }
    }
    
    /*
     * The following 3 functions are by myself.
     * 
     * Rf : https://discuss.leetcode.com/topic/15218/easy-solution-using-code-in-nextpermutation-can-be-used-in-permutations-ii-without-modification
     */
    public List<List<Integer>> permute_nextPermutation(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        Integer[] cur = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            cur[i] = nums[i];
        }
        List<Integer> temp = new ArrayList<>(Arrays.asList(cur));
        results.add(temp);
        while (true) {
            nextPermutation(cur);
            for (int i = 0; i < nums.length; i++) {
                if (cur[i] == nums[i] && i == nums.length - 1) {
                    return results;
                }
                if (cur[i] != nums[i]) {
                    break;
                }
            }
            temp = new ArrayList<>(Arrays.asList(cur));
            results.add(temp);
            //for (int i = 0; i < cur.length; i++) {
                //System.out.print(cur[i] + " ");
            //}
            //System.out.println();
        }
    }
    private void nextPermutation(Integer[] nums) {
        int i = nums.length - 2;
        while (i >= 0 && nums[i] >= nums[i + 1])
            i--;
        if (i >= 0) {
            int j = nums.length - 1;
            while (nums[j] <= nums[i])
                j--;
            swap2(nums, i, j);
        }
        int last = nums.length - 1;
        while (last > i + 1) {
            swap2(nums, i + 1, last);
            last--;
            i++;
        }
    }
    
    private void swap2(Integer[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
    
    // https://discuss.leetcode.com/topic/30731/new-approach-directly-find-the-kth-permutation-k-1-n-with-a-simple-loop
    
    /***********     other area     ***********/
    
    // store a combination
	void print(int comb) {
		int N = 5;
		System.out.print("{");
		for (int i = 0; i < N; ++i)
			if ((comb & (1 << i)) != 0)
				System.out.print(1);
			else
				System.out.print(0);
		System.out.print("}\n");
	}
	
	// http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation
	void next_combination(int comb) {
		int t = (comb | (comb - 1)) + 1;
		comb = t | ((((t & -t) / (comb & -comb)) >> 1) - 1);
		System.out.println(comb);
	}
	
	// 排容原理
	void inclusion_exclusion_principle(int N) {
		for (int comb = 0; comb < 1 << N; ++comb) {
			int c = 0; // size of set
			for (int i = 0; i < N; ++i)
				if ((comb & (1 << i)) != 0)
					c++;

			if ((c & 1) != 0)
				System.out.println("negative");
			else
				System.out.println("positive");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Permutations permutations = new Permutations();
		int N = 3;
		
		// dictionary order
		// https://stackoverflow.com/questions/4421400/how-to-get-0-padded-binary-representation-of-an-integer-in-java
		for (int i = 0; i < (1 << N); i++) {
			/*****   System.out.println(i);   *****/
			String s1 = Integer.toBinaryString( (1 << N) | i ).substring( 1 );
			//String s = String.format("%3s", Integer.toBinaryString(i)).replace(' ', '0');
			System.out.println(s1);
		}
		System.out.println();
		
		// gray code
		for (int i = 0; i < (1 << N); i++) {
			/*****   System.out.println(i ^ (i >> 1));   *****/
			String s = Integer.toBinaryString( (1 << N) | (i ^ (i >> 1)) ).substring( 1 );
			System.out.println(s);
		}
		System.out.println();
		
		int n = 4;
		int k = 2;
		// C(n,k)   ;   Gosper's Hack
		for (int comb = (1 << k) - 1; comb < 1 << n;) {
			String s = Integer.toBinaryString( (1 << n) | comb ).substring( 1 );
			System.out.println(s);
			//System.out.println(comb);
		    int x = comb & -comb, y = comb + x;
		    comb = ((comb ^ y) / x >> 2) | y;
		}
		System.out.println();
		
		//permutations.print(N);
		//permutations.next_combination(N);
		//permutations.inclusion_exclusion_principle(2);
		
		int[] num = {1, 3, 7, 8};
		List<List<Integer>> ans = permutations.permute_back_swap(num);
		for (List<Integer> element : ans) {
			for (Integer single : element) {
				System.out.print(single + " ");
			}
			System.out.println();
		}
	}

}
