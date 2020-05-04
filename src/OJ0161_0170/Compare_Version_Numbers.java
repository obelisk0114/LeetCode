package OJ0161_0170;

public class Compare_Version_Numbers {
	/*
	 * https://leetcode.com/problems/compare-version-numbers/discuss/50811/Simple-JAVA-Solution
	 * 
	 * Split the string with regex "." (it was written "\." since "." only means any 
	 * character), then looping, I tried to find out the value of the version using 
	 * parseInt.
	 * If one version has a lesser subversion than the others, it will be filled with 
	 * zeros.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/compare-version-numbers/discuss/50774/Accepted-small-Java-solution.
	 * https://leetcode.com/problems/compare-version-numbers/discuss/50994/Java-solution-with-fewer-if-logic
	 * https://leetcode.com/problems/compare-version-numbers/discuss/50959/My-solutions-in-3-languages
	 */
	public int compareVersion_fill(String version1, String version2) {
		String[] v1 = version1.split("\\.");
		String[] v2 = version2.split("\\.");

		int longest = v1.length > v2.length ? v1.length : v2.length;

		for (int i = 0; i < longest; i++) {
			int ver1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
			int ver2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;

			if (ver1 > ver2)
				return 1;
			else if (ver1 < ver2)
				return -1;
		}
		return 0;
	}
	
	/*
	 * https://leetcode.com/problems/compare-version-numbers/discuss/50788/My-JAVA-solution-without-split
	 * 
	 * Other code:
	 * https://leetcode.com/problems/compare-version-numbers/discuss/51012/Solution-using-two-pointer-(Java)
	 */
	public int compareVersion_2_pointer(String version1, String version2) {
		int temp1 = 0, temp2 = 0;
		int len1 = version1.length(), len2 = version2.length();
		int i = 0, j = 0;
		while (i < len1 || j < len2) {
			temp1 = 0;
			temp2 = 0;
			
			while (i < len1 && version1.charAt(i) != '.') {
				temp1 = temp1 * 10 + version1.charAt(i++) - '0';
			}
			while (j < len2 && version2.charAt(j) != '.') {
				temp2 = temp2 * 10 + version2.charAt(j++) - '0';
			}
			
			if (temp1 > temp2)
				return 1;
			else if (temp1 < temp2)
				return -1;
			else {
				i++;
				j++;
			}
		}
		return 0;
	}
	
	// https://leetcode.com/problems/compare-version-numbers/discuss/50953/7-lines-simple-java-solution
	public int compareVersion_loop3(String version1, String version2) {
		String[] v1 = version1.split("\\."), v2 = version2.split("\\.");
		int i;
		
		for (i = 0; i < v1.length && i < v2.length; i++)
			if (Integer.parseInt(v1[i]) != Integer.parseInt(v2[i]))
				return Integer.parseInt(v1[i]) > Integer.parseInt(v2[i]) ? 1 : -1;
				
		for (; i < v1.length; i++)
			if (Integer.parseInt(v1[i]) != 0)
				return 1;
		
		for (; i < v2.length; i++)
			if (Integer.parseInt(v2[i]) != 0)
				return -1;
		return 0;
	}
	
	// by myself
	public int compareVersion_self(String version1, String version2) {
        String[] ver1 = version1.split("\\.");
        String[] ver2 = version2.split("\\.");
        int i1 = 0;
        int i2 = 0;
        
        while (i1 < ver1.length || i2 < ver2.length) {
            int cur1 = 0;
            int cur2 = 0;
            if (i1 < ver1.length) {
                cur1 = Integer.parseInt(ver1[i1]);
            }
            if (i2 < ver2.length) {
                cur2 = Integer.parseInt(ver2[i2]);
            }
            
            if (cur1 < cur2) {
                return -1;
            }
            else if (cur1 > cur2) {
                return 1;
            }
            
            i1++;
            i2++;
        }
        
        return 0;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/compare-version-numbers/discuss/50952/Python-10-lines-solution
     * https://leetcode.com/problems/compare-version-numbers/discuss/50800/2-4-lines-Python-3-different-ways
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/compare-version-numbers/discuss/50943/C%2B%2B-solution-(10-lines).-very-clean-coding-using-istringstream-to-read-input
     * https://leetcode.com/problems/compare-version-numbers/discuss/50804/10-line-concise-solution.-(C%2B%2B)
     * https://leetcode.com/problems/compare-version-numbers/discuss/50767/My-2ms-easy-solution-with-CC%2B%2B
     * https://leetcode.com/problems/compare-version-numbers/discuss/50980/My-solution-in-C(-0-ms)-using-only-while-loop
     */

}
