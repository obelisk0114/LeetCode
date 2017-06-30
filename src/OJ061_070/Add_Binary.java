package OJ061_070;

public class Add_Binary {
	/*
	 * https://discuss.leetcode.com/topic/13698/short-ac-solution-in-java-with-explanation
	 * 
	 * Rf : https://discuss.leetcode.com/topic/42073/super-short-java-code
	 */
	public String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() -1, carry = 0;
        while (i >= 0 || j >= 0) {
            int sum = carry;
            if (j >= 0) sum += b.charAt(j--) - '0';
            if (i >= 0) sum += a.charAt(i--) - '0';
            sb.append(sum % 2);
            carry = sum / 2;
        }
        if (carry != 0) sb.append(carry);
        return sb.reverse().toString();
    }
	
	// Self
	public String addBinary2(String a, String b) {
		int len = Math.max(a.length(), b.length());
		int[] c = new int[len];
		int diff = Math.abs(a.length() - b.length());
		int carry = 0;
		for (int i = len - 1; i >= 0; i--) {
			if (a.length() < b.length()) {
				if (i - diff < 0) {
					carry += b.charAt(i) - '0';
				}
				else {
					carry += (b.charAt(i) - '0') + (a.charAt(i - diff) - '0');
				}
				c[i] = carry % 2;
				carry /= 2; 
			}
			else {
				if (i - diff < 0) {
					carry += a.charAt(i) - '0';
				}
				else {
					carry += (a.charAt(i) - '0') + (b.charAt(i - diff) - '0');
				}
				c[i] = carry % 2;
				carry /= 2;
			}
		}
		
		StringBuilder out = new StringBuilder();
		if (carry != 0)
			out.append(carry);
		for (int element : c) {
			out.append(element);
		}
		return out.toString();
	}
	
	/*
	 * https://discuss.leetcode.com/topic/33693/another-simple-java
	 * 
	 * Rf : https://discuss.leetcode.com/topic/38690/3ms-java-simple-solution-using-stringbuilder
	 */
	public String addBinary3(String a, String b) {
	    if(a == null || b ==null)
	        return a == null? b: a;
	        
	    int carry =0;
	    StringBuilder sb = new StringBuilder();        
	    
	    for(int i = a.length()-1, j = b.length() -1;  i >=0 || j >=0 || carry >0 ; i --, j --){
	        int sum = 0;
	        sum += (i >=0) ? a.charAt(i) - '0' : 0;
	        sum += (j >=0) ? b.charAt(j) - '0' : 0;
	        sum += carry;
	        
	        carry = sum /2;
	        sum %=2;
	        sb.append(sum);
	    }
	    
	    return sb.reverse().toString();
	}
	
	// https://discuss.leetcode.com/topic/5172/simple-accepted-java-solution
	public String addBinary_XOR(String a, String b) {
        if(a == null || a.isEmpty()) {
            return b;
        }
        if(b == null || b.isEmpty()) {
            return a;
        }
        char[] aArray = a.toCharArray();
        char[] bArray = b.toCharArray();
        StringBuilder stb = new StringBuilder();

        int i = aArray.length - 1;
        int j = bArray.length - 1;
        int aByte;
        int bByte;
        int carry = 0;
        int result;

        while(i > -1 || j > -1 || carry == 1) {
            aByte = (i > -1) ? Character.getNumericValue(aArray[i--]) : 0;
            bByte = (j > -1) ? Character.getNumericValue(bArray[j--]) : 0;
            result = aByte ^ bByte ^ carry;
            carry = ((aByte + bByte + carry) >= 2) ? 1 : 0;
            stb.append(result);
        }
        return stb.reverse().toString();
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
