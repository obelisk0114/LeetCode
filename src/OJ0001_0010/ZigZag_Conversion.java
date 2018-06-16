package OJ0001_0010;

public class ZigZag_Conversion {
	/*
	 * https://leetcode.com/problems/zigzag-conversion/discuss/3417/A-10-lines-one-pass-o(n)-time-o(1)-space-accepted-solution-with-detailed-explantation
	 * 
	 * P   | A   | H   | N
	 * A P | L S | I I | G
	 * Y   | I   | R   |
	 * 
	 * The size of every period: cycle = (2*nRows - 2), except nRows == 1.
	 * 
	 * In every period, every row has 2 elements, except the first and the last row.
	 * 
	 * Suppose the current row is i, the index of the first element is j:
	 * j = i + cycle*k, k = 0, 1, 2, ...
	 * 
	 * The index of the second element is secondJ:
	 * secondJ = (j - i) + cycle - i
	 * (j-i) is the start of current period, (j-i) + cycle is the start of next period.
	 */
	public String convert(String s, int nRows) {
		if (nRows <= 1)
			return s;
		
		String result = "";
		// the size of a cycle(period)
		int cycle = 2 * nRows - 2;
		for (int i = 0; i < nRows; ++i) {
			for (int j = i; j < s.length(); j = j + cycle) {
				result = result + s.charAt(j);
				int secondJ = (j - i) + cycle - i;
				
				if (i != 0 && i != nRows - 1 && secondJ < s.length())
					result = result + s.charAt(secondJ);
			}
		}
		return result;
	}

	/*
	 * https://leetcode.com/problems/zigzag-conversion/discuss/3769/O(n)-clean-java-solution-with-explaination
	 * 
	 * Take 4 rows for example. In the first row, the gap between each "column" 
	 * element is (4-1)*2 = 6. In the second row, there is another element between 
	 * each pair of "column" elements and it separate the gap to two parts. A variable 
	 * offset is used to track the position of the separator.
	 * 
	 * In the second row, initially offset equals gap - row_index * 2 = 6 - 1*2 = 4. 
	 * The first element in the second row is s[1], the second one is s[1 + offset] = 
	 * s[5]. Then offset becomes gap-offset = 6-4 = 2. So the third element is 
	 * s[5 + offset] = s[7]. In fact, the offset for the next element is always equal 
	 * to the gap - offset of the previous element.
	 * 
	 * Be careful that in the first and the last row, there is no element between the 
	 * "column" elements so we need to avoid inserting duplicate elements (offset is 0).
	 * 
	 * Rf : https://leetcode.com/problems/zigzag-conversion/discuss/3819/Two-ways-of-O(n)-solutions-one-follows-the-order-of-input-string-and-other-follows-the-order-of-output-string
	 */
	public String convert2(String s, int numRows) {
		if (numRows == 1)
			return s;
		
		int gap = (numRows - 1) << 1;
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < numRows; i++) {
			int current = i;
			int offset = gap - (i << 1);
			while (current < s.length()) {
				if (offset != 0) {
					// avoid inserting duplicate elements
					// in the first and the last row
					result.append(s.charAt(current));

					current += offset;
				}
				offset = gap - offset;
			}
		}

		return result.toString();
	}
	
	/*
	 * https://leetcode.com/problems/zigzag-conversion/discuss/3403/Easy-to-understand-Java-solution
	 * 
	 * Other code : 
	 * https://leetcode.com/problems/zigzag-conversion/discuss/3453/JAVA-solution-easy-and-clear-(-interesting-approach-)
	 * https://leetcode.com/problems/zigzag-conversion/discuss/3404/Python-O(n)-Solution-in-96ms-(99.43)
	 */
	public String convert_StringBuilder(String s, int nRows) {
		char[] c = s.toCharArray();
		int len = c.length;
		StringBuilder[] sb = new StringBuilder[nRows];
		for (int i = 0; i < sb.length; i++)
			sb[i] = new StringBuilder();

		int i = 0;
		while (i < len) {
			for (int idx = 0; idx < nRows && i < len; idx++) // vertically down
				sb[idx].append(c[i++]);
			for (int idx = nRows - 2; idx >= 1 && i < len; idx--) // obliquely up
				sb[idx].append(c[i++]);
		}
		for (int idx = 1; idx < sb.length; idx++)
			sb[0].append(sb[idx]);
		return sb[0].toString();
	}
	
	/*
	 * https://leetcode.com/problems/zigzag-conversion/discuss/3531/Share-my-Java-solution-with-comments
	 * 
	 * It is necessary to initialize the StringBuilder array. 
	 * I tried not to initialize it, but nullPointer error occurred.
	 * 
	 * Rf : leetcode.com/problems/zigzag-conversion/discuss/3531/Share-my-Java-solution-with-comments/4225
	 */
	public String convert_chunk(String s, int nRows) {
		int len = s.length();
		if (len <= nRows || nRows <= 1)
			return s; // such condition can't form the zigzag route.

		StringBuilder[] result = new StringBuilder[nRows]; // string buffer array to hold each row's result

		// initialize the string buffer
		for (int i = 0; i < result.length; i++) {
			result[i] = new StringBuilder();
		}

		// divide the groups into chunks with size (nRows*2-2).
		int chunk = nRows * 2 - 2; // 3->4, 4->6, 5->7, etc.

		for (int i = 0; i < len; i++) {
			int group = i % chunk; // get the index of the element in the chunk

			// if they are less then nRows, this element is vertically aligned from top to
			// buttom
			if (group < nRows) {
				result[group].append(s.charAt(i));
			}
			// otherwise, it falls onto the slope in reversed direction
			else {
				result[chunk - group].append(s.charAt(i));
			}
		}

		// combine the groups into final array.
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nRows; i++) {
			sb.append(result[i].toString());
		}
		return sb.toString();
	}
	
	// by myself
	public String convert_self(String s, int numRows) {
        if (s.length() < 2 || numRows == 1)
            return s;
        
        int length = 0;
        int part = numRows + numRows - 2;
        int tmp = s.length() / part;
        if (s.length() % part < numRows)
            length = tmp * (numRows - 1) + 1;
        else
            length = tmp * (numRows - 1) + (s.length() % part) - numRows + 2;
        
        char[][] map = new char[numRows][length];
        for (int i = 0; i < s.length(); i++) {
            int q = i / part;
            int r = i % part;
            
            int row = 0;
            int col = 0;
            if (r < numRows) {
                col = q * (numRows - 1);
                row = r;
            }
            else {
                col = q * (numRows - 1) + r - numRows + 1;
                row = numRows - (r - numRows + 2);
            }
            map[row][col] = s.charAt(i);
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != '\u0000') {
                    sb.append(map[i][j]);
                }
            }
        }
        
        return sb.toString();
    }

}
