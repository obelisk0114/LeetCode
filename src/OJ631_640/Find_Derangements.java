package OJ631_640;

/*
 * n is in the range of [1, 10^6]
 * https://discuss.leetcode.com/category/802/find-the-derangement-of-an-array
 */
public class Find_Derangements {
	// https://leetcode.com/articles/find-derangements/
	public int findDerangement(int n) {
        long mul = 1, sum = 0, M = 1000000007;
        for (int i = n; i >= 0; i--) {
            sum = (sum + M + mul * (i % 2 == 0 ? 1 : -1)) % M;
            System.out.println(i + " time : " + sum);
            mul = (mul * i) % M;
        }
        return (int) sum;
    }
	
	// https://leetcode.com/articles/find-derangements/
	public int findDerangement2(int n) {
        if (n == 0)
            return 1;
        if (n == 1)
            return 0;
        int first = 1, second = 0;
        for (int i = 2; i <= n; i++) {
            int temp = second;
            second = (int)(((i - 1L) * (first + second)) % 1000000007);
            first = temp;
        }
        return second;
    }
	
	public static void main(String[] args) {
		Find_Derangements derange = new Find_Derangements();
		int a = 5;
		System.out.println(derange.findDerangement(a));
		System.out.println(derange.findDerangement2(a));

	}

}
