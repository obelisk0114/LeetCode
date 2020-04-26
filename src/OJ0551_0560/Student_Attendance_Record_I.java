package OJ0551_0560;

public class Student_Attendance_Record_I {
	/*
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/101622/One-line-Java-solution-without-Regex
	 * 
	 * 1. String doesn't contain LLL.
	 * 2. The first occurrence of A equals to the last occurrence of A.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/280869/Java-1-Line
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/101552/Java-Simple-without-Regex-3-lines
	 */
	public boolean checkRecord(String s) {
		return !s.contains("LLL") && (s.indexOf("A") == s.lastIndexOf("A"));
	}
	
	// https://leetcode.com/problems/student-attendance-record-i/discuss/504549/Java-using-String.startsWith()-beats-100
	public boolean checkRecord_startsWith(String s) {
		int a = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == 'A') {
				a++;
				
				if (a > 1) {
					return false;
				}
			}

			if (s.charAt(i) == 'L' && s.startsWith("LLL", i)) {
				return false;
			}
		}
		return true;
	}
	
	// by myself
	public boolean checkRecord_self(String s) {
        int countA = 0;
        int continuousL = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'A') {
                countA++;
                continuousL = 0;
                
                if (countA > 1) {
                    return false;
                }
            }
            else if (c == 'L') {
                if (continuousL == 2) {
                    return false;
                }
                continuousL++;
            }
            else {
                continuousL = 0;
            }
        }
        return true;
    }
	
	// https://leetcode.com/problems/student-attendance-record-i/discuss/101599/Java-O(N)-solution-Accepted
	public boolean checkRecord2(String s) {
		int countA = 0;
		int continuosL = 0;
		int charA = 'A';
		int charL = 'L';
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == charA) {
				countA++;
				continuosL = 0;
			} 
			else if (s.charAt(i) == charL) {
				continuosL++;
			} 
			else {
				continuosL = 0;
			}
			
			if (countA > 1 || continuosL > 2) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/101553/Java-1-liner
	 * 
	 * Other code:
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/101594/Java-1-line-solution
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/101607/Tiny-Ruby-Short-PythonJavaC%2B%2B
	 */
	public boolean checkRecord_regex(String s) {
		return !s.matches(".*LLL.*|.*A.*A.*");
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/student-attendance-record-i/discuss/101613/Python-1-liner-without-regex
     * https://leetcode.com/problems/student-attendance-record-i/discuss/101628/Python-Straightforward-with-Explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/student-attendance-record-i/discuss/101556/C%2B%2B-very-simple-solution
     * https://leetcode.com/problems/student-attendance-record-i/discuss/114057/C%2B%2B-1-line-Regular-Expression
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/101611/Javascript-Solution
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/101610/A-few-short-JavaScript-solutions
	 * https://leetcode.com/problems/student-attendance-record-i/discuss/278470/JavaScript-beats-100
	 */

}
