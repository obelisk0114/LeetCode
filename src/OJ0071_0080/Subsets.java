package OJ0071_0080;

//import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/*
 * https://discuss.leetcode.com/topic/19110/c-recursive-iterative-bit-manipulation-solutions-with-explanations
 * https://discuss.leetcode.com/topic/19561/python-easy-to-understand-solutions-dfs-recursively-bit-manipulation-iteratively
 */

public class Subsets {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/10885/java-subsets-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/22636/very-simple-and-fast-java-solution-with-explanation
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/15602/java-recursion-solution
	 */
	public List<List<Integer>> subsets_dfs(int[] S) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if (S.length == 0) {
			return result;
		}

		Arrays.sort(S);
		dfs(S, 0, new ArrayList<Integer>(), result);
		return result;
	}
	public void dfs(int[] s, int index, List<Integer> path, List<List<Integer>> result) {
		result.add(new ArrayList<Integer>(path));

		for (int i = index; i < s.length; i++) {
			path.add(s[i]);
			dfs(s, i + 1, path, result);
			path.remove(path.size() - 1);
		}
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/35553/share-my-12-line-simple-java-code-with-brief-explanations
	 */
	public List<List<Integer>> subsets_myself(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        addSub(nums, 0, new ArrayList<Integer>(), res);
        return res;
    }
    private void addSub(int[] nums, int level, List<Integer> list, List<List<Integer>> res) {
        if (level == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                list.add(nums[level]);
                addSub(nums, level + 1, list, res);
                list.remove(list.size() - 1);
            }
            else {
                addSub(nums, level + 1, list, res);
            }
        }
    }
    
    /*
     * https://discuss.leetcode.com/topic/9031/simple-java-solution-with-for-each-loops
     * 
     * Rf : https://discuss.leetcode.com/topic/30867/simple-iteration-no-recursion-no-twiddling-explanation
     */
	public List<List<Integer>> subsets_iterative(int[] S) {
		List<List<Integer>> res = new ArrayList<>();
		res.add(new ArrayList<Integer>());

		Arrays.sort(S);
		for (int i : S) {
			List<List<Integer>> tmp = new ArrayList<>();
			for (List<Integer> sub : res) {
				List<Integer> a = new ArrayList<>(sub);
				a.add(i);
				tmp.add(a);
			}
			res.addAll(tmp);
		}
		return res;
	}
    
    /*
     * https://discuss.leetcode.com/topic/23069/simple-java-solution-using-bit-operations
     * 
     * Rf : 
     * https://discuss.leetcode.com/topic/2764/my-solution-using-bit-manipulation/13
     * https://discuss.leetcode.com/topic/2764/my-solution-using-bit-manipulation/7
     * https://discuss.leetcode.com/topic/2764/my-solution-using-bit-manipulation/4
     */
	public List<List<Integer>> subsets_bit_manipulation(int[] nums) {
		int n = nums.length;
		List<List<Integer>> subsets = new ArrayList<>();
		for (int i = 0; i < (1 << n); i++) {
			List<Integer> subset = new ArrayList<>();
			for (int j = 0; j < n; j++) {
				if (((1 << j) & i) != 0)
					subset.add(nums[j]);
			}
			//Collections.sort(subset);
			subsets.add(subset);
		}
		return subsets;
	}

}
