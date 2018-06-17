package OJ0041_0050;

public class Jump_Game_II {
	/*
	 * https://leetcode.com/problems/jump-game-ii/discuss/18014/Concise-O(n)-one-loop-JAVA-solution-based-on-Greedy
	 * 
	 * Let's say the range of the current jump is [curBegin, curEnd], curFarthest is 
	 * the farthest point that all points in [curBegin, curEnd] can reach. Once the 
	 * current point reaches curEnd, then trigger another jump, and set the new 
	 * curEnd with curFarthest, then keep the above steps.
	 * 
	 * Rf :
	 * leetcode.com/problems/jump-game-ii/discuss/18023/Single-loop-simple-java-solution/17989
	 * 
	 * Other code:
	 * https://leetcode.com/problems/jump-game-ii/discuss/18093/Sharing-My-AC-Java-Solution
	 * https://leetcode.com/problems/jump-game-ii/discuss/18023/Single-loop-simple-java-solution
	 */
	public int jump(int[] A) {
		int jumps = 0, curEnd = 0, curFarthest = 0;
		for (int i = 0; i < A.length - 1; i++) {
			curFarthest = Math.max(curFarthest, i + A[i]);
			if (i == curEnd) {
				jumps++;
				curEnd = curFarthest;
			}
		}
		return jumps;
	}
	
	/*
	 * https://leetcode.com/problems/jump-game-ii/discuss/18213/O(n)-runtime-O(1)-Space-Java-Solution
	 * 
	 * We keep two pointers low and high that record the current range of the starting 
	 * nodes. Each time after we make a move, update low to be high + 1 and high to be 
	 * the farthest index that can be reached in 1 move from the current [low, high].
	 * 
	 * Rf : https://leetcode.com/problems/jump-game-ii/discuss/18019/10-lines-C++-(16ms)-Python-BFS-Solutions-with-Explanations
	 */
	public int jump_low_high(int[] A) {
		int step = 0;
		int low = 0;
		int high = 0;
		while (high < A.length - 1) {
			int preLow = low;
			int preHigh = high;
			
			for (int t = preLow; t <= preHigh; t++)
				high = Math.max(t + A[t], high);
			
			low = preHigh + 1;
			step++;
		}
		return step;
	}
	
	/*
	 * https://leetcode.com/problems/jump-game-ii/discuss/18028/O(n)-BFS-solution
	 * 
	 * nodes in level i are all the nodes that can be reached in i-1th jump.
	 * 
	 * Rf : https://leetcode.com/problems/jump-game-ii/discuss/18152/Java-Solution-with-explanation
	 */
	public int jump_BFS(int[] A) {
		if (A.length < 2)
			return 0;
		
		int level = 0, currentMax = 0, i = 0, nextMax = 0;
		while (currentMax - i + 1 > 0) { // nodes count of current level>0
			level++;
			for (; i <= currentMax; i++) { // traverse current level , and update the max reach of next level
				nextMax = Math.max(nextMax, A[i] + i);
				if (nextMax >= A.length - 1)
					return level; // if last element is in level+1, then the min jump=level
			}
			currentMax = nextMax;
		}
		return 0;
	}
	
	// TLE
	public int jump_self_dp(int[] nums) {
        int[] step = new int[nums.length];
        for (int i = nums.length - 2; i >= 0; i--) {
            step[i] = nums.length + 1;
            int last = Math.min(i + nums[i], nums.length - 1);
            for (int j = i + 1; j <= last; j++) {
                if (step[j] != nums.length + 1) {
                    step[i] = Math.min(step[j] + 1, step[i]);
                }
            }
        }     
        return step[0];
    }

}
