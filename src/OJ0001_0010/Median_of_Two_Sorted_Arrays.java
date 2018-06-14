package OJ0001_0010;

public class Median_of_Two_Sorted_Arrays {
	/*
	 * leetcode.com/problems/median-of-two-sorted-arrays/discuss/2481/Share-my-O(log(min(mn))-solution-with-explanation/2952
	 * 
	 *       left_A             |        right_A
	 * A[0], A[1], ..., A[i-1]  |  A[i], A[i+1], ..., A[m-1]
	 * 
	 * Since A has m elements, so there are m+1 kinds of cutting( i = 0 ~ m ). And we 
	 * know: len(left_A) = i, len(right_A) = m - i . Note: when i = 0 , left_A is empty, 
	 * and when i = m , right_A is empty.
	 * 
	 *       left_B             |        right_B
	 * B[0], B[1], ..., B[j-1]  |  B[j], B[j+1], ..., B[n-1]
	 * 
	 *       left_part          |        right_part
	 * A[0], A[1], ..., A[i-1]  |  A[i], A[i+1], ..., A[m-1]
	 * B[0], B[1], ..., B[j-1]  |  B[j], B[j+1], ..., B[n-1]
	 * 
	 * (1) i + j == m - i + n - j (or: m - i + n - j + 1)
	 *     if n >= m, we just need to set: i = 0 ~ m, j = (m + n + 1)/2 - i
	 * (2) B[j-1] <= A[i] and A[i-1] <= B[j]
	 * 
	 * Searching i in [0, m], to find an object i that:
	 * B[j-1] <= A[i] and A[i-1] <= B[j], ( where j = (m + n + 1)/2 - i )
	 * 
	 * median = max(A[i-1], B[j-1]) (when m + n is odd)
	 * or (max(A[i-1], B[j-1]) + min(A[i], B[j]))/2 (when m + n is even)
	 * 
	 * m <= n, i < m ==> j = (m+n+1)/2 - i > (m+n+1)/2 - m >= (2*m+1)/2 - m >= 0    
	 * m <= n, i > 0 ==> j = (m+n+1)/2 - i < (m+n+1)/2 <= (2*n+1)/2 <= n
	 * 
	 * Rf : https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2481/Share-my-O(log(min(mn))-solution-with-explanation
	 */
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		int m = nums1.length;
		int n = nums2.length;

		if (m > n)
			return findMedianSortedArrays(nums2, nums1);

		int i = 0, j = 0, imin = 0, imax = m, half = (m + n + 1) / 2;
		double maxLeft = 0, minRight = 0;
		while (imin <= imax) {
			i = (imin + imax) / 2;
			j = half - i;
			if (i < m && nums2[j - 1] > nums1[i]) { // i is too small, must increase it
				imin = i + 1;
			} 
			else if (i > 0 && nums1[i - 1] > nums2[j]) { // i is too big, must decrease it
				imax = i - 1;
			} 
			else { // i is perfect
				if (i == 0)
					maxLeft = (double) nums2[j - 1];
				else if (j == 0)
					maxLeft = (double) nums1[i - 1];
				else
					maxLeft = (double) Math.max(nums1[i - 1], nums2[j - 1]);
				
				break;
			}
		}
		
		if ((m + n) % 2 == 1)
			return maxLeft;
		
		if (i == m)
			minRight = (double) nums2[j];
		else if (j == n)
			minRight = (double) nums1[i];
		else
			minRight = (double) Math.min(nums1[i], nums2[j]);
		
		return (double) (maxLeft + minRight) / 2;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2479/JAVA-Easy-Version-To-Understand!!
	 */
	public int findKthSmallest_move_back(int[] a, int m, int begin1, int[] b, int n, int begin2, int k) {
		if (m > n)
			return findKthSmallest_move_back(b, n, begin2, a, m, begin1, k);
		if (m == 0)
			return b[begin2 + k - 1];
		if (k == 1)
			return Integer.min(a[begin1], b[begin2]);
		
		int partA = Integer.min(k / 2, m), partB = k - partA;
		if (a[begin1 + partA - 1] == b[begin2 + partB - 1])
			return a[begin1 + partA - 1];
		else if (a[begin1 + partA - 1] > b[begin2 + partB - 1]) // b too small. Need to move backward.
			return findKthSmallest_move_back(a, partA, begin1, 
					b, n - partB, begin2 + partB, k - partB);
		else   // a too small. b too large.
			return findKthSmallest_move_back(a, m - partA, begin1 + partA, 
					b, partB, begin2, k - partA);
	}
	public double findMedianSortedArrays_move_back(int[] nums1, int[] nums2) {
		int len1 = nums1.length, len2 = nums2.length, sumLen = len1 + len2;
		if (sumLen % 2 != 0) {
			return findKthSmallest_move_back(nums1, len1, 0, nums2, len2, 0, sumLen / 2 + 1);
		} 
		else {
			return (findKthSmallest_move_back(nums1, len1, 0, nums2, len2, 0, sumLen / 2)
					+ findKthSmallest_move_back(nums1, len1, 0, nums2, len2, 0, sumLen / 2 + 1)) / 2.0;
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2496/Concise-JAVA-solution-based-on-Binary-Search
	 * 
	 * The key point of this problem is to ignore half part of A and B each step 
	 * recursively by comparing the median of remaining A and B: time = O(log(m + n))
	 * 
	 * if (aMid < bMid) Keep [aRight + bLeft]    
	 * else Keep [bRight + aLeft]
	 * 
	 * If you want to find the first(k==1) smallest element in two sorted arrays, you 
	 * should just return the smaller one from the first elements of the two arrays
	 * 
	 * Rf :
	 * leetcode.com/problems/median-of-two-sorted-arrays/discuss/2496/Concise-JAVA-solution-based-on-Binary-Search/3041
	 * leetcode.com/problems/median-of-two-sorted-arrays/discuss/2496/Concise-JAVA-solution-based-on-Binary-Search/3036
	 */
	public double findMedianSortedArrays_remove_half(int[] A, int[] B) {
		int m = A.length, n = B.length;
		int l = (m + n + 1) / 2;
		int r = (m + n + 2) / 2;
		return (getkth(A, 0, B, 0, l) + getkth(A, 0, B, 0, r)) / 2.0;
	}
	public double getkth(int[] A, int aStart, int[] B, int bStart, int k) {
		if (aStart > A.length - 1)
			return B[bStart + k - 1];
		if (bStart > B.length - 1)
			return A[aStart + k - 1];
		if (k == 1)
			return Math.min(A[aStart], B[bStart]);

		int aMid = Integer.MAX_VALUE, bMid = Integer.MAX_VALUE;
		if (aStart + k / 2 - 1 < A.length)
			aMid = A[aStart + k / 2 - 1];
		if (bStart + k / 2 - 1 < B.length)
			bMid = B[bStart + k / 2 - 1];

		if (aMid < bMid)
			return getkth(A, aStart + k / 2, B, bStart, k - k / 2);// Check: aRight + bLeft
		else
			return getkth(A, aStart, B, bStart + k / 2, k - k / 2);// Check: bRight + aLeft
	}
	
	/*
	 * leetcode.com/problems/median-of-two-sorted-arrays/discuss/2471/Very-concise-O(log(min(MN)))-iterative-solution-with-detailed-explanation/2811
	 * 
	 * L: the number immediately left to the cut, and R: right counterpart 
	 * [2 3 5 7] => [2 3 / 5 7] : median = (3 + 5)/2 ; L = 3, R = 5 
	 * [2 3 4 5 6] => [2 3 (4/4) 5 7] : median = (4 + 4) / 2 = 4
	 * L = (N-1)/2, R = N/2. median = (L + R) / 2
	 * 
	 * Add a few imaginary 'positions' (represented as #'s) in between numbers, 
	 * and treat numbers as 'positions' as well.
	 * [6 9 13 18]  ->   [# 6 # 9 # 13 # 18 #]    (N = 4)
	 * position index     0 1 2 3 4 5  6 7  8     (N_Position = 9)
	 * 
	 * [6 9 11 13 18]->   [# 6 # 9 # 11 # 13 # 18 #]   (N = 5)
	 * position index      0 1 2 3 4 5  6 7  8 9 10    (N_Position = 11)
	 * 
	 * There are 2*N+1 'positions'. The middle cut should always be made on the Nth 
	 * position (0-based). Since index(L) = (N-1)/2 and index(R) = N/2, we can infer 
	 * that index(L) = (CutPosition-1)/2, index(R) = (CutPosition)/2.
	 * 
	 * A1: [# 1 # 2 # 3 # 4 # 5 #]    (N1 = 5, N1_positions = 11)
	 * A2: [# 1 # 1 # 1 # 1 #]     (N2 = 4, N2_positions = 9)
	 * 
	 * There are 2N1 + 2N2 + 2 position altogether. Therefore, there must be exactly 
	 * N1 + N2 positions on each side of the cut, and 2 positions are taken by the cuts.
	 * 
	 * Therefore, when we cut at position C2 = K in A2, then the cut position in A1 
	 * must be C1 = N1 + N2 - K.  
	 * 
	 * [# 1 # 2 # 3 # (4/4) # 5 #]    C1 = 4 + 5 - C2 = 7.
	 * [# 1 / 1 # 1 # 1 #]            if C2 = 2 
	 * 
	 * L1 = A1[(C1-1)/2]; R1 = A1[C1/2];
	 * L2 = A2[(C2-1)/2]; R2 = A2[C2/2];
	 * 
	 * L1 <= R1 && L1 <= R2 && L2 <= R1 && L2 <= R2
	 * 
	 * 1. If we have L1 > R2, it means there are too many large numbers on the left 
	 *    half of A1, then we must move C1 to the left (i.e. move C2 to the right); 
	 * 2. If L2 > R1, then there are too many large numbers on the left half of A2, 
	 *    and we must move C2 to the left.
	 * 3. Otherwise, this cut is the right one. 
	 * After we find the cut, the medium can be computed as (max(L1, L2) + min(R1, R2)) / 2;
	 * 
	 * It is much more practical to move C2 (the one on the shorter array) first. The 
	 * reason is that on the shorter array, all positions are possible cut locations 
	 * for median, but on the longer array, the positions that are too far left or 
	 * right are simply impossible for a legitimate cut.
	 * 
	 * The only edge case is when a cut falls on the 0th(first) or the 2Nth(last) 
	 * position. For instance, if C2 = 2N2, then R2 = A2[2*N2/2] = A2[N2], which 
	 * exceeds the boundary of the array. To solve this problem, we can imagine that 
	 * both A1 and A2 actually have two extra elements, INT_MIN at A[-1] and INT_MAX 
	 * at A[N]. These additions don't change the result, but make the implementation 
	 * easier: If any L falls out of the left boundary of the array, then L = INT_MIN, 
	 * and if any R falls out of the right boundary, then R = INT_MAX.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2471/Very-concise-O(log(min(MN)))-iterative-solution-with-detailed-explanation
	 * leetcode.com/problems/median-of-two-sorted-arrays/discuss/2471/Very-concise-O(log(min(MN)))-iterative-solution-with-detailed-explanation/2804
	 * leetcode.com/problems/median-of-two-sorted-arrays/discuss/2471/Very-concise-O(log(min(MN)))-iterative-solution-with-detailed-explanation/2802
	 * leetcode.com/problems/median-of-two-sorted-arrays/discuss/2471/Very-concise-O(log(min(MN)))-iterative-solution-with-detailed-explanation/2777
	 */
	public double findMedianSortedArrays2(int[] nums1, int[] nums2) {
		int N1 = nums1.length, N2 = nums2.length;
		if (N1 < N2)             // Make sure A2 is the shorter one.
			return findMedianSortedArrays2(nums2, nums1);

		int lo = 0, hi = 2 * N2; // totally 2N + 1 positions, hi - lo + 1 = 2N + 1 where lo = 0, and hi = 2*N
		while (lo <= hi) {
			int C2 = (lo + hi) / 2;   // Try Cut 2 
			int C1 = N1 + N2 - C2;    // Calculate Cut 1 accordingly

			// Get L1, R1, L2, R2 respectively
			double L1 = (C1 == 0) ? Integer.MIN_VALUE : nums1[(C1 - 1) / 2];
			double L2 = (C2 == 0) ? Integer.MIN_VALUE : nums2[(C2 - 1) / 2];
			double R1 = (C1 == 2 * N1) ? Integer.MAX_VALUE : nums1[C1 / 2];
			double R2 = (C2 == 2 * N2) ? Integer.MAX_VALUE : nums2[C2 / 2];

			if (L1 > R2)      // A1's lower half is too big; need to move C1 left (C2 right)
				lo = C2 + 1;
			else if (L2 > R1) // A2's lower half too big; need to move C2 left.
				hi = C2 - 1;
			else              // Otherwise, that's the right cut.
				return (Math.max(L1, L2) + Math.min(R1, R2)) / 2;
		}
		return -1;
	}
	
	/*
	 * https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2547/Share-my-iterative-solution-with-O(log(min(n-m)))
	 * 
	 * To find the median of n numbers, means that you need to cut off the first 
	 * (n - 1)/2 or n/2 numbers, depends on whether n is odd. When it comes to two 
	 * sorted list of numbers, you need to cut off the first (n + m - 1)/2 or (n + m)/2 
	 * numbers. In this answer's code, k = (n + m - 1) / 2 is the numbers to cut off. 
	 * By doing binary search with constrain midB = k - midA, we can get two close 
	 * number and the count before this two number is k. The result median is adjacent 
	 * to "midA, midB".
	 * 
	 * k = (n + m - 1) / 2
	 * odd: cut off k, median is located at the first kth element
	 * even: cut off k, median is the average between the first kth and (k + 1)th
	 * 
	 * Rf : leetcode.com/problems/median-of-two-sorted-arrays/discuss/2547/Share-my-iterative-solution-with-O(log(min(n-m)))/3155
	 */
	public double findMedianSortedArrays_iterative(int A[], int B[]) {
	    int n = A.length;
	    int m = B.length;
	    
	    // the following call is to make sure len(A) <= len(B).
	    // It calls itself at most once, shouldn't be consider a recursive solution
	    if (n > m)
	        return findMedianSortedArrays_iterative(B, A);

	    // now, do binary search (find first position)
	    int k = (n + m - 1) / 2;
	    int l = 0, r = Math.min(k, n); // r is n, NOT n-1, this is important!!
		while (l < r) {
			int midA = (l + r) / 2;
			int midB = k - midA;
			if (A[midA] < B[midB])
				l = midA + 1;
			else
				r = midA;
		}
	    
	    // after binary search, we almost get the median because it must be between
	    // these 4 numbers: A[l-1], A[l], B[k-l], and B[k-l+1] 

	    // if (n+m) is odd, the median is the larger one between A[l-1] and B[k-l].
	    // and there are some corner cases we need to take care of.
		int a = Math.max(l > 0 ? A[l - 1] : Integer.MIN_VALUE, 
				k - l >= 0 ? B[k - l] : Integer.MIN_VALUE);
		if (((n + m) & 1) == 1)
			return (double) a;

	    // if (n+m) is even, the median can be calculated by 
	    //      median = (max(A[l-1], B[k-l]) + min(A[l], B[k-l+1]) / 2.0
	    // also, there are some corner cases to take care of.
		int b = Math.min(l < n ? A[l] : Integer.MAX_VALUE, 
				k - l + 1 < m ? B[k - l + 1] : Integer.MAX_VALUE);
		return (a + b) / 2.0;
	}

	// https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2652/Share-one-divide-and-conquer-O(log(m+n))-method-with-clear-description
	
	// https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2511/Intuitive-Python-O(log-(m+n))-solution-by-kth-smallest-in-the-two-sorted-arrays-252ms

}
