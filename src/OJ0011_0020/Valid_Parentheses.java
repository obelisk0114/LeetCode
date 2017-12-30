package OJ0011_0020;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class Valid_Parentheses {
	// https://discuss.leetcode.com/topic/7813/my-easy-to-understand-java-solution-with-one-stack
	public boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();
        // Iterate through string until empty
        for(int i = 0; i<s.length(); i++) {
            // Push any open parentheses onto stack
            if(s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{')
                stack.push(s.charAt(i));
            // Check stack for corresponding closing parentheses, false if not valid
            else if(s.charAt(i) == ')' && !stack.empty() && stack.peek() == '(')
                stack.pop();
            else if(s.charAt(i) == ']' && !stack.empty() && stack.peek() == '[')
                stack.pop();
            else if(s.charAt(i) == '}' && !stack.empty() && stack.peek() == '{')
                stack.pop();
            else
                return false;
        }
        // return true if no open parentheses left in stack
        return stack.empty();
    }
	
	// https://discuss.leetcode.com/topic/9006/java-solution-with-stack-very-easy-to-read-straightforward
	public boolean isValid2(String s) {
		Stack<Character> stack = new Stack<Character>();
		if (s.equals(""))
			return true;
		for (char ch : s.toCharArray()) {
			if (ch == '(' || ch == '[' || ch == '{') {
				stack.push(ch);
			} else if (ch == ')') {
				if (stack.empty() || stack.pop() != '(')
					return false;
			} else if (ch == ']') {
				if (stack.empty() || stack.pop() != '[')
					return false;
			} else if (ch == '}') {
				if (stack.empty() || stack.pop() != '{')
					return false;
			}
		}
		return stack.empty();
	}
	
	// https://discuss.leetcode.com/topic/27572/short-java-solution
	public boolean isValid_store_half(String s) {
		Stack<Character> stack = new Stack<Character>();
		for (char c : s.toCharArray()) {
			if (c == '(')
				stack.push(')');
			else if (c == '{')
				stack.push('}');
			else if (c == '[')
				stack.push(']');
			else if (stack.isEmpty() || stack.pop() != c)
				return false;
		}
		return stack.isEmpty();
	}
	
	// https://discuss.leetcode.com/topic/47067/java-iterative-solution-beating-97
	public boolean isValid_implement_stack(String s) {
		char[] stack = new char[s.length()];
		int head = 0;
		for (char c : s.toCharArray()) {
			switch (c) {
			case '{':
			case '[':
			case '(':
				stack[head++] = c;
				break;
			case '}':
				if (head == 0 || stack[--head] != '{')
					return false;
				break;
			case ')':
				if (head == 0 || stack[--head] != '(')
					return false;
				break;
			case ']':
				if (head == 0 || stack[--head] != '[')
					return false;
				break;
			}
		}
		return head == 0;

	}
	
	// https://discuss.leetcode.com/topic/9372/12-lines-of-java
	public boolean isValid_indexMatch(String s) {
		Stack<Integer> p = new Stack<>();
		for (int i = 0; i < s.length(); i++) {
			int q = "(){}[]".indexOf(s.substring(i, i + 1));
			if (q % 2 == 1) {
				if (p.isEmpty() || p.pop() != q - 1)
					return false;
			} 
			else
				p.push(q);
		}
		return p.isEmpty();
	}
	
	// https://discuss.leetcode.com/topic/27768/short-easy-to-follow-8ms-java-solution
	public boolean isValid_removePair(String s) {
		int length;

		do {
			length = s.length();
			s = s.replace("()", "").replace("{}", "").replace("[]", "");
		} while (length != s.length());

		return s.length() == 0;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/19387/my-21-line-java-solution-without-in-code
	 * 
	 * Rf : http://www.asciitable.com/
	 */
	public boolean isValid_without(String s) {
	    char [] arr = s.toCharArray();
		Stack<Character> stack = new Stack<>();
		for(char ch : arr){
			if(stack.isEmpty()){
				stack.push(ch);
			}
			else{
				char top = (char)stack.lastElement();
				if(ch - top == 1 || ch - top == 2){
					stack.pop();
				}
				else{
					stack.push(ch);
				}
			}
		}
		if(stack.isEmpty()){
		    return true;
		}
		return false;
	}
	
	// https://discuss.leetcode.com/topic/7587/simple-accepted-java-solution
	public boolean isValid_HashMap(String s) {
	    char[] chars = s.toCharArray();
	    Map<Character,Character> pairs = new HashMap<Character,Character>();
	    pairs.put('(', ')');
	    pairs.put('{', '}');
	    pairs.put('[', ']');
	    
	    Stack<Character> stack = new Stack<Character>();
	    for (char c:chars) {
	    	if (pairs.containsKey(c)) {
	    		stack.push(pairs.get(c));
	    	} 
			else {
				if (stack.isEmpty() || c != stack.pop())
					return false;
			}
		}
	    return stack.isEmpty();
	}
	
	/**********************                     **********************/
	private static int number_of_parentheses(String str) {
		int number = 0;
		LinkedList<String> stack = new LinkedList<String>();
		for (int i = 0; i < str.length(); i++) {
			String tmp = Character.toString(str.charAt(i));
			if (tmp.equals("(") || tmp.equals("[") || tmp.equals("{")) {
				stack.add(tmp);
			}
			else if (tmp.equals(")")) {
				if (stack.isEmpty()) {
					return -1;
				}
				if (stack.getLast().equals("(")) {
					stack.removeLast();
					number++;
				}
				else
					return -1;
			}
			else if (tmp.equals("]")) {
				if (stack.isEmpty()) {
					return -1;
				}
				if (stack.getLast().equals("[")) {
					stack.removeLast();
					number++;
				}
				else
					return -1;
			}
			else {
				if (stack.isEmpty()) {
					return -1;
				}
				if (stack.getLast().equals("{")) {
					stack.removeLast();
					number++;
				}
				else
					return -1;
			}
		}
		
		if (stack.isEmpty())
			return number;
		
		return -1;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter an String: ");
        String line = keyboard.nextLine();
        System.out.println("Answer : " + number_of_parentheses(line));
        
        keyboard.close();

	}

}
