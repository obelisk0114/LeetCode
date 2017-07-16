package OJ0641_0650;

public class Maximum_Average_Subarray_I {
	public double findMaxAverage(int[] nums, int k) {
		double sum = 0;
		for (int i = 0; i < k; i++) {
			sum += nums[i];
		}
		double max = sum;
		for (int i = k; i < nums.length; i++) {
			sum = sum + nums[i] - nums[i - k];
			max = Math.max(max, sum);
		}
		return max / k;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Maximum_Average_Subarray_I maxAvgSub1 = new Maximum_Average_Subarray_I();
		int[] a = { 1, 12, -5, -20, 1, -6, 50, 3 };
		int b = 4;
		System.out.println(maxAvgSub1.findMaxAverage(a, b));

	}

}
