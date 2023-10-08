package OJ0381_0390;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Shuffle_an_Array {
	/*
	 * https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
	 * https://blog.csdn.net/qq_26399665/article/details/79831490
	 */
	
	/*
	 * https://leetcode.com/problems/shuffle-an-array/discuss/85958/First-Accepted-Solution-Java
	 * 
	 * Rf :
	 * https://leetcode.com/problems/shuffle-an-array/discuss/85958/First-Accepted-Solution-Java/91048
	 * https://leetcode.com/problems/shuffle-an-array/discuss/85958/First-Accepted-Solution-Java/91032
	 * https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#Sattolo's_algorithm
	 */
	public class Shuffle_an_Array_Sattolo_algorithm {
	    private int[] nums;
	    private Random random;

	    public Shuffle_an_Array_Sattolo_algorithm(int[] nums) {
	        this.nums = nums;
	        random = new Random();
	    }
	    
	    /** Resets the array to its original configuration and return it. */
	    public int[] reset() {
	        return nums;
	    }
	    
	    /** Returns a random shuffling of the array. */
	    public int[] shuffle() {
	        if(nums == null) return null;
	        int[] a = nums.clone();
	        for(int j = 1; j < a.length; j++) {
	            int i = random.nextInt(j + 1);     // include itself
	            swap(a, i, j);
	        }
	        return a;
	    }
	    
	    private void swap(int[] a, int i, int j) {
	        int t = a[i];
	        a[i] = a[j];
	        a[j] = t;
	    }
	}
	
	/*
	 * https://leetcode.com/problems/shuffle-an-array/discuss/86017/Using-Fisheru2013Yates-shuffle-java-solution.
	 * 
	 * refer wikipedia for Fisher-Yates shuffle
	 * 
	 * Other code :
	 * https://leetcode.com/problems/shuffle-an-array/discuss/197912/Java-150ms-faster-than-94
	 */
	private int[] nums;
	private int[] copy;

	public Shuffle_an_Array(int[] nums) {
		this.nums = nums;
		this.copy = nums.clone();
	}

	public int[] reset() {
		return copy;
	}

	public int[] shuffle() {
		Random random = new Random();
		for (int i = nums.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			int t = nums[i];
			nums[i] = nums[j];
			nums[j] = t;
		}
		return nums;
	}
	
	/*
	 * https://leetcode.com/articles/shuffle-an-array/
	 * 
	 * On each iteration, we generate a random integer between the current index and 
	 * the last index of the array. Then, we swap the elements at the current index 
	 * and the chosen index - this simulates drawing (and removing) the element from 
	 * the hat, as the next range from which we select a random index will not include 
	 * the most recently processed one. It is possible to swap an element with itself 
	 * - otherwise, some array permutations would be more likely than others.
	 */
	class Solution {
	    private int[] array;
	    private int[] original;

	    Random rand = new Random();

	    private int randRange(int min, int max) {
	        return rand.nextInt(max - min) + min;
	    }

	    private void swapAt(int i, int j) {
	        int temp = array[i];
	        array[i] = array[j];
	        array[j] = temp;
	    }

	    public Solution(int[] nums) {
	        array = nums;
	        original = nums.clone();
	    }
	    
	    public int[] reset() {
	        array = original;
	        original = original.clone();
	        return original;
	    }
	    
	    public int[] shuffle() {
	        for (int i = 0; i < array.length; i++) {
	            swapAt(i, randRange(i, array.length));
	        }
	        return array;
	    }
	}

	/*
	 * https://leetcode.com/problems/shuffle-an-array/discuss/86006/simple-java-solution
	 * 
	 * Inside-Out Algorithm 算法的基本思想是從前向後掃描數據，把位置 i 的數據隨機插入到前 i 個(包括第 i 個)
	 * 位置中(假設為 k)，這個操作是在新陣列中進行，然後把原始數據中位置 k 的數字替換新陣列位置 i 的數字。其實效果
	 * 相當於新陣列中位置 k 和位置 i 的數字進行交換。
	 * 
	 * Rf : https://blog.csdn.net/qq_26399665/article/details/79831490
	 */
	public class Shuffle_an_Array_Inside_out {

	    private int[] nums;
	    
	    public Shuffle_an_Array_Inside_out(int[] nums) {
	        this.nums = nums;
	    }
	    
	    /** Resets the array to its original configuration and return it. */
	    public int[] reset() {
	        return nums;
	    }
	    
	    /** Returns a random shuffling of the array. */
	    public int[] shuffle() {
	        int[] rand = new int[nums.length];
	        for (int i = 0; i < nums.length; i++){
	            int r = (int) (Math.random() * (i+1));
	            rand[i] = rand[r];
	            rand[r] = nums[i];
	        }
	        return rand;
	    }
	}
	
	// by myself
	class Shuffle_an_Array_self {
	    private List<Integer> list;
	    private int[] origin;

	    public Shuffle_an_Array_self(int[] nums) {
	        list = new ArrayList<>();
	        origin = new int[nums.length];
	        
	        for (int i = 0; i < nums.length; i++) {
	            list.add(nums[i]);
	            origin[i] = nums[i];
	        }
	    }
	    
	    /** Resets the array to its original configuration and return it. */
	    public int[] reset() {
	        return origin;
	    }
	    
	    /** Returns a random shuffling of the array. */
	    public int[] shuffle() {
	        Collections.shuffle(list);
	        
	        int[] ans = new int[list.size()];
	        for (int i = 0; i < ans.length; i++) {
	            ans[i] = list.get(i);
	        }
	        return ans;
	    }
	}
	
	/**
     * Python collections
     *
     * https://leetcode.com/problems/shuffle-an-array/discuss/85957/easy-python-solution-based-on-generating-random-index-and-swapping
     * https://leetcode.com/problems/shuffle-an-array/discuss/86000/Python.-Solution-in-a-few-lines
     * https://leetcode.com/problems/shuffle-an-array/discuss/160162/Elegant-100-solution-in-Python-using-random-in-3-lines-of-code
     * https://leetcode.com/problems/shuffle-an-array/discuss/86053/Python-hack
     */
	
	/**
     * C++ collections
     * 
     * https://leetcode.com/problems/shuffle-an-array/discuss/85979/Straight-forward-C%2B%2B-solution
     * https://leetcode.com/problems/shuffle-an-array/discuss/86001/C%2B%2B-solution-with-Fisher-Yates-algorithm
     * https://leetcode.com/problems/shuffle-an-array/discuss/85992/My-straightforward-C%2B%2B-solution
     */
	
	/**
	 * Your Solution object will be instantiated and called as such:
	 * Solution obj = new Solution(nums);
	 * int[] param_1 = obj.reset();
	 * int[] param_2 = obj.shuffle();
	 */

}
