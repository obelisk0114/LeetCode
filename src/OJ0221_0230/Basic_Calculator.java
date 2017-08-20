package OJ0221_0230;

import java.util.Stack;
import java.util.Deque;
import java.util.List;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Basic_Calculator {
	/*
	 * https://discuss.leetcode.com/topic/33044/java-easy-version-to-understand
	 * 
	 * Rf : https://discuss.leetcode.com/topic/15816/iterative-java-solution-with-stack
	 */
	public int calculate(String s) {
		int len = s.length(), sign = 1, result = 0;
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < len; i++) {
			if (Character.isDigit(s.charAt(i))) {
				int sum = s.charAt(i) - '0';
				while (i + 1 < len && Character.isDigit(s.charAt(i + 1))) {
					sum = sum * 10 + s.charAt(i + 1) - '0';
					i++;
				}
				result += sum * sign;
			} 
			else if (s.charAt(i) == '+')
				sign = 1;
			else if (s.charAt(i) == '-')
				sign = -1;
			else if (s.charAt(i) == '(') {
				stack.push(result);
				stack.push(sign);
				result = 0;
				sign = 1;
			} else if (s.charAt(i) == ')') {
				result = result * stack.pop() + stack.pop();
			}

		}
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/16919/java-solution-stack
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/23099/java-solution-use-stack
	 */
	public int calculate2(String s) {
		Deque<Integer> stack = new LinkedList<>();
		int rs = 0;
		int sign = 1;
		stack.push(1);
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ')
				continue;
			else if (s.charAt(i) == '(') {
				stack.push(stack.peekFirst() * sign);
				sign = 1;
			} else if (s.charAt(i) == ')')
				stack.pop();
			else if (s.charAt(i) == '+')
				sign = 1;
			else if (s.charAt(i) == '-')
				sign = -1;
			else {
				int temp = s.charAt(i) - '0';
				while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1)))
					temp = temp * 10 + s.charAt(++i) - '0';
				rs += sign * stack.peekFirst() * temp;
			}
		}
		return rs;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/35584/3ms-java-concise-and-fast-recursive-solution-with-comments-beats-99-61
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/63906/java-one-pass-recursion-method-3ms-beat-99
	 */
	public int calculate_recursive(String s) {
		if (s.length() == 0)
			return 0;
		int[] p = { 0 };
		return eval(s, p);
	}

	// calculate value between parentheses
	private int eval(String s, int[] p) {
		int val = 0;
		int i = p[0];
		int oper = 1; // 1:+ -1:-
		int num = 0;
		while (i < s.length()) {
			char c = s.charAt(i);
			switch (c) {
			case '+':
				val = val + oper * num;
				num = 0;
				oper = 1;
				i++;
				break;// end of number and set operator
			case '-':
				val = val + oper * num;
				num = 0;
				oper = -1;
				i++;
				break;// end of number and set operator
			case '(':
				p[0] = i + 1;
				val = val + oper * eval(s, p);
				i = p[0];
				break; // start a new eval
			case ')':
				p[0] = i + 1;
				return val + oper * num; // end current eval and return. Note that we need to deal with the last num
			case ' ':
				i++;
				continue;
			default:
				num = num * 10 + c - '0';
				i++;
			}
		}
		return val + oper * num;
	}
	
	/*
	 * The following 4 functions are from this link.
	 * https://discuss.leetcode.com/topic/15761/accepted-java-infix-to-postfix-based-solution-with-explaination-600ms
	 */
	int rank(char op) {
		// the bigger the number, the higher the rank
		switch (op) {
		case '+':
			return 1;
		case '-':
			return 1;
		case '*':
			return 2;
		case '/':
			return 2;
		default:
			return 0; // '('
		}
	}
	List<Object> infixToPostfix(String s) {
		Stack<Character> operators = new Stack<Character>();
		List<Object> postfix = new LinkedList<Object>();

		int numberBuffer = 0;
		boolean bufferingOperand = false;
		for (char c : s.toCharArray()) {
			if (c >= '0' && c <= '9') {
				numberBuffer = numberBuffer * 10 + c - '0';
				bufferingOperand = true;
			} 
			else {
				if (bufferingOperand)
					postfix.add(numberBuffer);
				numberBuffer = 0;
				bufferingOperand = false;

				if (c == ' ' || c == '\t')
					continue;

				if (c == '(') {
					operators.push('(');
				} 
				else if (c == ')') {
					while (operators.peek() != '(')
						postfix.add(operators.pop());
					operators.pop(); // popping "("
				} 
				else { // operator
					while (!operators.isEmpty() && rank(c) <= rank(operators.peek()))
						postfix.add(operators.pop());
					operators.push(c);
				}
			}

		}
		if (bufferingOperand)
			postfix.add(numberBuffer);

		while (!operators.isEmpty())
			postfix.add(operators.pop());

		return postfix;
	}
	int evaluatePostfix(List<Object> postfix) {
		Stack<Integer> operands = new Stack<Integer>();
		int a = 0, b = 0;
		for (Object s : postfix) {
			if (s instanceof Character) {
				char c = (Character) s;
				b = operands.pop();
				a = operands.pop();
				switch (c) {
				case '+':
					operands.push(a + b);
					break;
				case '-':
					operands.push(a - b);
					break;
				case '*':
					operands.push(a * b);
					break;
				default:
					operands.push(a / b);
				}
			} 
			else { // instanceof Integer
				operands.push((Integer) s);
			}
		}
		return operands.pop();
	}
	public int calculate_postfix(String s) {
		return evaluatePostfix(infixToPostfix(s));
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/15763/accepted-java-solution-easy-understood-with-explanations-no-need-to-convert-to-postfix-rnp
	 * 
	 * Combine infix to postfix and calculate
	 */
	public int calculate_combine_postfix(String expression) {
		char[] tokens = expression.toCharArray();

		// Stack for numbers: 'values'
		Stack<Integer> values = new Stack<Integer>();

		// Stack for Operators: 'ops'
		Stack<Character> ops = new Stack<Character>();

		for (int i = 0; i < tokens.length; i++) {
			// Current token is a whitespace, skip it
			if (tokens[i] == ' ')
				continue;

			// Current token is a number, push it to stack for numbers
			if (tokens[i] >= '0' && tokens[i] <= '9') {
				StringBuffer sbuf = new StringBuffer();
				// There may be more than one digits in number
				while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
					sbuf.append(tokens[i++]);
				values.push(Integer.parseInt(sbuf.toString()));
				i--;
			}

			// Current token is an opening brace, push it to 'ops'
			else if (tokens[i] == '(')
				ops.push(tokens[i]);

			// Closing brace encountered, solve entire brace
			else if (tokens[i] == ')') {

				while (ops.peek() != '(') {
					values.push(applyOp(ops.pop(), values.pop(), values.pop()));
				}
				if (ops.size() != 0) {
					ops.pop();
				}

			}

			// Current token is an operator.
			else if (tokens[i] == '+' || tokens[i] == '-' 
					|| tokens[i] == '*' || tokens[i] == '/') {
				// While top of 'ops' has same or greater precedence to current
				// token, which is an operator. Apply operator on top of 'ops'
				// to top two elements in values stack
				while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
					values.push(applyOp(ops.pop(), values.pop(), values.pop()));

				// Push current token to 'ops'.
				ops.push(tokens[i]);
			}
		}

		// Entire expression has been parsed at this point, apply remaining
		// ops to remaining values

		while (!ops.empty()) {
			if (values.size() == 1)
				return values.pop();
			values.push(applyOp(ops.pop(), values.pop(), values.pop()));
		}

		// Top of 'values' contains result, return it
		return values.pop();
	}

	// Returns true if 'op2' has higher or same precedence as 'op1', otherwise false.
	public boolean hasPrecedence(char op1, char op2) {
		if (op2 == '(' || op2 == ')')
			return false;
		if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
			return false;
		else
			return true;
	}

	// Apply an operator 'op' on operands 'a' and 'b'. Return the result.
	public int applyOp(char op, int b, int a) {
		switch (op) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '*':
			return a * b;
		case '/':
			if (b == 0)
				throw new UnsupportedOperationException("Cannot divide by zero");
			return a / b;
		}
		return 0;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/29298/simple-java-solution-with-recursion
	 * 
	 * if "(" spotted, dive one level deeper in recursion. 
	 * when it sees matching ")", return with an integer value of expression between "(...)" . 
	 * Each call of calc() handles expression within one pair of "(...)".
	 */
	public int calculate_StringTokenizer(String s) {
		return calc(new StringTokenizer(s, " ()+-", true));
	}
	int calc(StringTokenizer st) {
		int sofar = 0;
		boolean plus = true; // last seen operator.
		while (st.hasMoreTokens()) {
			int val = 0;
			String next = st.nextToken();
			switch (next) {
			case "(":
				val = calc(st);
				sofar += (plus ? val : -val);
				break;
			case ")":
				return sofar;
			case "+":
				plus = true;
				break;
			case "-":
				plus = false;
				break;
			case " ": // no-op
				break;
			default:
				val = Integer.parseInt(next);
				sofar += (plus ? val : -val);
				break;
			}
		}
		return sofar;
	}
	
	/*
	 * The following 2 variables and 5 functions are by modified myself.
	 */
	private StringTokenizer tokenizer;
	private String token;
	public void ini(String line) {
		tokenizer = new StringTokenizer(line, "+-()", true);
		token = tokenizer.nextToken();
	}
    public int calculate_self_modified(String s) {
        s = s.replaceAll(" +", "");
        ini(s);
        return Expression();
    }
    private int Parentheses() {
		int priorResult = 0;

		token = tokenizer.nextToken();
		priorResult = Expression();
		return priorResult;
	}
    private int Primary() {
		int result;

		if (token.equals("(")) {
			result = Parentheses();
		}  
		else {
			result = Integer.parseInt(token);
		}

        if (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
        }
		return result;
	}
    private int Expression() {
		int nextValue;
		int result;
		result = Primary();

		while (token.equals("+") || token.equals("-")) {
			if (token.equals("+")) {
				token = tokenizer.nextToken();
				nextValue = Primary();
				result += nextValue;
			} 
			else if (token.equals("-")) {
				token = tokenizer.nextToken();
				nextValue = Primary();
				result -= nextValue;
			}
		}
		return result;
	}

}
