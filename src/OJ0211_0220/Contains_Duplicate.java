package OJ0211_0220;

import java.util.HashSet;

public class Contains_Duplicate {
	// https://discuss.leetcode.com/topic/14730/possible-solutions
	public boolean containsDuplicate(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for (int i : nums) {
            if (!set.add(i))
                return true;
        }
        return false;
    }
	
	// https://discuss.leetcode.com/topic/30339/3ms-java-solution-with-bit-manipulation

}
