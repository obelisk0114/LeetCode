package OJ41_50;

import java.util.Scanner;

public class Pow_x_n {
	private static double fastPower(int x, int n) {
		// handle base is zero.
		if (x == 0 && n == 0) {
			throw new IllegalArgumentException("0 to the power of 0 is illegal.");
		}
		else if (x == 0 && n < 0) {
			throw new IllegalArgumentException("This is illegal.");
		}
		else if (x == 0) {
			return 0;
		}
		
		if (n == 0) {
			return 1;
		}
		else if (x == 1) {
			return 1;
		}
		else if (x == -1) {
			if ((n & 1) == 1) {
				return -1;
			}
			else
				return 1;
		}
		else if ((n == Integer.MAX_VALUE) && (x > 1)) {
		    return Double.POSITIVE_INFINITY;
		} 
		else if ((n == Integer.MAX_VALUE) && (x < -1)) {
		    return Double.NEGATIVE_INFINITY;
		} 
		else if (n == Integer.MIN_VALUE) {
		    return 0;
		}
		int rslt = 1;
		boolean positive = true;
		if (n < 0) {
			n = -n;
			positive = false;
		}
		
		while (n != 0) {
			if ((n & 1) == 1) {          // If n is odd.
				rslt *= x;
			}
			x *= x;
			n >>= 1;
		}
		
		if (positive)
			return rslt;
		else
			return 1.0/rslt;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Please input the base : ");
		int base = keyboard.nextInt();
		System.out.println("Please input the order : ");
		int power = keyboard.nextInt();
		System.out.println("Answer : " + fastPower(base, power));
		keyboard.close();
	}

}
