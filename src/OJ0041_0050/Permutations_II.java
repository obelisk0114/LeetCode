package OJ0041_0050;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class Permutations_II {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/31445/really-easy-java-solution-much-easier-than-the-solutions-with-very-high-vote
	 * 
	 * " boolean[] used " is used to indicate whether the value is added to list.
     * Sort the array "int[] nums" to make sure we can skip the same value.
     * when a number has the same value with its previous, 
     * we can use this number only if his previous is used
     * 
     * Rf : 
     * https://discuss.leetcode.com/topic/31445/really-easy-java-solution-much-easier-than-the-solutions-with-very-high-vote/26
     * https://discuss.leetcode.com/topic/5993/share-my-recursive-solution
	 */
	public List<List<Integer>> permuteUnique(int[] nums) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		if (nums == null || nums.length == 0)
			return res;
		boolean[] used = new boolean[nums.length];
		List<Integer> list = new ArrayList<Integer>();
		Arrays.sort(nums);
		dfs(nums, used, list, res);
		return res;
	}
	public void dfs(int[] nums, boolean[] used, List<Integer> list, List<List<Integer>> res) {
		if (list.size() == nums.length) {
			res.add(new ArrayList<Integer>(list));
			return;
		}
		for (int i = 0; i < nums.length; i++) {
			if (used[i])
				continue;
			if (i > 0 && nums[i - 1] == nums[i] && used[i - 1])
				continue;
			used[i] = true;
			list.add(nums[i]);
			dfs(nums, used, list, res);
			used[i] = false;
			list.remove(list.size() - 1);
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/36221/share-my-java-code-with-detailed-explanantion
	 * 
	 * Rf : https://discuss.leetcode.com/topic/36221/share-my-java-code-with-detailed-explanantion/3
	 */
	public List<List<Integer>> permuteUnique_set_check(int[] nums) {
		List<List<Integer>> ans = new ArrayList<>();
		if (nums == null || nums.length == 0) {
			return ans;
		}
		permute_set_check(ans, nums, 0);
		return ans;
	}
	private void permute_set_check(List<List<Integer>> ans, int[] nums, int index) {
		if (index == nums.length) {
			List<Integer> temp = new ArrayList<>();
			for (int num : nums) {
				temp.add(num);
			}
			ans.add(temp);
			return;
		}
		Set<Integer> appeared = new HashSet<>();
		for (int i = index; i < nums.length; ++i) {
			if (appeared.add(nums[i])) {
				swap(nums, index, i);
				permute_set_check(ans, nums, index + 1);
				swap(nums, index, i);
			}
		}
	}
    	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/38166/java-recursive-solution-with-minimal-extra-space
	 * 
	 * Rf : http://wuchong.me/blog/2014/07/28/permutation-and-combination-realize/#去重的全排列
	 */
	public List<List<Integer>> permuteUnique_check_same_number(int[] nums) {
		List<List<Integer>> result = new ArrayList<>();
		if (nums.length == 0)
			return result;
		backTrack(nums, result, 0, nums.length - 1);
		return result;
	}
	public void backTrack(int[] nums, List<List<Integer>> result, int begin, int end) {
		if (begin > end) {
			// changing int[] to arraylist and save into final result list
			result.add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					for (int i : nums)
						add(i);
				}
			});
		}

		else {
			for (int i = begin; i <= end; i++) {
				if (!isDuplicate(nums, begin, i)) {
					swap(nums, i, begin);
					backTrack(nums, result, begin + 1, end);
					swap(nums, i, begin);
				}
			}
		}
	}

	// check whether the current number has appeared in the subarray.
	// if same number appears, we do not need to move this number again
	public boolean isDuplicate(int[] nums, int begin, int i) {
		for (int a = begin; a < i; a++) {
			if (nums[a] == nums[i]) {
				return true;
			}
		}
		return false;
	}

	public void swap(int[] nums, int i, int j){
	    int buf = nums[i];
	    nums[i] = nums[j];
	    nums[j] = buf;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/24347/short-and-fast-recursive-java-solution-easy-to-understand-with-explaination
	 * 
	 * Put every number at the beginning of the array, 
	 * and then do the same thing for the rest of the array.
	 */
	public List<List<Integer>> permuteUnique_put_at_beginning(int[] nums) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		Arrays.sort(nums);
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int num : nums)
			list.add(num);
		permute_put_beginning(list, 0, res);
		return res;
	}
	private void permute_put_beginning(LinkedList<Integer> nums, int start, List<List<Integer>> res) {
		if (start == nums.size() - 1) {
			res.add(new LinkedList<Integer>(nums));
			return;
		}
		for (int i = start; i < nums.size(); i++) {
			if (i > start && nums.get(i) == nums.get(i - 1))
				continue;
			nums.add(start, nums.get(i));
			nums.remove(i + 1);
			permute_put_beginning(nums, start + 1, res);
			nums.add(i + 1, nums.get(start));
			nums.remove(start);
		}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/37030/java-iterative-solution-no-set-needed
	 * 
	 * In each iteration, put the new number to all possible place.
     * To avoid duplicate we also have to:
         1. For duplicate numbers in a row, only add same number in in front of them.
         2. Break when same number exists in the permutation.
	 */
	public List<List<Integer>> permuteUnique_iterative(int[] nums) {
		LinkedList<List<Integer>> r = new LinkedList<>();
		r.add(new ArrayList<Integer>());
		for (int i = 0; i < nums.length; i++) {
			int n = r.size();
			for (int j = 0; j < n; j++) {
				List<Integer> list = r.poll();
				for (int k = 0; k <= list.size(); k++) {
					if (k > 0 && list.get(k - 1) == nums[i])
						break;
					ArrayList<Integer> t = new ArrayList<>(list);
					t.add(k, nums[i]);
					r.offer(t);
				}
			}
		}
		return r;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/3170/accepted-iterative-solution-in-java
	 * 
	 * Rf : https://discuss.leetcode.com/topic/12923/short-iterative-java-solution
	 */
	public List<List<Integer>> permuteUnique_iterative_set(int[] num) {
        Set<List<Integer>> permutations = new HashSet<List<Integer>>();
        
        if(num.length > 0){
            permutations.add(Arrays.asList(num[0]));
            
            for(int index = 1; index < num.length; index++) {
              
                Set<List<Integer>> newPermutations = new HashSet<List<Integer>>();
                for(List<Integer> list : permutations){
                    for(int innerIndex = 0; innerIndex <= list.size(); innerIndex++){
                        List<Integer> newList = new ArrayList<>(list);
                        newList.add(innerIndex, num[index]);
                        newPermutations.add(newList);
                    }
                }
                permutations = newPermutations;
            }
        }
        return new ArrayList<List<Integer>>(permutations);
    }
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/3194/a-non-recursive-c-implementation-with-o-1-space-cost
	 */
	public List<List<Integer>> permuteUnique_nextPermutation(int[] nums) {
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Permutations_II permutations2 = new Permutations_II();
		int[] num = {1, 3, 7, 1};
		List<List<Integer>> ans = permutations2.permuteUnique_check_same_number(num);
		for (List<Integer> element : ans) {
			for (Integer single : element) {
				System.out.print(single + " ");
			}
			System.out.println();
		}

	}

}
