package OJ0061_0070;

import java.util.ArrayList;

public class Valid_Number {
	// https://discuss.leetcode.com/topic/8029/a-clean-design-solution-by-using-design-pattern
	/*
	 * https://discuss.leetcode.com/topic/9490/clear-java-solution-with-ifs
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/19412/ac-java-solution-with-clear-explanation
	 * https://discuss.leetcode.com/topic/40983/java-logically-simple-flexible-and-clear-solution-including-rules-of-a-valid-number
	 */
	public boolean isNumber_scan(String s) {
	    s = s.trim();
	    
	    boolean pointSeen = false;
	    boolean eSeen = false;
	    boolean numberSeen = false;
	    boolean numberAfterE = true;
	    for(int i=0; i<s.length(); i++) {
	        if('0' <= s.charAt(i) && s.charAt(i) <= '9') {
	            numberSeen = true;
	            numberAfterE = true;
	        } else if(s.charAt(i) == '.') {
	            if(eSeen || pointSeen) {
	                return false;
	            }
	            pointSeen = true;
	        } else if(s.charAt(i) == 'e') {
	            if(eSeen || !numberSeen) {
	                return false;
	            }
	            numberAfterE = false;
	            eSeen = true;
	        } else if(s.charAt(i) == '-' || s.charAt(i) == '+') {
	            if(i != 0 && s.charAt(i-1) != 'e') {
	                return false;
	            }
	        } else {
	            return false;
	        }
	    }
	    
	    return numberSeen && numberAfterE;
	}
	
	/*
	 * The following 4 functions are by myself.
	 * 
	 * Rf :
	 * https://stackoverflow.com/a/35242882
	 */
	public boolean isNumber_self2(String s) {
        int e = (int) (countCharNumber_self2(s, 'e') + countCharNumber_self2(s, 'E'));
        
        if (e > 1) {
            return false;
        }
        else if (e == 0) {
            return isDecimal_self2(s);
        }
        else {
            int cut = s.indexOf('e') == -1 ? s.indexOf('E') : s.indexOf('e');
            
            String s1 = s.substring(0, cut);
            String s2 = s.substring(cut + 1);
            
            return isDecimal_self2(s1) && isInteger_self2(s2);
        }
    }
    
    private long countCharNumber_self2(String s, char c) {
        return s.chars().filter(ch -> ch == c).count();
    }
    
    private boolean isDecimal_self2(String s) {
        if (s.length() == 0) {
            return false;
        }
        if (s.length() == 1) {
            return Character.isDigit(s.charAt(0));
        }
        
        char first = s.charAt(0);
        
        if (first == '+' || first == '-' || first == '.' 
        		|| Character.isDigit(first)) {
        	
            int dotNumber = (int) countCharNumber_self2(s, '.');
            
            if (dotNumber == 0) {
                return isInteger_self2(s);
            }
            else if (dotNumber == 1) {
                if ((first == '+' || first == '-') && s.length() == 2) {
                    return false;
                }
                
                for (int i = 1; i < s.length(); i++) {
                    char c = s.charAt(i);
                    
                    if (!Character.isDigit(c) && c != '.') {
                        return false;
                    }
                }
                
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    
    private boolean isInteger_self2(String s) {
        if (s.length() == 0) {
            return false;
        }
        if (s.length() == 1) {
            return Character.isDigit(s.charAt(0));
        }
        
        char first = s.charAt(0);
        
        if (first == '+' || first == '-' || Character.isDigit(first)) {
            for (int i = 1; i < s.length(); i++) {
                if (!Character.isDigit(s.charAt(i))) {
                    return false;
                }
            }
            
            return true;
        }
        else {
            return false;
        }
    }
	
	/*
	 * Self
	 * http://www.asciitable.com/
	 */
	public boolean isNumber_self(String s) {
        int dot = -1;         // .
        int decimal = -1;     // e
        ArrayList<Integer> sign = new ArrayList<Integer>();     // position
        s = s.trim();
        //System.out.println("\'" + s + "\'");
        if (s.equals(""))
        	return false;
        if (s.equals("-.") || s.equals("+."))
        	return false;
        int ascii = (int) s.charAt(0);
        if (ascii < 48 || ascii > 57) {
        	if (s.length() == 1)
        		return false;
        	if (ascii == 46) {
        		dot = 0;
        	}
        	else if (ascii == 45 || ascii == 43) {   // "-" or "+"
        		sign.add(0);
        	}
        	else
        		return false;        		
        }
        if (s.charAt(s.length() - 1) == 'e' || s.charAt(s.length() - 1) == '+' || s.charAt(s.length() - 1) == '-') {
        	return false;
        }
        for (int i = 1; i < s.length(); i++) {
            ascii = (int) s.charAt(i);
            if (ascii > 47 && ascii < 58)
                continue;
            else if (ascii == 46) {     // "."
                if (dot != -1)
                    return false;
                if (decimal < i && decimal != -1)
                	return false;
                dot = i;
            }
            else if (ascii == 101) {    // "e"
                if (decimal != -1)
                    return false;
                if (dot == i-1 && i == 1)
                	return false;
                if (i == 1 && !sign.isEmpty()) {
                	return false;
                }
                else if (i == 2 && (!sign.isEmpty() && dot != -1)) {
                	return false;
                }
                decimal = i;
            }
            else if (ascii == 45 || ascii == 43) {   // "-" or "+"
        		if (sign.size() >= 2)
        			return false;
        		if (decimal != i-1 && decimal != -1)
        			return false;
        		if (decimal == -1)
        			return false;
        		sign.add(i);
        	}
            else {
            	System.out.println("else; i = " + i + " ; ascii = " + ascii);
                return false;
            }
        }
        return true;   
    }
	
	/*
	 * https://discuss.leetcode.com/topic/21037/java-regular-expression-hopefully-this-one-is-more-understandable
	 * 
	 * Rf : https://discuss.leetcode.com/topic/2973/java-solution-with-one-line
	 * return s.matches("(\\s*)[+-]?((\\.[0-9]+)|([0-9]+(\\.[0-9]*)?))(e[+-]?[0-9]+)?(\\s*)");
	 */
	public boolean isNumber_regex(String s) {
		s = s.trim();
		if (s.length() == 0)
			return false;
		if (s.matches("[+-]?(([0-9]*\\.?[0-9]+)|([0-9]+\\.?[0-9]*))([eE][+-]?[0-9]+)?"))
			return true;
		else
			return false;
	}
	
	// https://discuss.leetcode.com/topic/2973/java-solution-with-one-line/4
	public boolean isNumber_cheat(String s) {
	    try {
	        s = s.trim();
	        int n = s.length();
	        if ( n == 0 || (s.charAt(n-1) != '.' && (s.charAt(n-1) - '0' < 0 || s.charAt(n-1) - '0' > 9 )) ) {
	            return false;
	        }
	        double i = Double.parseDouble(s);
	        System.out.println(i);
	        return true;
	    }
	    catch (NumberFormatException e) {
	        return false;
	    }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Valid_Number validNumber = new Valid_Number();
		//String s = "e";         false
		//String s = "e9";        false
		//String s = "1 ";
		//String s = ".1";        true
		//String s = "0e";        false
		//String s = "-1.";       true
		//String s = "+.8";       true
		//String s = " -.";       false
		//String s = "46.e3";     true
		//String s = ".e1";       false
		//String s = "6e6.5";     false
		//String s = "-e58 ";     // false
		//String s = " 005047e+6";  true
		//String s = "4e+";       false
		String s = ".2e81";      // true
		System.out.println("Valid or not : " + validNumber.isNumber_scan(s));
		System.out.println("Valid or not : " + validNumber.isNumber_self(s));
		System.out.println("Valid or not : " + validNumber.isNumber_regex(s));
		System.out.println("Valid or not : " + validNumber.isNumber_cheat(s));
	}

}
