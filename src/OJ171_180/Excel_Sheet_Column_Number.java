package OJ171_180;

public class Excel_Sheet_Column_Number {
	public int titleToNumber(String s) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            ans = ans * 26 + (int) (s.charAt(i) - 'A' + 1);
        }
        return ans;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/17923/one-line-in-java
	 * 
	 * Rf : https://discuss.leetcode.com/topic/6458/my-solutions-in-3-languages-does-any-one-have-one-line-solution-in-java-or-c/2
	 */
	public int titleToNumber2(String s) {
		return s.length()==0?0:(s.charAt(s.length()-1)-'A'+1)+26*titleToNumber(s.substring(0, s.length()-1));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Excel_Sheet_Column_Number excelColumn = new Excel_Sheet_Column_Number();
		String s = "SA";
		System.out.println(excelColumn.titleToNumber(s));
		System.out.println(excelColumn.titleToNumber2(s));

	}

}
