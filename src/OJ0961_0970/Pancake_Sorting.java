package OJ0961_0970;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Pancake_Sorting {
	/*
	 * The following 3 functions are by myself.
	 * 
	 * Find the index of largest element and move it to the correct place
	 * 1. Find the largest number
	 * 2. Flip twice to the tail
	 * 
	 * Rf :
	 * https://leetcode.com/problems/pancake-sorting/discuss/818239/Simple-image-for-intuition-without-code-solution.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/pancake-sorting/discuss/214213/JavaC%2B%2BPython-Straight-Forward
	 */
	public List<Integer> pancakeSort_self(int[] A) {
        List<Integer> res = new ArrayList<>();
        
        // Can be removed
        if (A == null || A.length == 0) {
            return res;
        }
        
        for (int i = A.length - 1; i > 0; i--) {
            int pos = getPosition_self(A, i);
            
            // Since within 10 * arr.length flips will be judged as correct,
            // we can remove two if statements.
            if (pos != i) {
                if (pos != 0) {
                    reverse_self(A, pos);
                    res.add(pos + 1);
                }
                
                reverse_self(A, i);
                res.add(i + 1);
            }
        }
        return res;
    }
    
    private int getPosition_self(int[] A, int end) {
        int pos = 0;
        int max = A[0];
        
        for (int i = 0; i <= end; i++) {
            if (A[i] > max) {
                max = A[i];
                pos = i;
            }
        }
        return pos;
    }
    
    private void reverse_self(int[] A, int pos) {
        int i = 0;
        int j = pos;
        
        while (i < j) {
            int tmp = A[i];
            A[i] = A[j];
            A[j] = tmp;
            i++;
            j--;
        }
    }
    
    /*
     * The following 3 functions are from this link.
     * https://leetcode.com/problems/pancake-sorting/discuss/494417/Dew-It-or-True-O(n)-or-Explained-with-Diagrams
     * 
     * Use a map to find indices of each of the elements 1 to n in the given array.
     * Loop through positions 0 to n-1: for each index i, swap element at i with 
     * element at j
     * where j is the index of (i+1): use the index map from previous step
     * 
     * If we find the same Pancake Flip occurring as adjacent operations, let them 
     * cancel each other out
     * Skip Pancake Flips with K = 0 or 1
     * 
     * It has converted the problem "sort using pancake flips" into "swap using 
     * pancake flips".
     * 
     * Rf :
     * https://leetcode.com/problems/pancake-sorting/discuss/494417/Dew-It-or-True-O(n)-or-Explained-with-Diagrams/675768
     */
	public List<Integer> pancakeSort_swap(int[] arr) {
		Stack<Integer> stack = new Stack<>();
		
		int n = arr.length;
		int[] indices = new int[n];
		for (int i = 0; i < n; ++i)
			indices[arr[i] - 1] = i;
		
		for (int i = 0; i < n; ++i) {
			int pull = indices[i];
			dew_swap(stack, i, pull);
			indices[arr[i] - 1] = pull;
			indices[i] = i;
			arr[pull] = arr[i];
			arr[i] = i + 1;
		}
		
		LinkedList<Integer> ans = new LinkedList<>();
		while (!stack.isEmpty()) {
			int x = stack.pop();
			ans.addFirst(x);
		}
		return ans;
	}

	private void dew_swap(Stack<Integer> stack, int j, int i) {
		if (i != j) {
			add_swap(stack, i + 1);
			add_swap(stack, i - j + 1);
			add_swap(stack, i - j);
			add_swap(stack, i - j - 1);
			add_swap(stack, i - j);
			add_swap(stack, i + 1);
		}
	}

	private void add_swap(Stack<Integer> stack, int x) {
		if (x > 1) {
			if (!stack.isEmpty() && stack.peek() == x)
				stack.pop();
			else
				stack.push(x);
		}
	}
    
    /*
     * The following 3 functions are from this link.
     * https://leetcode.com/problems/pancake-sorting/discuss/818116/Detailed-Explanation-with-Dry-Run-Example
     * 
     * first find the index of largest element and move it to the correct place
     * 1. find the index of that element using find helper function.
     * 2. now flip the array form 0 to the find index.
     * 3. now flip the array from 0 to the correct place of that element
     * 4. Above 3 operation we have to perform for all elements from n to 1.
     * 
     * Other code:
     * https://leetcode.com/problems/pancake-sorting/solution/
     * https://leetcode.com/problems/pancake-sorting/discuss/214200/Java-flip-the-largest-number-to-the-tail
     */
    public List<Integer> pancakeSort2(int[] A) {
		List<Integer> list = new ArrayList<>();
		for (int n = A.length; n > 0; n--) {
			
			// first find the index of current target = n
			int index = find2(A, n);
			
			// flip the array till that index 
			// so that get that element on front of array
			flip2(A, index);
			
			// now flip the array to move that element on correct position
			flip2(A, n - 1);
			
			// add first flip operation k
			list.add(index + 1);
			
			// add second flip operation k
			list.add(n);
		}

		return list;
	}

	// find method will help to find the index of the target element in the array
	private int find2(int[] A, int target) {
		for (int i = 0; i < A.length; i++)
			if (A[i] == target)
				return i;
		return -1;
	}

	// flip method will help to flip all the element of subarray from [0, j]
	private void flip2(int[] A, int j) {
		int i = 0;
		while (i < j) {
			int temp = A[i];
			A[i++] = A[j];
			A[j--] = temp;
		}
	}
    
    /*
     * The following function and class are from this link.
     * https://leetcode.com/problems/pancake-sorting/discuss/385968/O(NlogN)-solutions-using-Fenwick-tree-with-O(N)-space-complexity
     * 
     * Suppose we have sorted A[:i] and now we insert A[i] into the sorted array.
     * We have to find how many numbers are smaller than A[i] (suppose n values 
     * smaller than A[i]).
     * If pre == 0, the insertion can be done by flip i, flip i+1. (add to the first)
     * If pre > 1, the insertion can be done by flip pre, flip i, flip i+1, flip pre+1.
     * 
     * Simply add checks to remove flip condition: flip 0.
     * 
     * a b c d   x   e f g
     *  sorted   ^   unsorted
     * 
     * pre = 0:
     * flip i     : d c b a x e f g
     * flip i + 1 : x a b c d e f g
     * 
     * pre = 2:
     * flip n     : b a c d x e f g
     * flip i     : d c a b x e f g
     * flip i + 1 : x b a c d e f g
     * flip n + 1 : a b x c d e f g
     * 
     * pre = 1:
     * flip i     : d c b a x e f g
     * flip i + 1 : x a b c d e f g
     * flip n + 1 : a x b c d e f g
     * 
     * 0 - N  index ボ场计, ﹍て 0 ボ, 璝琘竚 1 ボ硂计竒砆
     * add(A[i], 1) 穝, ボ盢硂计 Fenwick Tree 柑 (1 )
     * sum(A[i] - 1) ―㎝, ボ璸衡 Fenwick Tree 柑, ゑ讽玡计计计
     * 
     * Rf :
     * https://leetcode.com/problems/pancake-sorting/discuss/223059/Binary-Trie-O(NlogN)-time-O(N)-space
     */
	public List<Integer> pancakeSort_FenwickTree(int[] A) {
		List<Integer> ans = new ArrayList<>();
		int N = A.length;

		FenwickTree fwt = new FenwickTree(N + 1);
		fwt.add(A[0], 1);
		for (int i = 1; i < A.length; i++) {
			int pre = fwt.sum(A[i] - 1);
			fwt.add(A[i], 1);
			if (pre < i) {
				if (pre == 0) {
					if (i > 1)
						ans.add(i);
					ans.add(i + 1);
				} 
				else {
					if (pre > 1) {
						ans.add(pre);
					}
					ans.add(i);
					ans.add(i + 1);
					ans.add(pre + 1);
				}
			}
		}

		return ans;
	}

	public class FenwickTree {
		public int[] bit;
		private int n;

		public FenwickTree(int n) {
			bit = new int[n];
			this.n = n;
		}
		
		public int sum(int r) {
			int res = 0;
			while (r > 0) {
				res += bit[r];
				r -= lowbit(r);
			}
			return res;
		}
		
		public void add(int idx, int val) {
			while (idx < n) {
				bit[idx] += val;
				idx += lowbit(idx);
			}
		}
		
		private int lowbit(int i) {
			return i & (-i);
		}

		/*
		 * original code, discard
		 */
//		public int sum_discard(int r) {
//			int res = 0;
//			while (r >= 0) {
//				res += bit[r];
//				r = (r & (r + 1)) - 1;
//			}
//			return res;
//		}

		/*
		 * original code, discard
		 */
//		public void add_discard(int idx, int val) {
//			for (; idx < n; idx = idx | (idx + 1))
//				bit[idx] += val;
//		}
	}
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/pancake-sorting/discuss/274921/Python-Detailed-Explanation-for-This-Problem
     * https://leetcode.com/problems/pancake-sorting/discuss/818148/Simple-python-solution-with-comments-beats-99-time
     * https://leetcode.com/problems/pancake-sorting/discuss/214215/Python-recursive-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/pancake-sorting/discuss/223059/Binary-Trie-O(NlogN)-time-O(N)-space
     * https://leetcode.com/problems/pancake-sorting/discuss/598765/C%2B%2B-or-Simple-solution-or-Fix-max-index-and-Reverse
     * https://leetcode.com/problems/pancake-sorting/discuss/214285/C%2B%2B-8ms-8lines-easy-to-understand
     */
    
    /**
     * JavaScript collections
     * 
     * https://leetcode.com/problems/pancake-sorting/discuss/817960/JavaScript-explanation.
     */

}
