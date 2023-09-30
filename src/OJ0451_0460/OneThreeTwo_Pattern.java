package OJ0451_0460;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Stack;

/*
 * test cases:
 * [1,0,1,-4,-3]
 */

public class OneThreeTwo_Pattern {
	/*
	 * The following class and function are from this link.
	 * https://leetcode.com/problems/132-pattern/discuss/94077/Java-O(n)-solution-using-stack-in-detail-explanation
	 * 
	 * stack.peek() �� min �O global �̤p�Amax �O min ����̤j������
	 * �]���s������ (num) �Y�� stack.peek().min �p�A��ܥi�H�q�L�}�l�s���u�q�A�ҥH push (num, num)
	 * 
	 * �_�h stack.pop() �X�ӡA�N�o�� pop �X�� Pair �w�� last
	 *   �Y num < last.max �F������Areturn true
	 *   �_�h num �i�H�X�֨� last �̭��A�P�ɽT�{�e�������ǽu�q�Q�]�t
	 *   �]�� last.min �O global �̤p�A�ڭ̥u�n�ˬd�� stack �̭� Pair �� max < num�A�o�� Pair �N�Q�]�t
	 *     stack ���_ pop �Q�]�t�� Pair
	 *     �Y���j�鰱��A��� stack.peek().max > num
	 *     ���p���ɡAstack.peek().min < num�A��ܧڭ̧�� 132 pattern�Areturn true
	 *     �_�h�N��s�᪺ last push �i�h stack
	 * 
	 * ------------------------------------------------------------
	 * 
	 * �� stack �O�� (min, max)�A�C�� (min, max) �u�q���S���涰�C
	 * stack ���� min �O sorted�Astack.peek().min �O�̤p��
	 * 
	 * �� stack ���ŮɡApush (num, num)
	 * �� stack.peek().min > num �ɡApush (num, num)
	 * 
	 * �� stack.peek().min <= num�A��� num �i�H�Q�X�֨� stack.peek()�A�ҥH stack pop
	 * �� num < last.max�A�ڭ̧�� 132 pattern
	 * �� num >= last.max�A�N num �X�֨��e pop �X���u�q (�O���� last)�Alast.max = num
	 * ���O�o�˦��i��|�M stack �̪���L�u�q���ͥ涰�A�]���n�ˬd
	 * 
	 * �]���ڭ̥u���b stack.peek().min > num �~ push �s���u�q�A�]�� last.min < stack.peek().min
	 * �Y stack.peek().max <= last.max�A�o�q�Q last ����]�t�Astack pop
	 * �Y stack.peek().max > last.max�A�ڭ̧�� 132 pattern
	 * 
	 * �B�z������A�N last push �i stack
	 * 
	 * Use a stack to keep track of previous min-max intervals.
	 * 
	 * If stack is empty:
	 * + push a new Pair of num into stack
	 * 
	 * If stack is not empty:
	 * + if num < stack.peek().min, push a new Pair of num into stack
	 * + if num >= stack.peek().min, we first pop() out the peek element (as "last")
	 *   - if num < last.max, we are done, return true;
	 *   - if num >= last.max, we merge num into last, which means last.max = num.
	 *     Once we update last, if stack is empty, we just push back last.
	 *     However, If stack is not empty, the updated last might:
	 *     # Entirely covered stack.peek(), i.e. last.min < stack.peek().min (which is 
	 *       always true) && last.max >= stack.peek().max, in which case we keep 
	 *       popping out stack.peek().
	 *     # Form a 1-3-2 pattern, we are done ,return true
	 * 
	 * So at any time in the stack, non-overlapping Pairs are formed in descending 
	 * order by their min value, which means the min value of peek element in the 
	 * stack is always the min value globally.
	 * 
	 * 1. the stack is a collection of disjoint segments
	 * 2. the last segment is always connected with the current number
	 * 3. each segment is always min-max
	 * 
	 * Only two variables cannot easily maintain the position information (as you 
	 * update min and max, the previous ones lost).
	 * 
	 * So we need to keep "multiple min-max pairs" here. We only need to fetch those 
	 * intervals from the List sequentially, and Stack is a perfect LIFO container.
	 * 
	 * BTW, for all Stack questions, we can always use List, but Stack makes it easier 
	 * to code and maintain.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/132-pattern/discuss/94077/Java-O(n)-solution-using-stack-in-detail-explanation/98379
	 * https://leetcode.com/problems/132-pattern/discuss/94077/Java-O(n)-solution-using-stack-in-detail-explanation/98387
	 * 
	 * Other code:
	 * https://leetcode.com/problems/132-pattern/discuss/94077/Java-O(n)-solution-using-stack-in-detail-explanation/98385
	 */
	class Pair {
		int min, max;

		public Pair(int min, int max) {
			this.min = min;
			this.max = max;
		}
	}

	public boolean find132pattern_min_max(int[] nums) {
		Stack<Pair> stack = new Stack<>();
		for (int n : nums) {
			if (stack.isEmpty() || n < stack.peek().min) {
				stack.push(new Pair(n, n));				
			}
			else if (n > stack.peek().min) {
				Pair last = stack.pop();
				
				if (n < last.max) {
					return true;					
				}
				else {
					last.max = n;
					while (!stack.isEmpty() && n >= stack.peek().max) {
						stack.pop();						
					}
					
					// At this time, n < stack.peek().max (if stack not empty)
					if (!stack.isEmpty() && stack.peek().min < n) {
						return true;						
					}
					
					stack.push(last);
				}
			}
		}
		return false;
	}

	/*
	 * https://leetcode.com/problems/132-pattern/solution/312273
	 * 
	 * TreeSet which contains all potential "twos", we need scan from left to get the 
	 * minimum/"one" on the left, then scan from right to find three > one, 
	 * three > two by TreeSet.floor(three).
	 * 
	 * O(n log n)
	 */
	public boolean find132pattern_TreeSet(int[] nums) {
		if (nums == null || nums.length == 0)
			return false;
		
		// ones[i] = minimum from index 0 to index i-1
		int[] ones = new int[nums.length];
		ones[0] = nums[0];
		for (int i = 1; i < nums.length; i++) {
			ones[i] = Math.min(nums[i - 1], ones[i - 1]);
		}
		
		// Time complexity: lg1 + lg2 + lg3 +... + lg(n-1) = lg(n!) < nlgn
		TreeSet<Integer> twoSet = new TreeSet<>();
		
		// scan from right to find one < three and two < three and one < two
		for (int i = nums.length - 1; i > 0; i--) {
			int one = ones[i];
			int three = nums[i];
			
			// one < three
			if (one < three) {
				// two < three
				Integer two = twoSet.floor(three - 1);
				
				if (two != null && one < two)
					return true;
			}
			
			// After we take nums[i] as three, nums[i] will be added to twoSet
			twoSet.add(nums[i]);
		}
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/132-pattern/discuss/906971/Brute-Force-to-Optimal-Greedy-Stack-O(n)
	 * https://www.youtube.com/watch?v=8nx5dxFuvLo
	 * 
	 * O(n)
	 * 
	 * �Τ@�� min value array ���x�s�̨Ϊ� "1"
	 * 
	 * �b i ����|�O (nums[j] <= nums[k], j < k)�A�Y�����}�o���Ǫ��Ʀr�A��ܦ� 132 pattern
	 * �ѫ᩹�e�A�Y�{�b�O nums[j]�A�ڭ̥i�H�ϥ� j + 1 ���᪺���G
	 * �]���Τ@�� stack ���x�s�i�઺ "2"
	 * 
	 * �����A stack[top] <= min[j] �N���_ pop�A�o�� stack �N�u�ѤU�ŦX����
	 * �Y nums[j] > stack[top] ��ܧ��F
	 * �M�� nums[j] <= stack[top]�A��� nums[j] �O�i�઺ "2"�A�N nums[j] push �i�h
	 * 
	 * stack ���w�O�ƧǦn�� (stack[top] �̤p)�A�]��
	 * 1. min[i] <= min[j], when i > j�A�ҥH pop �����٬O sorted
	 * 2. nums[j] <= stack[top]�A�M��~ push nums[j]
	 * 
	 * The preprocessing is to just find the best nums[i] value corresponding to every 
	 * nums[j] value. We find the minimum element found till the jth element which 
	 * acts as the nums[i] for the current nums[j]. min[j] refers to the best nums[i] 
	 * value for a particular nums[j].
	 * 
	 * We traverse back from the end of the nums array to find the nums[k]'s. Suppose, 
	 * we keep a track of the nums[k] values which can potentially satisfy the 132 
	 * criteria for the current nums[j]. 
	 * 
	 * Once it is ensured that the elements left for competing for the nums[k] are all 
	 * greater than min[j](or nums[i]), our only task is to ensure that it should be 
	 * less than nums[j]. Now, the best element from among the competitors, for 
	 * satisfying this condition will be the minimum one from out of these elements.
	 * 
	 * If this element, nums[k], nums[k] < nums[j], we've found a 132 pattern. If 
	 * not, no other element will satisfy this criteria, since they are all greater 
	 * than or equal to nums[k] and thus greater than or equal to nums[j] as well.
	 * 
	 * We maintain a stack on which these potential nums[k]'s satisfying the 132 
	 * criteria lie in a descending order(minimum element on the top). We need not 
	 * sort these elements on the stack, but they'll be sorted automatically.
	 * 
	 * After creating a min array, we start traversing the nums[j] array in a backward 
	 * manner. Let's say, we are currently at the jth element and let's also assume 
	 * that the stack is sorted. Firstly, we check if nums[j] > min[j]. If not, we 
	 * continue with the (j-1)th element and the stack remains sorted. Second, we 
	 * check if stack[top] > min[j]. If not, we keep on popping the elements from the 
	 * top of the stack till we find an element, stack[top] such that, 
	 * stack[top] > min[j](or stack[top] > nums[i]).
	 * 
	 * Once the popping is done, we're sure that all the elements pending on the 
	 * stack are greater than nums[i] and are the potential candidates for nums[k] 
	 * satisfying the 132 criteria. 
	 * 
	 * Since, in the min array, min[p] <= min[q], for every p > q, these popped 
	 * elements also satisfy stack[top] <= min[k], for all 0 <= k < j. Even after 
	 * doing the popping, the stack remains sorted.
	 * 
	 * After the popping is done, we've got the minimum element from amongst all the 
	 * potential nums[k]'s on the top of the stack. We can check if it is less than 
	 * nums[j] to satisfy the 132 criteria (we've checked stack[top] > nums[i]). If 
	 * not, we know that for the current j, nums[j] > min[j]. Thus, the element 
	 * nums[j] could be a potential nums[k] value, for the preceding nums[i]'s. 
	 * Thus, we push it over the stack.
	 * 
	 * We need to push this element nums[j] on the stack only when it didn't satisfy 
	 * stack[top] < nums[j]. Thus, nums[j] <= stack[top]. Thus, even after pushing 
	 * this element on the stack, the stack remains sorted. Thus, we've seen by 
	 * induction, that the stack always remains sorted.
	 * 
	 * Also, note that in case nums[j] <= min[j], we don't push nums[j] onto the stack. 
	 * This is because this nums[j] isn't greater than even the minimum element lying 
	 * towards its left and thus can't act as nums[k] in the future.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/132-pattern/solution/
	 * https://leetcode.com/problems/132-pattern/discuss/484439/stack-O(n)-with-explanation
	 */
	public boolean find132pattern_2_pass(int[] nums) {
        int n = nums.length;
        
        int[] min = new int[n];
        min[0] = nums[0];
        for(int i = 1; i < n; i++) 
            min[i] = Math.min(min[i - 1], nums[i]);
        
        Stack < Integer > stack = new Stack < > ();
        for (int j = nums.length - 1; j >= 0; j--) {
            if (nums[j] > min[j]) {
                while (!stack.isEmpty() && stack.peek() <= min[j])
                    stack.pop();
                
                if (!stack.isEmpty() && stack.peek() < nums[j])
                    return true;
                
                stack.push(nums[j]);
            }
        }
        return false;
    }
	
	/*
	 * https://leetcode.com/problems/132-pattern/discuss/906789/Short-Java-O(N)-Solution-with-Detailed-Explanation-and-Sample-Test-Case-or-Stack-or-100
	 * 
	 * nums[i] �O "1"�A�ڭ̭n�� "2" (second) �ɥi��j
	 * 
	 * �Y nums[i] < second ��ܧ�� 132 pattern
	 * �_�h�N nums[i] ��@�i�઺ "3"�A�ڭ̥u�ݭn�ˬd��{�b�� "2" �j�������A�]���ϥ� stack
	 * 
	 * �N stack[top] �� nums[i] �p���Ʀr�� pop �X�Ӱ��� "2"�C�]���ѫ᩹�e�A�ҥH "2" ���w�b nums[i] �k��
	 * �Y stack[top] >= nums[i]�A��ܦb�k�䦳��j�� "2" �M�N�L pop �X�Ӫ� "3"
	 * 
	 * �̫�A�N nums[i] push �i�h�Astack �̭��O "3"
	 * 
	 * stack ���w�O�ƧǦn�� (stack[top] �̤p)�A�]�� stack[top] < nums[i] �N�Q pop
	 * �M��~ push nums[i]
	 * 
	 * We are only storing one item in the stack, which is our ideal candidate for s2 
	 * (number that needs to be the largest). If we find a number that is bigger than 
	 * what we thought was our ideal candidate for s2; we pop out our stack and store 
	 * the value in s3 (mid value number), then we store the new ideal candidate for 
	 * s2 in the stack. On the next ith iteration, if nums[i] is actually less than 
	 * s3, then we are done!
	 * 
	 * If we fix the peak, i.e. 3 in the 132 pattern, then we can determine if any 
	 * numbers on its left and right satisfy the given pattern. Our stack will take 
	 * care of the 32 pattern and then we will iterate over the array to find if any 
	 * number satisfies the 1 pattern.
	 * 
	 * stack[top] is containing the highest number so far, i.e. 3 and second is 
	 * containing the second highest number after the highest number, i.e. 2. So, this 
	 * satisfies the 32 pattern. Now, we will just keep updating second and stack[top] 
	 * when we encounter a number which is greater than the highest number.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/132-pattern/discuss/94071/Single-pass-C++-O(n)-space-and-time-solution-(8-lines)-with-detailed-explanation./255300
	 * https://leetcode.com/problems/132-pattern/discuss/94089/Java-solutions-from-O(n3)-to-O(n)-for-%22132%22-pattern-(updated-with-one-pass-slution)
	 * https://leetcode.com/problems/132-pattern/discuss/94071/Single-pass-C++-O(n)-space-and-time-solution-(8-lines)-with-detailed-explanation./98352
	 * 
	 * Other code:
	 * https://leetcode.com/problems/132-pattern/discuss/425027/Java-simple-and-clean-O(n)-solution-similar-idea-with-%22Next-Greater-Element%22
	 */
	public boolean find132pattern_1_pass(int[] nums) {
		Stack<Integer> stack = new Stack<>();
		int second = Integer.MIN_VALUE;
		for (int i = nums.length - 1; i >= 0; i--) {
			if (nums[i] < second)
				return true;
			
			while (!stack.isEmpty() && nums[i] > stack.peek())
				second = stack.pop();
			
			stack.push(nums[i]);
		}
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/132-pattern/discuss/94076/Share-my-easy-and-simple-solution
	 * 
	 * Find peak and bottom
	 * For every [bottom, peak], find if there is one number bottom < number < peak.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/132-pattern/discuss/94076/Share-my-easy-and-simple-solution/98371
	 */
	public boolean find132pattern_better_brute_force(int[] nums) {
		if (nums.length < 3)
			return false;

		int i = 0;
		while (i < nums.length - 1) {
			while (i < nums.length - 1 && nums[i] >= nums[i + 1])
				i++;

			// i is lowest now
			int j = i + 1;
			while (j < nums.length - 1 && nums[j] <= nums[j + 1])
				j++;

			// j is highest now
			int k = j + 1;
			while (k < nums.length) {
				if (nums[k] > nums[i] && nums[k] < nums[j])
					return true;

				k++;
			}

			i = j + 1;
		}
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/132-pattern/solution/
	 * Approach 5: Binary Search
	 * 
	 * We can use a stack on which these potential nums[k]'s satisfying the 132 
	 * criteria lie in a descending order(minimum element on the top). We need not 
	 * sort these elements on the stack, but they'll be sorted automatically.
	 * 
	 * We can also note that when we reach the index j while scanning backwards for 
	 * finding nums[k], the stack can contain at most (n - j - 1) elements. Here, n 
	 * refers to the number of elements in nums array. This is the same number of 
	 * elements which lie beyond the jth index in nums array.
	 * 
	 * These elements lying beyond the jth index won't be needed in the future. Thus, 
	 * we can make use of this space in nums array instead of using a separate stack. 
	 * We can use this space to pop and push potential nums[k].
	 * (if stack[top] <= min[j], pop) => (not 132 pattern, push)
	 * 
	 * Since, we've got an array for storing the potential nums[k] values now, we need 
	 * not do the popping process for a min[j] to find an element just larger than 
	 * min[j] from amongst these potential values. Instead, we can make use of Binary 
	 * Search to directly find an element, which is just larger than min[j] in the 
	 * required interval, if it exists. If such an element is found, we can compare it 
	 * with nums[j] to check the 132 criteria. Otherwise, we continue the process.
	 */
	public boolean find132pattern_binary_search(int[] nums) {
        if (nums.length < 3)
            return false;
        
        int[] min = new int[nums.length];
        min[0] = nums[0];
        for (int i = 1; i < nums.length; i++)
            min[i] = Math.min(min[i - 1], nums[i]);
        
        // k �O array ��b������ stack �� top�Anums.length - 1 �O bottom
        // �]�� stack �O sorted�A�ҥH�i�H�� binary search�C��X�s�� top
        for (int j = nums.length - 1, k = nums.length; j >= 0; j--) {
            if (nums[j] > min[j]) {
            	// �u�� k ���᪺�A���� pop
                k = Arrays.binarySearch(nums, k, nums.length, min[j] + 1);
                
                if (k < 0)
                    k = -1 - k;
                
                if (k < nums.length && nums[k] < nums[j])
                    return true;
                
                // nums[k] >= nums[j]
                // �N nums[j] push �i�h�C�]�� k �O top�A�ҥH�n�� --k
                // k = j; �]�| accept�A��]����
                nums[--k] = nums[j];
            }
        }
        return false;
    }
	
	/*
	 * https://leetcode.com/problems/132-pattern/solution/
	 * Approach 3: Searching Intervals
	 * 
	 * keep (nums[i], nums[j]) as much as possible to find nums[k].
	 * 
	 * The best qualifiers to act as the nums[i], nums[j] pair, to maximize the range 
	 * nums[i], nums[j], at any instant, while traversing the nums array, will be the 
	 * points at the endpoints of a local rising slope. Thus, once we've found such 
	 * points, we can traverse over the nums array to find a nums[k] satisfying the 
	 * given 132 criteria.
	 * 
	 * To find these points at the ends of a local rising slope, we can traverse over 
	 * the given nums array. While traversing, we can keep a track of the minimum 
	 * point found after the last peak (nums[s]).
	 * 
	 * whenever we encounter a falling slope, say, at index i, we know, that nums[i-1] 
	 * was the endpoint of the last rising slope found. Thus, we can scan over the k 
	 * indices(k >= i), to find a 132 pattern.
	 * 
	 * But, instead of traversing over nums to find a k satisfying the 132 pattern for 
	 * every such rising slope, we can store this range (nums[s], nums[i-1])(acting 
	 * as (nums[i], nums[j])) in, say an intervals array.
	 * 
	 * While traversing over the nums array to check the rising/falling slopes, 
	 * whenever we find any rising slope, we can keep adding the endpoint pairs to 
	 * this intervals array. At the same time, we can also check if the current 
	 * element falls in any of the ranges found so far. If so, this element satisfies 
	 * the 132 criteria for that range.
	 * 
	 * (worst case e.g.[5 6 4 7 3 8 2 9]).
	 */
	public boolean find132pattern_intervals(int[] nums) {
        List < int[] > intervals = new ArrayList < > ();
        int i = 1, s = 0;
        while (i < nums.length) {
            if (nums[i] < nums[i - 1]) {
                if (s < i - 1)
                    intervals.add(new int[] {nums[s], nums[i - 1]});
                
                s = i;
            }
            
            for (int[] a: intervals)
                if (nums[i] > a[0] && nums[i] < a[1])
                    return true;
            
            i++;
        }
        return false;
    }
	
	/*
	 * https://leetcode.com/problems/132-pattern/discuss/906971/Brute-Force-to-Optimal-Greedy-Stack-O(n)
	 * 
	 * Let's assume we have index j fixed.
	 * 
	 * Once the first two numbers nums[i] and nums[j] are fixed, we are up to find the 
	 * third number nums[k] which will be within the range (nums[i], nums[j]) (the two 
	 * boundaries are exclusive). The larger the range is, the more likely there will 
	 * be a number "falling into" it. Therefore we need to choose index i which will 
	 * maximize the range (nums[i], nums[j]). 
	 * 
	 * Since the upper bound nums[j] is fixed, this is equivalent to minimizing the 
	 * lower bound nums[i]. Thus it is clear i should be the index of the minimum 
	 * element of the subarray nums[0, j) (left inclusive, right exclusive).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/132-pattern/discuss/94089/Java-solutions-from-O(n3)-to-O(n)-for-%22132%22-pattern-(updated-with-one-pass-slution)
	 */
	public boolean find132pattern_fix_j(int[] nums) {
		int n = nums.length;
		int leftMin = nums[0];
		for (int j = 1; j < n - 1; j++) {
			for (int k = j + 1; k < n; k++) {
				if (nums[k] > leftMin && nums[j] > nums[k])
					return true;
			}
			
			leftMin = Math.min(nums[j], leftMin);
		}
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/132-pattern/discuss/94133/Simple-java-accepted-well-explained-O(n2)-solution
	 * 
	 * nums[i] �O�̤p���A���L nums[j] <= nums[i]
	 * 
	 * bigger �O�̤j���A�� nums[j] >= bigger �N�n��s bigger
	 * �Y bigger �S�Q nums[j] ��s�A��� nums[j] < bigger
	 * => �]�� nums[i] < bigger > nums[j]�A�]�� bigger > nums[i]�A�_�h�|�Q���L
	 */
	public boolean find132pattern_bigger(int[] nums) {
		if (nums == null || nums.length < 3) {
			return false;
		}

		for (int i = 0; i < nums.length - 2; i++) {
			int bigger = nums[i];
			for (int j = i + 1; j < nums.length; j++) {
				// 1. We don't care about numbers
				// less than a[i]
				if (nums[j] <= nums[i])
					continue;

				// 2. If num is greater than bigger
				// then update bigger
				if (nums[j] >= bigger) {
					bigger = nums[j];
				} 
				else {
					// Now this number is greater than nums[i]
					// see 1. and less than bigger, see 2.
					return true;
				}
			}
		}
		return false;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/132-pattern/discuss/206575/previous-greater-element-(-stack-O(n)-no-reverse)
     * https://leetcode.com/problems/132-pattern/discuss/906876/Python-O(n)-solution-with-decreasing-stack-explained
     * https://leetcode.com/problems/132-pattern/discuss/94081/10-line-Python-Solution
     * https://leetcode.com/problems/132-pattern/discuss/94086/Python-solution-in-O(nlogn)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/132-pattern/discuss/94071/Single-pass-C%2B%2B-O(n)-space-and-time-solution-(8-lines)-with-detailed-explanation.
     * https://leetcode.com/problems/132-pattern/discuss/166953/Easy-and-concise-C%2B%2B-solution-using-a-stack-with-explanation-VERY-EASY-to-understand
     * https://leetcode.com/problems/132-pattern/discuss/902824/C%2B%2B-Stack-O(N)-Solution-Explained
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/132-pattern/discuss/445517/JavaScript-Solution-w-Explanation-(Stack)
	 */

}
