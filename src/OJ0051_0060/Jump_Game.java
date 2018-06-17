package OJ0051_0060;

public class Jump_Game {
	// https://leetcode.com/problems/jump-game/discuss/21193/How-about-my-solution
	public boolean canJump(int[] A) {
		if (A.length == 0)
			return true;
		
		int maxcan = 0;// the farthest position it can jump
		for (int i = 0; i < A.length; i++) {
			if (maxcan >= A.length - 1)
				return true; // if the farthest postion have passed the end or reached the end
			
			maxcan = Math.max(maxcan, A[i] + i);// update the farthest position
			
			if (maxcan == i)
				return false; // return false when you cannot move any further..
		}
		return true;// not necessary, just a return type...
	}
	
	/*
	 * https://leetcode.com/problems/jump-game/discuss/20932/6-line-java-solution-in-O(n)
	 * 
	 * At each step, we keep track of the furthest reachable index. The nature of the 
	 * problem (eg. maximal jumps where you can hit a range of targets instead of 
	 * singular jumps where you can only hit one target) is that for an index to be 
	 * reachable, each of the previous indices have to be reachable.
	 * Hence, it suffices that we iterate over each index, and If we ever encounter an 
	 * index that is not reachable, we abort and return false. By the end, we will 
	 * have iterated to the last index. If the loop finishes, then the last index is 
	 * reachable.
	 */
	public boolean canJump2(int[] nums) {
		int reachable = 0;
		for (int i = 0; i < nums.length; ++i) {
			if (i > reachable)
				return false;
			reachable = Math.max(reachable, i + nums[i]);
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/jump-game/discuss/20900/Simplest-O(N)-solution-with-constant-space
	 * 
	 * When you are at index i, you can jump to index i + nums[i]
	 * So backwardly, the first goal is jumping to the index, target == len(nums) - 1
	 * If you find a index i, s.t. i + nums[i] >= len(nums) - 1, which means that as 
	 * long as we can somehow greedily find a index j, s.t. j + nums[j] >= i, then we 
	 * can sure we can jump from j to i.
	 * So we simply change target to i, and continuously doing backward !!
	 * 
	 * In the end, since we are in the index 0 at the beginning, and we keep updating 
	 * our goal to i, which means that if we (somehow) can jump to 0, which we already 
	 * did, then we must be able to jump to len(nums) - 1!!
	 * 
	 * Rf : 
	 * https://leetcode.com/articles/jump-game/
	 * leetcode.com/problems/jump-game/discuss/20900/Simplest-O(N)-solution-with-constant-space/20949
	 */
	public boolean canJump_back(int[] A) {
		int n = A.length;
		int last = n - 1;
		for (int i = n - 2; i >= 0; i--) {
			if (i + A[i] >= last)
				last = i;
		}
		return last <= 0;
	}
	
	/*
	 * https://leetcode.com/problems/jump-game/discuss/21196/This-might-be-the-simplest-interview-question-ever...
	 * 
	 * Just update the current max position it can reach.
	 * 
	 * Rf : https://leetcode.com/problems/jump-game/discuss/20917/Linear-and-simple-solution-in-C++
	 */
	public boolean canJump3(int[] A) {
		int max = 0;
		for (int i = 0; i <= max && i < A.length; i++) {
			max = Math.max(A[i] + i, max);
		}
		return max >= A.length - 1;
	}
	
	// by myself
	public boolean canJump_self(int[] nums) {
        if (nums.length == 1)
            return true;
        
        int zero = -1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (zero == -1 && nums[i] == 0) {
                zero = i;
            }
            
            if (zero != -1) {
                if (nums[i] > zero - i) {
                    zero = -1;
                }
            }
        }
        
        if (zero == -1)
            return true;
        else
            return false;
    }
	
	/*
	 * https://leetcode.com/problems/jump-game/discuss/21068/My-2ms-Java-solution-O(n)-complexity-and-O(1)-space-easy-to-understand
	 * 
	 * Finding the furthest point that we can jump to. Initialize nums[0] to be the 
	 * furthest position that we can jump to, then loop from nums[1] to that furthest 
	 * position to find if we can jump further than that using each element. Once we 
	 * find a value that helps us jump further than the current furthest position, set 
	 * that value to be the new furthest position. When our furthest position is 
	 * bigger than the last position of the array, exits the loop and returns true. 
	 * If the loop exits before that, it means that we cannot reach the last position, 
	 * return false.
	 */
	public boolean canJump_modify_furthest(int[] nums) {
		if (nums == null)
			return false;
		if (nums.length == 1)
			return true;

		int furthest_pos = nums[0];
		for (int i = 1; i <= furthest_pos; ++i) {
			if (furthest_pos >= nums.length - 1) {
				return true;
			}
			
			int cur_furthest_pos = i + nums[i];
			if (cur_furthest_pos > furthest_pos) {
				furthest_pos = cur_furthest_pos;
			}
		}

		return false;
	}
	
	/*
	 * https://leetcode.com/problems/jump-game/discuss/21185/Another-way-of-looking-at-the-problem
	 * 
	 * 0 is like a trap.
	 * Anytime you fall-in 0, you can't jump no more (except the last one which you 
	 * are already at target).
	 * So first, find those traps from start. After we find one, we have to go back to 
	 * test if this trap is leap-able. This is not efficient.
	 * 
	 * If we search from back, whenever a trap is found, we can conveniently convert 
	 * searching for trap problem to searching for leap-able problem. No need to go 
	 * back. So one scan, O(n).
	 * 
	 * Other code:
	 * https://leetcode.com/problems/jump-game/discuss/20944/Java-98-Percentile-Solution
	 */
	public boolean canJump_back2(int[] A) {
		for (int i = A.length - 2; i >= 0; i--) {
			if (A[i] == 0) {
				// start search
				int zeroIndex = i;
				for (i = i - 1; i >= 0; i--) // keep using same i to continue searching!
				{
					if (A[i] > zeroIndex - i) // we can overcome this trap!
						break;
				}
				if (i == -1) // searched to end and no possible leap
					return false;
			}
		}
		return true;
	}

}
