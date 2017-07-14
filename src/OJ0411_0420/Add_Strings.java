package OJ0411_0420;

public class Add_Strings {
	// https://discuss.leetcode.com/topic/62310/straightforward-java-8-main-lines-25ms
	public String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int carry = 0;
        for(int i = num1.length() - 1, j = num2.length() - 1; i >= 0 || j >= 0 || carry == 1; i--, j--){
            int x = i < 0 ? 0 : num1.charAt(i) - '0';
            int y = j < 0 ? 0 : num2.charAt(j) - '0';
            sb.append((x + y + carry) % 10);
            carry = (x + y + carry) / 10;
        }
        return sb.reverse().toString();
    }
	
	// Self
	public String addStrings2(String num1, String num2) {
		String big = num1;
		String small = num2;
		if (num1.length() < num2.length()) {
			big = num2;
			small = num1;
		}
		int[] c = new int[big.length()];
		int diff = big.length() - small.length();
		int sum = 0;
		for (int i = c.length - 1; i >= 0; i--) {
			if (i - diff < 0) {
				sum += (big.charAt(i) - '0');
			}
			else {
				sum += (big.charAt(i) - '0') + (small.charAt(i - diff) - '0');					
			}
			c[i] = sum % 10;
			sum = sum / 10;
		}
		
		StringBuilder out = new StringBuilder();
		if (sum != 0)
			out.append(sum);
		for (int element : c) {
			out.append(element);
		}
		return out.toString();	
	}
	
	/*
	 * https://discuss.leetcode.com/topic/64593/clean-java-code
	 * 
	 * Rf : https://discuss.leetcode.com/topic/65011/java-simple-and-clean-with-explanations-29-ms
	 */
	public String addStrings3(String num1, String num2) {
		int len1 = num1.length() - 1;
		int len2 = num2.length() - 1;
		StringBuilder s = new StringBuilder();
		int sum = 0, carry = 0;

		while (len1 >= 0 || len2 >= 0) {
			int first = len1 >= 0 ? num1.charAt(len1) - '0' : 0;
			int second = len2 >= 0 ? num2.charAt(len2) - '0' : 0;
			sum = carry + first + second;
			s.insert(0, sum % 10);
			carry = sum / 10;
			len1--;
			len2--;
		}

		if (carry > 0)
			s.insert(0, carry);

		return s.toString();
	}
	
	// https://discuss.leetcode.com/topic/62424/java-19ms-in-place-without-stringbuilder
	public String addStrings4(String num1, String num2) {
		// suppose num1 is shorter
		if (num1.length() > num2.length())
			return addStrings(num2, num1);
		char[] arr1 = num1.toCharArray(), arr2 = num2.toCharArray();
		int len1 = arr1.length, len2 = arr2.length, carry = 0;
		for (int i = 0; i < len2; ++i) {
			int idx1 = len1 - i - 1, idx2 = len2 - i - 1;

			if (idx1 >= 0)
				arr2[idx2] += (arr1[idx1] - '0' + carry);
			else if (carry == 0)
				break;
			else
				arr2[idx2] += carry;

			if (arr2[idx2] > '9') {
				carry = 1;
				arr2[idx2] -= 10;
			} else
				carry = 0;
		}
		return carry == 1 ? "1".concat(String.valueOf(arr2)) : String.valueOf(arr2);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Add_Strings add = new Add_Strings();
		String a = "1234567890987654321";
		String b = "987654321";
		System.out.println(add.addStrings2(a, b));

	}

}
