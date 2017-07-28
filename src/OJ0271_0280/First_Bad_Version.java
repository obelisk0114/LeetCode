package OJ0271_0280;

public class First_Bad_Version {
	/*
	 * https://discuss.leetcode.com/topic/26272/o-lgn-simple-java-solution
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/38135/a-good-warning-to-me-to-use-start-end-start-2-to-avoid-overflow
	 * https://discuss.leetcode.com/topic/35523/do-large-inputs-really-make-sense
	 * https://leetcode.com/articles/first-bad-version/
	 */
	public int firstBadVersion(int n) {
        int start = 1;
        int end = n;
        while (start < end) {
            int mid = start + (end - start)/2;     // It will not overflow
            if (isBadVersion(mid)) {
                end = mid;
            }
            else {
                start = mid + 1;
            }
        }
        return start;
    }
	
	// https://discuss.leetcode.com/topic/23680/java-simple-clean
	
	boolean isBadVersion(int version) {
		/* The isBadVersion API is defined in the parent class VersionControl. */
		return true;
	}

}
