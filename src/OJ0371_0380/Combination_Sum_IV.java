package OJ0371_0380;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Combination_Sum_IV {
	/*
	 * https://discuss.leetcode.com/topic/52186/my-3ms-java-dp-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/52193/simple-java-dp
	 */
	public int combinationSum4_bottom_up(int[] nums, int target) {
		Arrays.sort(nums);
		int[] res = new int[target + 1];
		res[0] = 1;
		for (int i = 1; i < res.length; i++) {
			for (int num : nums) {
				if (num > i)
					break;
				else
					res[i] += res[i - num];
			}
		}
		return res[target];
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/52302/1ms-java-dp-solution-with-detailed-explanation
	 * 
	 * Imagine we only need one more number to reach target, 
	 * this number can be any one in the array, right? 
	 * 
	 * So the # of combinations of target, 
	 * comb[target] = sum(comb[target - nums[i]]), 
	 * where 0 <= i < nums.length, and target >= nums[i].
	 */
	private int[] dp;
	public int combinationSum4_top_down(int[] nums, int target) {
		dp = new int[target + 1];
		Arrays.fill(dp, -1);
		dp[0] = 1;
		return helper(nums, target);
	}
	private int helper(int[] nums, int target) {
		if (dp[target] != -1) {
			return dp[target];
		}
		int res = 0;
		for (int i = 0; i < nums.length; i++) {
			if (target >= nums[i]) {
				res += helper(nums, target - nums[i]);
			}
		}
		dp[target] = res;
		return res;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://discuss.leetcode.com/topic/52255/java-recursion-solution-using-hashmap-as-memory
	 */
	Map<Integer, Integer> map = new HashMap<>();
	public int combinationSum4_map(int[] nums, int target) {
		int count = 0;
		if (nums == null || nums.length == 0 || target < 0)
			return 0;
		if (target == 0)
			return 1;
		if (map.containsKey(target))
			return map.get(target);
		for (int num : nums) {
			count += combinationSum4_map(nums, target - num);
		}
		map.put(target, count);
		return count;
	}
	
	/*
	 * The following variable and 3 functions are by myself.
	 * will TLE
	 */
	private int sum_self = 0;
    public int combinationSum4_self(int[] nums, int target) {
        Arrays.sort(nums);
        //List<List<Integer>> ret = new ArrayList<>();
        combine_self(nums, target, 0, new ArrayList<Integer>());
        return sum_self;
    }
    
    private void combine_self(int[] nums, int target, int start, List<Integer> list) {
        if (target == 0) {
            //for (int i : list) {
                //System.out.print(i + " ");
            //}
            //System.out.println();
            calculate_self(list);
            return;
        }
        if (target < 0) {
            return;
        }
        for (int i = start; i < nums.length; i++) {
            if (target - nums[i] < 0)
                return;
            list.add(nums[i]);
            combine_self(nums, target - nums[i], i, list);
            list.remove(list.size() - 1);
        }
    }
    
    private void calculate_self(List<Integer> list) {
        Map<Integer, Integer> map_self = new HashMap<>();
        double[] factorial = new double[list.size() + 1];
        factorial[0] = 1;
        map_self.put(list.get(0), 1);
        for (int i = 1; i < list.size(); i++) {
            factorial[i] = factorial[i - 1] * i;
            int count = map_self.getOrDefault(list.get(i), 0);
            map_self.put(list.get(i), count + 1);
        }
        
        if (map_self.size() == 1) {
            sum_self += 1;
            return;
        }
        if (map_self.size() == 2 && map_self.containsValue(1)) {
            sum_self += list.size();
            return;
        }
        
        factorial[list.size()] = factorial[list.size() - 1] * list.size();
        double permutation = factorial[list.size()];
        //System.out.println("permutation = " + permutation);
        for (Map.Entry<Integer, Integer> entry : map_self.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            if (entry.getValue() != 1) {
                permutation /= factorial[entry.getValue()];
            }
        }
        
        //System.out.println("After; permutation = " + permutation);
        sum_self = sum_self + (int) (Math.round(permutation));
        if (((long) permutation) - Math.round(permutation) > 0.9) {
            sum_self++;
        }
    }
	
	// https://discuss.leetcode.com/topic/53553/what-if-negative-numbers-are-allowed-in-the-given-array
    // https://discuss.leetcode.com/topic/52290/java-follow-up-using-recursion-and-memorization
	
	public static void main(String[] args) {
		Combination_Sum_IV combination4 = new Combination_Sum_IV();
		int[] nums = {10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240,250,260,270,280,290,300,310,320,330,340,350,360,370,380,390,400,410,420,430,440,450,460,470,480,490,500,510,520,530,540,550,560,570,580,590,600,610,620,630,640,650,660,670,680,690,700,710,720,730,740,750,760,770,780,790,800,810,820,830,840,850,860,870,880,890,900,910,920,930,940,950,960,970,980,990,111};
		int target = 999;
		System.out.println(combination4.combinationSum4_top_down(nums, target));
//		long preTime = System.currentTimeMillis();
//		int ans = combination4.combinationSum4_self(nums, target);
//		long aftTime = System.currentTimeMillis();
//		long currentTime = aftTime - preTime;
//		System.out.println("Run time : " + currentTime/1000. + " sec");
//		System.out.println(ans);
	}

}
