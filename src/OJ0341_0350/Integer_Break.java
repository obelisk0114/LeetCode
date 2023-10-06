package OJ0341_0350;

// https://stackoverflow.com/questions/32418731/java-math-powa-b-time-complexity

public class Integer_Break {
	/*
	 * https://discuss.leetcode.com/topic/51239/a-clear-math-proof-of-the-algorithm
	 * 
	 * If an optimal product contains a factor f >= 4, then you can replace it with 
	 * factors 2 and f-2 without losing optimality, as 2*(f-2) = 2f-4 >= f. So you 
	 * never need a factor greater than or equal to 4, meaning you only need factors 
	 * 1, 2 and 3 (and 1 is of course wasteful and you'd only use it for n=2 and n=3, 
	 * where it's needed).
	 * 
	 * 3*3 is simply better than 2*2*2, so you'd never use 2 more than twice.
	 * 
	 * =============
	 * 
	 * a1 + a2 +...+ am = n. By the Inequality of Arithmetic and Geometric Means,
	 * (a1 + a2 +...+ am) / m >= (a1 * a2 *...* am)^(1 / m)
	 * (n / m) ^ m >= a1 * a2 *...* am ; equality holds iff a1 = a2 =...= am
	 * 
	 * x = n / m, and f(x) = x ^ (n / x). Let y = ln[f(x)] = (n / x) * ln(x)
	 * y' = -(n / x^2) * ln(x) + (n/x) * (1/x) = (n / x^2) * [ln(e) - ln(x)]
	 * y' = 0, x = e. 
	 * f(x) is increasing iff y increasing iff y'>0 iff [ln(e) - ln(x)] > 0 iff x < e
     * and is decreasing iff x > e; and is maximized when x = e.
	 * But x is integer, therefore, Max value appears in 3.
	 * 
	 * n = r * 3 + s * 2, r,s >= 0. Hence s = (n - 3r) / 2
	 * P(r) = (3^r)(2^s) = (3^r)(2^( (n-3r)/2) ). ln(P) = rln(3) + [(n-3r) ln(2)]/2
	 * Let z = ln(P), then z' = ln(3)- 3 ln(2) / 2 > 0
	 * Use as much r as possible.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/integer-break/discuss/80721/Why-factor-2-or-3-The-math-behind-this-problem./85320
	 * https://discuss.leetcode.com/topic/45341/a-simple-explanation-of-the-math-part-and-a-o-n-solution
	 * https://discuss.leetcode.com/topic/43055/why-factor-2-or-3-the-math-behind-this-problem
	 * https://leetcode.com/problems/integer-break/editorial/#Approach 4: Equation
	 */
	public int integerBreak_math(int n) {
		if (n < 3)
			return 1;
		if (n == 3)
			return 2;
		if (n % 3 == 0)
			return (int) Math.pow(3, n / 3);
		if (n % 3 == 1)
			return 2 * 2 * ((int) Math.pow(3, (n - 4) / 3));

		return 2 * ((int) Math.pow(3, (n - 2) / 3));
	}
	
	/*
	 * https://discuss.leetcode.com/topic/42978/java-dp-solution/2
	 * 
	 * Rf :
	 * https://leetcode.com/problems/integer-break/discuss/80694/Java-DP-solution/204792
	 */
	public int integerBreak_dp(int n) {
		int[] dp = new int[n + 1];
		dp[1] = 1;
		for (int i = 2; i <= n; i++) {
			// i can only be broken into 2 parts,       => j
			// but these numbers can be further broken. => dp[j]
			// Math.max(not broken, broken) => Math.max(j, dp[j])
			for (int j = 1; 2 * j <= i; j++) {
				dp[i] = Math.max(dp[i], (Math.max(j, dp[j])) * (Math.max(i - j, dp[i - j])));
			}
		}
		return dp[n];
	}

	/*
	 * by myself
	 * 
	 * we have two options:
	 * 1. Don't split the number up at all. We can initialize the answer as `ans = num`.
	 * 2. Split the number. We can try all possible splits. Iterate `i` from `2` until `num`. 
	 *    For each value of `i`, try to update `ans` with `i * dp(num - i)` if it is larger.
	 * 
	 * We need to check for 2 separate cases before performing the recursion.
	 * 1. If `n == 2`, we immediately return `1`. The only possible split is `1 * 1`.
	 * 2. If `n == 3`, we immediately return `2`. The only possible split is `1 * 2`.
	 * 
	 * Rf:
	 * https://leetcode.com/problems/integer-break/editorial/
	 */
	public int integerBreak_self_dp(int n) {
        int[] dp = new int[n + 1];
        //dp[0] = 1;
        dp[1] = 1;

        for (int i = 2; i < dp.length; i++) {
            for (int j = 1; j < i; j++) {
                dp[i] = Math.max(dp[i], Math.max(dp[j], j) * (i - j));
            }
        }

        //System.out.println(Arrays.toString(dp));

        return dp[n];
    }

	/*
	 * https://leetcode.com/problems/integer-break/editorial/
	 * Approach 2: Bottom-Up Dynamic Programming
	 * 
	 * We have the following base cases for this function:
	 * 1. If `num == 1`, then it isn't possible to split the number up, so we just `return 1`.
	 * 2. If `num == 2`, then it would be better to not split the number at all, since the only 
	 *    possible split `1 * 1` is less than `2`, so just `return 2`. The exact same argument 
	 *    can be made for `num == 3`: the only possible split `1 * 2` is less than `3` itself, 
	 *    so just `return 3`.
	 * 
	 * Otherwise, we have two options:
	 * 1. Don't split the number up at all. We can initialize the answer as `ans = num`.
	 * 2. Split the number. We can try all possible splits. Iterate `i` from `2` until `num`. 
	 *    For each value of `i`, try to update `ans` with `i * dp(num - i)` if it is larger.
	 * 
	 * We need to check for 2 separate cases before performing the recursion.
	 * 1. If `n == 2`, we immediately return `1`. The only possible split is `1 * 1`.
	 * 2. If `n == 3`, we immediately return `2`. The only possible split is `1 * 2`.
	 * 
	 * We need to explicitly check for these cases before going into the recursion, otherwise, 
	 * we would incorrectly return a larger answer since we initialize `ans = num`.
	 * 
	 * As you can see, as `n` increases, the product from splitting becomes larger and larger, and 
	 * thus we will always satisfy the requirement of needing to perform at least one split. The 
	 * only cases where performing a split results in a lower product is `2, 3`.
	 * 
	 * In bottom-up, we will start from these base cases and iterate toward the answer. We will 
	 * use a table `dp`.
	 * 
	 * We have a for loop for `num` starting from `4` and iterating to `n`. Each iteration of this 
	 * loop represents a state, like a function call. We can calculate this state the same way we 
	 * did in the previous approach, by checking all possible splits with a for loop over `i`.
	 * 
	 * Algorithm
	 * 1. If `n <= 3`, then `return n - 1`.
	 * 2. Create an array `dp` of length `n + 1`.
	 * 3. Set the base cases. For `i = 1, 2, 3`, set `dp[i] = i`.
	 * 4. Iterate `num` from `4` to `n`:
	 *   + Initialize `ans = num`. This is the case of not splitting the number at all.
	 *   + Iterate `i` from `2` until `num`:
	 *     + Try to update `ans` with `i * dp[num - i]` if it is larger.
	 *   + Set `dp[num] = ans`.
	 * 5. Return `dp[n]`.
	 * 
	 * Time complexity: O(n^2)
	 * There are O(n) possible states of `num` that `dp` can be called with. We only calculate 
	 * each state once, as we calculate one state per outer for loop iteration. To calculate a 
	 * state, we iterate from `2` until `num`, which costs up to O(n). Thus, we have a time 
	 * complexity of O(n^2).
	 * 
	 * Space complexity: O(n)
	 * The table `dp` requires O(n) space.
	 */
	public int integerBreak_bottom_up(int n) {
		if (n <= 3) {
			return n - 1;
		}
			
		int[] dp = new int[n + 1];
	
		// Set base cases
		for (int i = 1; i <= 3; i++) {
			dp[i] = i;
		}
			
		for (int num = 4; num <= n; num++) {
			int ans = num;
			for (int i = 2; i < num; i++) {
				ans = Math.max(ans, i * dp[num - i]);
			}
			
			dp[num] = ans;
		}
	
		return dp[n];
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * 算術平均數 >= 幾何平均數, 等號成立條件為每個數相同
	 * 取不同分割方式的最大值
	 */
	public int integerBreak_self(int n) {
        int splitCount = 2;
        int max = (n / splitCount) * (n - n / splitCount);
        while (splitCount * 2 < n) {
            splitCount++;
            max = Math.max(max, getMultiply(n, splitCount));
        }
        return max;
    }
    
    private int getMultiply(int n, int splitCount) {
        int ans = 1;
        int remain = n % splitCount;
        for (int j = 0; j < splitCount; j++) {
            if (j < remain) {
                ans = ans * (n / splitCount + 1);
            }
            else {
                ans = ans * (n / splitCount);
            }
        }
        //System.out.println("i = " + i + " ; ans = " + ans);
        return ans;
    }

	/*
	 * https://leetcode.com/problems/integer-break/editorial/
	 * Approach 3: Mathematics
	 * 
	 * it is optimal to only split `n` into `2` and `3`!
	 * 
	 * The inequality of arithmetic and geometric means shows that to maximize the product of 
	 * a set of numbers with a fixed sum, the numbers should all be equal. This means that we 
	 * should split `n` into `a` copies of `x`. We have:
	 * 
	 * n = ax
	 * So, a = n/x
	 * 
	 * The product of these copies will be x^a, we will substitute a = n/x and represent this 
	 * function as f.
	 * 
	 * f(x) = x^(n/x)
	 * 
	 * We need to maximize this function. Let's start by taking the derivative of f with respect 
	 * to x (here, log is the natural logarithm).
	 * 
	 * f'(x) = -nx^(n/x - 2) * (log(x) - 1)
	 * 
	 * Now, we set this equal to `0` and solve for `x` to find a critical point.
	 * 
	 * -nx^(n/x - 2) * (log(x) - 1) = 0
	 * 
	 * f = 0 when log(x) - 1 = 0. This is the case when x = e = 2.71828...
	 * 
	 * f is maximized at e. Unfortunately, e is not an integer, and this problem is called 
	 * Integer Break. The nearest integer is 3, which suggests that we should try to use 3 
	 * as much as we can.
	 * 
	 * For numbers that are not divisible by 3, we will have remainders. This is where 2 comes in. 
	 * 
	 * When we have `n = 4`, it is better to split it into `2 * 2` versus `3 * 1`. Thus, the strategy 
	 * of splitting into as many 3s as we can only applies when `n > 4`. This brings us to our 
	 * algorithm. As long as `n > 4`, keep splitting 3s. Once `n <= 4`, we can just multiply it 
	 * directly with whatever product we have built up to that point.
	 * 
	 * Note that due to the constraint of needing to perform at least one split, we still need to 
	 * account for the special cases when `n <= 3`.
	 * 
	 * Algorithm
	 * 1. If `n <= 3`, then `return n - 1`.
	 * 2. Initialize `ans = 1`.
	 * 3. While `n > 4`:
	 *   + Multiply `ans` by `3`.
	 *   + Subtract `3` from `n`.
	 * 4. Return `ans * n`.
	 * 
	 * Time complexity: O(n)
	 * The while loop performs O(n / 3) = O(n) iterations.
	 * 
	 * Space complexity: O(1)
	 * We aren't using any extra space other than a few integers.
	 */
	public int integerBreak_math2(int n) {
        if (n <= 3) {
            return n - 1;
        }
        
        int ans = 1;
        while (n > 4) {
            ans *= 3;
            n -= 3;
        }
        
        return ans * n;
    }

	/*
	 * https://leetcode.com/problems/integer-break/editorial/
	 * Approach 1: Top-Down Dynamic Programming
	 * 
	 * Let's say we have an integer `num` and split it into two integers: `i` and `num - i`. The 
	 * highest product possible would be `i * BEST`, where `BEST` is the highest product possible 
	 * from splitting up `num - i`.
	 * 
	 * We have the following base cases for this function:
	 * 1. If `num == 1`, then it isn't possible to split the number up, so we just `return 1`.
	 * 2. If `num == 2`, then it would be better to not split the number at all, since the only 
	 *    possible split `1 * 1` is less than `2`, so just `return 2`. The exact same argument 
	 *    can be made for `num == 3`: the only possible split `1 * 2` is less than `3` itself, 
	 *    so just `return 3`.
	 * 
	 * Otherwise, we have two options:
	 * 1. Don't split the number up at all. We can initialize the answer as `ans = num`.
	 * 2. Split the number. We can try all possible splits. Iterate `i` from `2` until `num`. 
	 *    For each value of `i`, try to update `ans` with `i * dp(num - i)` if it is larger.
	 * 
	 * We need to check for 2 separate cases before performing the recursion.
	 * 1. If `n == 2`, we immediately return `1`. The only possible split is `1 * 1`.
	 * 2. If `n == 3`, we immediately return `2`. The only possible split is `1 * 2`.
	 * 
	 * We need to explicitly check for these cases before going into the recursion, otherwise, 
	 * we would incorrectly return a larger answer since we initialize `ans = num`.
	 * 
	 * For all other values of `n`, the recursion will work. Take a look at the first few numbers
	 * + For `num = 4`, we can do `2 * 2 = 4`, which is not less than `4` itself.
	 * + For `num = 5`, we can do `2 * 3 = 6`, which is not less than `5` itself.
	 * + For `num = 6`, we can do `3 * 3 = 9`, which is not less than `6` itself.
	 * 
	 * As you can see, as `n` increases, the product from splitting becomes larger and larger, and 
	 * thus we will always satisfy the requirement of needing to perform at least one split. The 
	 * only cases where performing a split results in a lower product is `2, 3`.
	 * 
	 * The first time we calculate `dp(num)`, we will store the result. In the future, we can 
	 * reference this result for `num` instead of having to recalculate it.
	 * 
	 * Algorithm
	 * 1. If `n <= 3`, then `return n - 1`.
	 * 2. Define a memoized function `dp(num)`:
	 *   + Base case: if `num <= 3`, then `return num`.
	 *   + Initialize `ans = num`. This is the case of not splitting the number at all.
	 *   + Iterate `i` from `2` until `num`:
	 *     + Try to update `ans` with `i * dp(num - i)` if it is larger.
	 *   + Return `ans`.
	 * 3. Return `dp(n)`.
	 * 
	 * Time complexity: O(n^2)
	 * There are O(n) possible states of `num` that `dp` can be called with. Due to memoization, 
	 * we only calculate each state once. To calculate a state, we iterate from `2` until `num`, 
	 * which costs up to O(n). Thus, we have a time complexity of O(n^2).
	 * 
	 * Space complexity: O(n)
	 * The recursion call stack can grow up to O(n). Also, we require O(n) space to memoize the 
	 * function.
	 */
	class Solution_top_down {
		int[] memo;
		
		public int integerBreak(int n) {
			if (n <= 3) {
				return n - 1;
			}
			
			memo = new int[n + 1];
			return dp(n);
		}
		
		public int dp(int num) {
			if (num <= 3) {
				return num;
			}
			
			if (memo[num] != 0) {
				return memo[num];
			}
			
			int ans = num;
			for (int i = 2; i < num; i++) {
				ans = Math.max(ans, i * dp(num - i));
			}
			
			memo[num] = ans;
			return ans;
		}
	}

}
