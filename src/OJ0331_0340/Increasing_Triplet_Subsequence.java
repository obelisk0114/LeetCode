package OJ0331_0340;

public class Increasing_Triplet_Subsequence {
	
	/*
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79000/My-accepted-JAVA-solution-for-this-question-only-7-lines-clear-and-concise.
	 * 
	 * The main idea is keep two values when check all elements in the array: the 
	 * minimum value min until now and the second minimum value secondMin from the 
	 * minimum value's position until now. Then if we can find the third one that 
	 * larger than those two values at the same time, it must exists the triplet 
	 * subsequence and return true.
	 * 
	 * What need to be careful is: we need to include the condition that some value 
	 * has the same value with minimum number, otherwise this condition will cause 
	 * the secondMin change its value.
	 * 
	 * We don't need the 'first'. Since a third number in the increasing subsequence 
	 * is greater than 'second', it is, of course, greater than some 'first' which 
	 * appears before 'second'. We don't care whether that 'first' is identical to 
	 * the 'min' or not. In this way, we don't have to keep record of the 'first'
	 * 
	 * min = so far best candidate of end element of a one-cell subsequence to form a 
	 *       triplet subsequence
	 * secondMin = so far best candidate of end element of a two-cell subsequence to 
	 *             form a triplet subsequence
	 * 
	 * There is always a number before secondMin that is less than secondMin, which 
	 * might or might not be min
	 * 
	 * Rf :
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/78993/Clean-and-short-with-comments-C++/83836
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/78993/Clean-and-short-with-comments-C++/193389
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79004/Concise-Java-solution-with-comments./216801
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79000/My-accepted-JAVA-solution-for-this-question-only-7-lines-clear-and-concise./83867
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/78993/Clean-and-short-with-comments-C++/83827
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79004/Concise-Java-solution-with-comments./247643
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79004/Concise-Java-solution-with-comments.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/353613/short-and-clean-java-solution-beats-100
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79071/Clean-Java-Solution-with-Clear-Explanation
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79002/Simple-Java-Solution...Easy-to-understand!!!!
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/407963/Simple-Java-solution-using-3-pointers-O(n)-for-slow-learners-like-myself
	 */
	public boolean increasingTriplet(int[] nums) {
		int min = Integer.MAX_VALUE, secondMin = Integer.MAX_VALUE;
		for (int num : nums) {
			if (num <= min)   // "=" is used to prevent change secondMin
				min = num;
			// else if (num <= secondMin) ¤]¥i¥H
			else if (num < secondMin)
				secondMin = num;
			else if (num > secondMin)
				return true;
		}
		return false;
	}
	
	// https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79085/My-java-solution-works-for-not-just-triplet
	public boolean increasingTriplet_k(int[] nums) {
		int k = 3;
		int[] small = new int[k - 1];
		for (int i = 0; i < small.length; i++) {
			small[i] = Integer.MAX_VALUE;
		}
		
		for (int num : nums) {
			int i = 0;
			while (i < small.length && small[i] < num) {
				i++;
			}
			
			if (i < small.length) {
				small[i] = num;
			} 
			else {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79018/Java-1ms-Clean-solution
	 * 
	 * Use two variables to store the value with increasing subsequence of length 1 
	 * and length 2, respectively. Keep updating the two variables if we get to a 
	 * smaller candidate ending up with the same length.
	 * 
	 * Rf : https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79114/*Java*-7-lines-O(n)-time-and-O(1)-space
	 */
	public boolean increasingTriplet2(int[] nums) {
		if (nums == null || nums.length < 3)
			return false;
		
		int min = Integer.MAX_VALUE, max = Integer.MAX_VALUE;
		int i = 0;

		while (i < nums.length) {
			if (nums[i] > max) {
				return true;
			} 
			else if (nums[i] > min) {
				max = nums[i];
			} 
			else {
				min = nums[i];
			}
			
			i++;
		}
		return false;
	}
	
	// https://leetcode.com/problems/increasing-triplet-subsequence/discuss/534266/Concise-Java-Solution-explained-%3A%3A-Better-than-100-submissions
	public boolean increasingTriplet3(int[] nums) {
		int num1 = Integer.MAX_VALUE;
		int num2 = Integer.MAX_VALUE;
		for (int num : nums) {
			if (num < num1)
				num1 = num;
			if (num > num1)
				num2 = Math.min(num2, num);
			if (num > num2)
				return true;
		}
		return false;
	}
	
	// by myself
	public boolean increasingTriplet_self(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        int first = nums[0];
        int second = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < first) {
                if (first == second) {
                    first = nums[i];
                    second = nums[i];
                }
                else {
                    first = nums[i];
                }
            }
            else if (nums[i] > first && first == second) {
                second = nums[i];
            }
            else if (nums[i] > first && nums[i] < second) {
                second = nums[i];
            }
            else if (nums[i] > second) {
                return true;
            }
        }
        return false;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/78995/Python-Easy-O(n)-Solution
     * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/78997/Generalization-in-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/78993/Clean-and-short-with-comments-C%2B%2B
     * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79053/My-way-to-approach-such-a-problem.-How-to-think-about-it-Explanation-of-my-think-flow.
     * https://leetcode.com/problems/increasing-triplet-subsequence/discuss/79023/Just-a-simplified-version-of-patient-sort.
     */

}
