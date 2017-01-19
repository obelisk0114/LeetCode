package OJ11_20;

import java.util.Scanner;
import java.util.LinkedList;

public class Valid_Parentheses {
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
