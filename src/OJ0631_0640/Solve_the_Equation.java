package OJ0631_0640;

import java.util.List;
import java.util.ArrayList;

public class Solve_the_Equation {
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/articles/solve-the-equation/
	 */
	public String coeff(String x) {
		// ax -> a
        if (x.length() > 1 && x.charAt(x.length() - 2) >= '0' && x.charAt(x.length() - 2) <= '9')
            return x.replace("x", "");
        return x.replace("x", "1");
    }
    public String solveEquation(String equation) {
        String[] lr = equation.split("=");
        int lhs = 0, rhs = 0;
        for (String x: breakIt(lr[0])) {
            if (x.contains("x")) {
                lhs += Integer.parseInt(coeff(x));
            } else
                rhs -= Integer.parseInt(x);
        }
        for (String x: breakIt(lr[1])) {
            if (x.contains("x"))
                lhs -= Integer.parseInt(coeff(x));
            else
                rhs += Integer.parseInt(x);
        }
        if (lhs == 0) {
            if (rhs == 0)
                return "Infinite solutions";
            else
                return "No solution";
        }
        return "x=" + rhs / lhs;
    }
    public List < String > breakIt(String s) {
        List < String > res = new ArrayList < > ();
        String r = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                if (r.length() > 0)                        // deal with -x=-1
                    res.add(r);
                r = "" + s.charAt(i);
            } else
                r += s.charAt(i);
        }
        res.add(r);
        return res;
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/articles/solve-the-equation/
     * 
     * regular expression
     */
	public String coeff_regex(String x) {
		if (x.length() > 1 && x.charAt(x.length() - 2) >= '0' && x.charAt(x.length() - 2) <= '9')
			return x.replace("x", "");
		return x.replace("x", "1");
	}
	public String solveEquation_regex(String equation) {
		String[] lr = equation.split("=");
		int lhs = 0, rhs = 0;
		for (String x : lr[0].split("(?=\\+)|(?=-)")) {
			if (x.indexOf("x") >= 0) {

				lhs += Integer.parseInt(coeff(x));
			} else
				rhs -= Integer.parseInt(x);
		}
		for (String x : lr[1].split("(?=\\+)|(?=-)")) {
			if (x.indexOf("x") >= 0)
				lhs -= Integer.parseInt(coeff(x));
			else
				rhs += Integer.parseInt(x);
		}
		if (lhs == 0) {
			if (rhs == 0)
				return "Infinite solutions";
			else
				return "No solution";
		} else
			return "x=" + rhs / lhs;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solve_the_Equation solve = new Solve_the_Equation();
		//String s = "x+5-3x=2x";
		String s = "-x=-2";
		System.out.println(solve.solveEquation(s));
		System.out.println("---------------------");
		System.out.println("plus sign (+8) : " + Integer.parseInt("+8"));
		System.out.println("minus sign (-8) : " + Integer.parseInt("-8"));
		System.out.println("no sign (7) : " + Integer.parseInt("7"));

	}

}
