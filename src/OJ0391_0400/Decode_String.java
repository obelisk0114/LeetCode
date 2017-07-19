package OJ0391_0400;

import java.util.LinkedList;
import java.util.Stack;

public class Decode_String {
	// https://discuss.leetcode.com/topic/57159/simple-java-solution-using-stack/3
	public String decodeString(String s) {
        Stack<Integer> intStack = new Stack<>();
        Stack<StringBuilder> strStack = new Stack<>();
        StringBuilder cur = new StringBuilder();
        int k = 0;
        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) {
                k = k * 10 + ch - '0';
            } else if ( ch == '[') {
                intStack.push(k);
                strStack.push(cur);
                cur = new StringBuilder();
                k = 0;
            } else if (ch == ']') {
                StringBuilder tmp = cur;
                cur = strStack.pop();
                for (k = intStack.pop(); k > 0; --k) cur.append(tmp);
            } else cur.append(ch);
        }
        return cur.toString();
    }
	
	/*
	 * https://discuss.leetcode.com/topic/57250/java-short-and-easy-understanding-solution-using-stack
	 * 
	 * Rf : https://discuss.leetcode.com/topic/57159/simple-java-solution-using-stack
	 */
	public String decodeString_stack(String s) {
		Stack<Integer> count = new Stack<>();
		Stack<String> result = new Stack<>();
		int i = 0;
		result.push("");
		while (i < s.length()) {
			char ch = s.charAt(i);
			if (ch >= '0' && ch <= '9') {
				int start = i;
				while (s.charAt(i + 1) >= '0' && s.charAt(i + 1) <= '9')
					i++;
				count.push(Integer.parseInt(s.substring(start, i + 1)));
			} else if (ch == '[') {
				result.push("");
			} else if (ch == ']') {
				String str = result.pop();
				StringBuilder sb = new StringBuilder();
				int times = count.pop();
				for (int j = 0; j < times; j += 1) {
					sb.append(str);
				}
				result.push(result.pop() + sb.toString());
			} else {
				result.push(result.pop() + ch);
			}
			i += 1;
		}
		return result.pop();
	}
	
	/*
	 * self
	 * 
	 * Rf : https://discuss.leetcode.com/topic/57356/java-2-stacks-solution-reference-basic-calculator
	 */
	public String decodeString_self(String s) {
		LinkedList<Character> stack = new LinkedList<>(); // StringBuilder stack = new StringBuilder();
		LinkedList<Integer> num = new LinkedList<>();
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char now = s.charAt(i); 
			if (now - '0' >= 0 && now - '0' <= 9) {
				if (i > 0) {
					char pre = s.charAt(i - 1);
					if (pre - '0' >= 0 && pre - '0' <= 9) {
						num.add(num.removeLast() * 10 + (now - '0'));
					}
					else {					
						num.add(now - '0');
					}					
				}
				else
					num.add(now - '0');
			}
			else if (now == ']') {
				while (stack.getLast() != '[') {
					output.insert(0, stack.removeLast());
				}
				stack.removeLast();
				for (int j = 0; j < num.getLast(); j++) {
					for (int j2 = 0; j2 < output.length(); j2++) {
						stack.add(output.charAt(j2));
					}
				}
				num.removeLast();
				output.replace(0, output.length(), "");
			}
			else {
				stack.add(now);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		while (!stack.isEmpty()) {
			sb.append(stack.removeFirst());
		}
		return sb.toString();
	}
	
	// https://discuss.leetcode.com/topic/57318/java-simple-recursive-solution
	
	public static void main(String[] args) {
		// TODO
		Decode_String decode = new Decode_String();
		String s = "10[leetcode]";
		System.out.println(decode.decodeString(s));
	}

}
