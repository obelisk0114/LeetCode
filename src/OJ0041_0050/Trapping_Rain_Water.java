package OJ0041_0050;

// https://leetcode.com/articles/trapping-rain-water/

public class Trapping_Rain_Water {
	/*
	 * https://discuss.leetcode.com/topic/3016/share-my-short-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/5125/sharing-my-simple-c-code-o-n-time-o-1-space
	 */
	public int trap(int[] A) {
		int a = 0;
		int b = A.length - 1;
		int max = 0;
		int leftmax = 0;
		int rightmax = 0;
		while (a < b) {
			leftmax = Math.max(leftmax, A[a]);
			rightmax = Math.max(rightmax, A[b]);
			if (leftmax < rightmax) {
				max += (leftmax - A[a]);    // leftmax is smaller than rightmax, so
											// the (leftmax-A[a]) water can be stored
				a++;
			} else {
				max += (rightmax - A[b]);
				b--;
			}
		}
		return max;
	}
	
	/*
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89495/How-to-get-the-solution-to-2-D-%22Trapping-Rain-Water%22-problem-from-1-D-case
	 * 
	 * Rain water "container": To hold the rain water, we need some sort of 
	 * "container", which will be defined by its boundary.
	 * 
	 * Liebig's law of the minimum: The maximum height of water for current container 
	 * is determined by the lowest part of its boundary.
	 * 
	 * Boundary replacement: The above "law of the minimum" applies only to neighbors 
	 * of the lowest part of current boundary. After that the lowest part will be 
	 * replaced by its neighbors. The height of the new parts of the boundary will be 
	 * the larger one of the old boundary height and the height from the elevation map.
	 * 
	 * In the 1-D case, the boundary of the container contains only two points, i.e., 
	 * the left and right edges (l and r) from the elevation map. We then apply the 
	 * "law of the minimum" to find the minimum height of the boundary (corresponding 
	 * to the two cases involving comparing height[l] and height[r]). After that we 
	 * replace the old part of the boundary with its neighbors and continue until the 
	 * container shrinks to a point.
	 */
	public int trap_1D(int[] height) {
	    int res = 0, l = 0, r = height.length - 1;
	        
	    while (l < r) {
	        if (height[l] <= height[r]) {
	            if (l + 1 < r) {
	                res += Math.max(0, height[l] - height[l + 1]);
	                height[l + 1] = Math.max(height[l], height[l + 1]);
	            }
	                
	            l++;
	        } 
	        else {
	            if (l < r - 1) {
	                res += Math.max(0, height[r] - height[r - 1]);
	                height[r - 1] = Math.max(height[r], height[r - 1]);
	            }
	                
	            r--;
	        }
	    }
	        
	    return res;
	}
	
	// https://discuss.leetcode.com/topic/22976/my-accepted-java-solution
	public int trap2(int[] height) {
		if (height.length <= 2)
			return 0;
		int max = Integer.MIN_VALUE;
		int maxIndex = -1;
		for (int i = 0; i < height.length; i++) {
			if (height[i] > max) {
				max = height[i];
				maxIndex = i;
			}
		}

		int leftMax = height[0];
		int water = 0;
		for (int i = 1; i < maxIndex; i++) {
			if (height[i] > leftMax) {
				leftMax = height[i];
			} else {
				water += leftMax - height[i];
			}
		}

		int rightMax = height[height.length - 1];
		for (int i = height.length - 2; i > maxIndex; i--) {
			if (height[i] > rightMax) {
				rightMax = height[i];
			} else {
				water += rightMax - height[i];
			}
		}

		return water;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/26932/very-concise-java-solution-no-stack-with-explanations
	 * 
	 * Begin scan from beginning and end of array. 
	 * Compare value of left and right pointer, 
	 * hold the greater one and move the other to inner array. 
	 * Compute passed area when pointer gets inner.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/18720/8-lines-c-c-java-python-solution
	 */
	public int trap1_2(int[] height) {
		int secHight = 0;
		int left = 0;
		int right = height.length - 1;
		int area = 0;
		while (left < right) {
			if (height[left] < height[right]) {
				secHight = Math.max(height[left], secHight);
				area += secHight - height[left];
				left++;
			} else {
				secHight = Math.max(height[right], secHight);
				area += secHight - height[right];
				right--;
			}
		}
		return area;
	}
	
	// https://discuss.leetcode.com/topic/5819/sharing-my-java-code-o-n-time-o-1-space
	public int trap1_3(int[] A) {
	    if (A.length < 3) return 0;
	    
	    int ans = 0;
	    int l = 0, r = A.length - 1;
	    
	    // find the left and right edge which can hold water
	    while (l < r && A[l] <= A[l + 1]) l++;
	    while (l < r && A[r] <= A[r - 1]) r--;
	    
	    while (l < r) {
	        int left = A[l];
	        int right = A[r];
	        if (left <= right) {
	            // add volum until an edge larger than the left edge
	            while (l < r && left >= A[++l]) {
	                ans += left - A[l];
	            }
	        } else {
	            // add volum until an edge larger than the right volum
	            while (l < r && A[--r] <= right) {
	                ans += right - A[r];
	            }
	        }
	    }
	    return ans;
	}
	
	// https://discuss.leetcode.com/topic/4939/a-stack-based-solution-for-reference-inspired-by-histogram
	
	public static void main(String[] args) {
		Trapping_Rain_Water trapping = new Trapping_Rain_Water();
		int[] a = {0,1,0,2,1,0,1,3,2,1,2,1};
		System.out.println("Ans = " + trapping.trap2(a));
	}

}
