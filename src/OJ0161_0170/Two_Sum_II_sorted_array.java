package OJ0161_0170;

import java.util.Scanner;

public class Two_Sum_II_sorted_array {
	private int[] twoSum(int[] numbers, int target) {
		int a = 0;
		int b = numbers.length - 1;
		while (a < b) {
			int sum = numbers[a] + numbers[b];
			if (sum < target) {
				a++;
			}
			else if (sum > target) {
				b--;
			}
			else {
				int[] result = {a + 1, b + 1};
				return result;
			}
		}
		throw new IllegalArgumentException("No two sum solution");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Two_Sum_II_sorted_array twosum_II = new Two_Sum_II_sorted_array();
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Input the total amount of numbers.");
		int total = keyboard.nextInt();
		System.out.println("Enter an array of integers: ");
        int[] line = new int[total];
        for (int i = 0; i < total; i++) {
        	line[i] = keyboard.nextInt();
        }
        System.out.println("Enter the sum : ");
        int sum = keyboard.nextInt();
        int[] ans = twosum_II.twoSum(line, sum);
        String out = "";
        for (int i = 0; i < ans.length; i++) {
        	out = out + ans[i] + " ";
        }
        System.out.println("Two-pass Hash Table Answer : " + out);
        keyboard.close();
	}

}
