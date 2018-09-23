package OJ0661_0670;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Maximum_Swap {
	/*
	 * leetcode.com/problems/maximum-swap/discuss/107073/C++-one-pass-simple-and-fast-solution:-1-3ms-O(n)-time/138000
	 * 
	 * To achieve the maximum number after a swap, you have to swap with the most 
	 * significant digit possible. Additionally, you want to make sure that the digit 
	 * you swap with that more significant number is as big as possible. Actually this 
	 * problem can be easily solved by only one pass from backward.
	 * 
	 * 1. record the largest digit and its corresponding index
	 * 2. if the current digit is smaller than the largest digit, this digit and the 
	 *    largest digit are the best candidate for max swap so far. In this case, this 
	 *    digit pair is recorded. Otherwise, if the current digit is bigger, reset 
	 *    the maxIndex.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/maximum-swap/discuss/107146/Java-O(n)-time-O(n)-space-w-Explanation
	 * https://leetcode.com/problems/maximum-swap/discuss/107073/C++-one-pass-simple-and-fast-solution:-1-3ms-O(n)-time
	 */
	public int maximumSwap_one_pass(int num) {
		char[] chars = Integer.toString(num).toCharArray();
		int maxIndex = chars.length - 1;
		int x = 0, y = 0;
		for (int i = chars.length - 2; i >= 0; i--) {
			if (chars[maxIndex] < chars[i]) {
				maxIndex = i;
			} 
            else if (chars[maxIndex] > chars[i]) {
				x = maxIndex;
				y = i;
			}
		}

		char temp = chars[x];
		chars[x] = chars[y];
		chars[y] = temp;

		return Integer.valueOf(new String(chars));
	}
	
	/*
	 * https://leetcode.com/problems/maximum-swap/discuss/107068/Java-simple-solution-O(n)-time
	 * 
	 * Use buckets to record the last position of digit 0 ~ 9 in this num.
	 * 
	 * Loop through the num array from left to right. For each position, we check 
	 * whether there exists a larger digit in this num (start from 9 to current digit). 
	 * We also need to make sure the position of this larger digit is behind the 
	 * current one. If we find it, simply swap these two digits and return the result.
	 * 
	 * Rf :
	 * leetcode.com/problems/maximum-swap/discuss/107068/Java-simple-solution-O(n)-time/109335
	 * leetcode.com/problems/maximum-swap/discuss/107068/Java-simple-solution-O(n)-time/109334
	 */
	public int maximumSwap_bucket(int num) {
		char[] digits = Integer.toString(num).toCharArray();

		int[] buckets = new int[10];
		for (int i = 0; i < digits.length; i++) {
			buckets[digits[i] - '0'] = i;
		}

		for (int i = 0; i < digits.length; i++) {
			for (int k = 9; k > digits[i] - '0'; k--) {
				if (buckets[k] > i) {
					char tmp = digits[i];
					digits[i] = digits[buckets[k]];
					digits[buckets[k]] = tmp;
					return Integer.valueOf(new String(digits));
				}
			}
		}

		return num;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-swap/discuss/136012/Java-no-array-conversion-O(n)-and-real-O(1)-solution-(9ms)
	 * 
	 * Other code : 
	 * https://leetcode.com/problems/maximum-swap/discuss/107098/C++-One-Pass-O(n)-and-O(1)-space-pure-math-no-string-manipulation-Detailed-explanation
	 */
	public int maximumSwap_record_xy_and_idx(int num) {
		int n = num;
		int prevMax = -1, prevMaxIdx = -1;
		int i = 1;
		
		// We save the candidate x and y to switch
		int candidate_x = -1, candidate_x_idx = -1;
		int candidate_y = -1, candidate_y_idx = -1;

		while (n > 0) {
			int d = n % 10;
			
			// Find a swap candidate
			if (d < prevMax) {
				candidate_x = d;
				candidate_y = prevMax;
				candidate_x_idx = i;
				candidate_y_idx = prevMaxIdx;
			}
			// Update prevMax
			if (d > prevMax) {
				prevMax = d;
				prevMaxIdx = i;
			}
			
			i *= 10;
			n /= 10;
		}
		
		if (candidate_x == -1 || candidate_y == -1) {
			return num;
		}
		
        //swap
        return num - candidate_x * candidate_x_idx
            - candidate_y * candidate_y_idx
            + candidate_x * candidate_y_idx
            + candidate_y * candidate_x_idx;
    }
	
	/*
	 * https://leetcode.com/problems/maximum-swap/discuss/107076/Fastest-O(n)-time-java-solution-w-detailed-explanation.-Intuitive-and-easy-to-understand.
	 * 
	 * 1. Search from the left end and find the position where the reversed list was 
	 *    not satisfied. (example. in number 54367, we will find the position of 6.) 
	 *    Then we cut the original digits array to two parts, the first part was 
	 *    reversely sorted which means no swap needed within it and the second part.
	 * 2. Find and record the max value and max value position in the second part. 
	 *    ( example, digit 7 in 54367)
	 * 3. Swap the max value in the second part to the first number in first part 
	 *    that smaller than that max value. (Example, swap 7 with 5 in 54367)
	 *    
	 * Rf : https://leetcode.com/problems/maximum-swap/discuss/107102/Simple-AC-O(n)-java-solution-with-ex
	 */
	public int maximumSwap_left_right(int num) {
		// Convert to int array (num -> digits[])
		String temp = Integer.toString(num);
		int[] digits = new int[temp.length()];
		for (int i = 0; i < temp.length(); i++) {
			digits[i] = temp.charAt(i) - '0';
		}
        
        // Ignore all digits until (next digit > prev). store the min and minindex
		int min = digits[0], minIndex = 0;
		for (int i = 0; i < digits.length - 1; i++) {
			if (digits[i + 1] > digits[i]) {
				minIndex = i;
				min = digits[i];
				break;
            } 
            else if (i == digits.length - 2)  // Reached end. Nothing to swap.           
                return num;                   // Return original number.
        }
        
        // Starting from minindex find the largest digit in the remaining digits.
		int max = min, maxIndex = -1;
		for (int j = minIndex; j < digits.length; j++) {
			if (digits[j] >= max) {
				max = digits[j];
				maxIndex = j;
			}
		}
        
        // Iterate through the array till minIndex to find any digit that is lesser than max
        int result = 0, swapindex = minIndex;
        for (int i = 0; i <= minIndex; i++){
            if (digits[i] < max) {
                swapindex = i;
                break;
            }
        }
        
        // Swap the maxIndex digit with swapIndex
        int tmp = digits[swapindex];
        digits[swapindex] = digits[maxIndex];
        digits[maxIndex] = tmp;
         
        // Convert the result into integer (digits -> result) 
        for (int i = digits.length - 1, j = 0; i >= 0; i--) {
            result = result + (digits[j] * ((int) Math.pow(10, i)));
            j++;
        }
        
        return result;
    }
	
	/*
	 * https://leetcode.com/problems/maximum-swap/discuss/107090/Java-easy-to-understand-code-with-only-two-passes.-Beats-87.77
	 * 
	 * Use maxIdx[i] to represent the index of maximum digit in range [i, N);
	 * 
	 * N = 8,
	 * index      0  1  2  3  4  5  6  7       
	 * num        2  7  3  6  9  3  4  1
	 * maxIdx     4  4  4  4  4  6  6  7 
	 * 
	 * Scan the number to find the first index k, which is not the maximum digit in 
	 * range [k, N). And swap it with that digit.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/maximum-swap/discuss/107138/C++-concise-code-O(N-8)
	 * https://leetcode.com/problems/maximum-swap/discuss/107084/C++-3ms-O(n)-time-O(n)-space-DP-solution
	 */
	public int maximumSwap_two_pass(int num) {
		char[] digits = String.valueOf(num).toCharArray();
		int[] maxIdx = new int[digits.length];
		int maxPos = digits.length - 1;
		maxIdx[maxPos] = maxPos;

		for (int i = digits.length - 2; i >= 0; i--) {
			if (digits[i] > digits[maxPos]) {
				maxPos = i;
			}
			maxIdx[i] = maxPos;
		}

		for (int i = 0; i < digits.length; i++) {
			if (digits[i] != digits[maxIdx[i]]) {
				char tmp = digits[i];
				digits[i] = digits[maxIdx[i]];
				digits[maxIdx[i]] = tmp;
				return Integer.parseInt(String.valueOf(digits));
			}
		}

		return num;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-swap/discuss/107157/Java-solution-based-on-sorting-and-finding-indices-to-swap
	 * 
	 * Convert the string to character array and sort the digits in descending order. 
	 * 
	 * Traverse through the digits of the actual number (originalString) and sorted 
	 * one (sortedString). As soon as you see a mismatch, it means you have found the 
	 * biggest number which hasn't occurred early. So you need to swap it.
	 * 
	 * Since there might be many of this big digit in the actual number, you need to 
	 * swap that with the one which came later in the number. (closer to end) so you 
	 * search for this digit from end of the number and break as soon as you find it. 
	 * This guarantees that you are building a bigger number.
	 */
	public int maximumSwap_sort(int num) {
		String s = Integer.toString(num);
		char[] originalString = s.toCharArray();

		Character[] sortedString = new Character[originalString.length];
		for (int i = 0; i < originalString.length; i++) {
			sortedString[i] = originalString[i];
		}
		Arrays.sort(sortedString, Collections.reverseOrder());

		int i; // Find the position of mismatch between the original and sorted string
		for (i = 0; i < originalString.length; i++) {
			if (originalString[i] != sortedString[i])
				break;
		}

		// if no mismatch, no swap needed, return the original number
		if (i == originalString.length)
			return num;
		
		// find the last position of the mismatching digit in the original string
		int j = s.lastIndexOf(sortedString[i]);

        // Interchange digits in position i and j
        char temp = originalString[i];
        originalString[i] = originalString[j];
        originalString[j] = temp;
        
        return Integer.parseInt(new String(originalString));
	}
	
	/*
	 * by myself2
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/maximum-swap/discuss/107096/My-Java-and-C-solution-using-sorting-With-explanation
	 * https://leetcode.com/problems/maximum-swap/discuss/126216/Python-easy-to-understand-beat-97-sorted-first-then-compare-pos-to-pos
	 */
	public int maximumSwap_self2(int num) {
        List<Integer> list = new ArrayList<>();
        int tmp = num;
        while (tmp != 0) {
            list.add(0, tmp % 10);
            tmp /= 10;
        }
        
        List<Integer> sortlist = new ArrayList<>(list);
        Collections.sort(sortlist, Collections.reverseOrder());
        
        int front = 0;
        int back = -1;
        while (front < list.size()) {
            if (sortlist.get(front) != list.get(front)) {
                for (int j = list.size() - 1; j > front; j--) {
                    if (list.get(j) == sortlist.get(front)) {
						back = j;
						int diff = (int) (list.get(back) *
								(Math.pow(10, list.size() - 1 - front) - 
										Math.pow(10, list.size() - 1 - back))
								+ list.get(front) * 
								(Math.pow(10, list.size() - 1 - back)
										- Math.pow(10, list.size() - 1 - front)));
						num += diff;
						return num;
                    }
                }
            }
            
            front++;
        }
        return num;
    }
	
	// by myself
	public int maximumSwap_self(int num) {
        List<Integer> list = new ArrayList<>();
        int tmp = num;
        while (tmp != 0) {
            list.add(0, tmp % 10);
            tmp /= 10;
        }
        
        List<Integer> sortlist = new ArrayList<>(list);
        Collections.sort(sortlist, Collections.reverseOrder());
        
        int i = 0;
        int ans = 0;
        while (i < list.size()) {
            if (sortlist.get(i) != list.get(i)) {
                ans = ans * 10 + sortlist.get(i);
                int back = 0;
                boolean find = false;
                for (int j = list.size() - 1; j > i; j--) {
                    if (!find && list.get(j) == sortlist.get(i)) {
                        back = back + (int) Math.pow(10, list.size() - 1 - j) * list.get(i);
                        find = true;
                    }
                    else {
                        back = back + (int) Math.pow(10, list.size() - 1 - j) * list.get(j);
                    }
                }
                ans = ans * (int) Math.pow(10, list.size() - 1 - i) + back;
                return ans;
            }
            else {
                ans = ans * 10 + sortlist.get(i);
            }
            i++;
        }
        return num;
    }
	
	// https://leetcode.com/problems/maximum-swap/discuss/107131/Java-solution-brute-force
	public int maximumSwap_brute_force(int num) {
		char[] arr = (num + "").toCharArray();
		for (int i = 0; i < arr.length - 1; i++) {
			int k = -1, idx = -1;
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[j] >= k) {
					k = arr[j];
					idx = j;
				}
			}
			
			if (arr[i] < k) {
				arr[idx] = arr[i];
				arr[i] = (char) k;
				
				int res = 0;
				for (int l = 0; l < arr.length; l++) {
					res = res * 10 + arr[l] - '0';
				}
				return res;
			}
		}

		return num;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-swap/discuss/107137/3-5-lines-PythonC++JavaRuby
	 * 
	 * Try them all, swapping between any two places p and q (where places are 1, 10, 
	 * 100, 1000, etc, i.e., the "ones place", "tens place", "hundreds place", etc). 
	 * With num/p%10 I get the digit at place p, and multiplying it with (q-p) takes 
	 * it out of place p and puts it into place q. And move the digit from place q to 
	 * place p the same way.
	 */
	public int maximumSwap_try_everyone(int num) {
		int max = 0;
		for (int p = 1; p <= num; p *= 10)
			for (int q = p; q <= num; q *= 10)
				max = Math.max(max, num + (num / p) % 10 * (q - p) + (num / q) % 10 * (p - q));
		return max;
	}

}
