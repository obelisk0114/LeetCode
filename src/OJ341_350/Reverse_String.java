package OJ341_350;

public class Reverse_String {
	// Self
	public String reverseString(String s) {
		StringBuilder builder = new StringBuilder(s);
		builder.reverse();
		return builder.toString();
	}
	
	// Self
	public String reverseString2(String s) {
		StringBuilder builder = new StringBuilder();
		for (int i = s.length() - 1; i >= 0; i--) {
			builder.append(s.charAt(i));
		}
		return builder.toString();
	}
	
	/*
	 * https://discuss.leetcode.com/topic/44869/java-easiest-method-2-line-code-attached-another-method
	 * 
	 * Rf : https://discuss.leetcode.com/topic/47573/2ms-java-solution
	 */
	public String reverseString_swap(String s){
        if(s == null || s.length() == 0)
            return "";
        char[] cs = s.toCharArray();
        int begin = 0, end = s.length() - 1;
        while(begin <= end){
            char c = cs[begin];
            cs[begin] = cs[end];
            cs[end] = c;
            begin++;
            end--;
        }
        
        return new String(cs);
    }
	
	/*
	 * https://discuss.leetcode.com/topic/44316/java-solution-two-pointers
	 * 
	 * Rf : https://discuss.leetcode.com/topic/43296/java-simple-and-clean-with-explanations-6-solutions
	 */
	public String reverseString_2Pointer(String s) {
        int head = 0, tail = s.length() - 1;
        char[] ch = new char[s.length()];
        while(head <= tail) {
        	ch[head] = s.charAt(tail);
        	ch[tail--] = s.charAt(head++);
        }
        return new String(ch);
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Reverse_String reverse = new Reverse_String();
		String s = "hello";
		System.out.println(reverse.reverseString(s));
		System.out.println(reverse.reverseString2(s));
		System.out.println(reverse.reverseString_swap(s));
		System.out.println(reverse.reverseString_2Pointer(s));
	}

}
