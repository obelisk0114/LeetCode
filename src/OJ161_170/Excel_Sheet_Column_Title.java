package OJ161_170;

public class Excel_Sheet_Column_Title {
	/*
	 * Self
	 * 
	 * Rf : https://discuss.leetcode.com/topic/6248/accepted-java-solution
	 */
	public String convertToTitle(int n) {
        StringBuilder title = new StringBuilder();
        while(--n >= 0) {
            title.insert(0, (char)('A' + n % 26));
            n /= 26;
        }
        return title.toString();
    }
	
	/*
	 * https://discuss.leetcode.com/topic/13298/share-my-java-solusion
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/9018/my-short-java-solution-without-using-string-buffer-and-string-builder
	 * https://discuss.leetcode.com/topic/35360/my-easy-to-understand-java-solution
	 */
	public String convertToTitle2(int n) {
		String res = "";
		while (n != 0) {
			res = (char) ('A' + (n - 1) % 26) + res;
			n = (n - 1) / 26;
		}
		return res;
	}
	
	// https://discuss.leetcode.com/topic/6214/my-1-lines-code-in-java-c-and-python
	public String convertToTitle3(int n) {
		return n == 0 ? "" : convertToTitle(--n / 26) + (char)('A' + (n % 26));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Excel_Sheet_Column_Title excelTitle = new Excel_Sheet_Column_Title();
		int a = 37;
		System.out.println(excelTitle.convertToTitle(a));
		System.out.println(excelTitle.convertToTitle2(a));
		System.out.println(excelTitle.convertToTitle3(a));
	}

}
