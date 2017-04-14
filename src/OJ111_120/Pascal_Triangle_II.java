package OJ111_120;

import java.util.List;
import java.util.ArrayList;

public class Pascal_Triangle_II {
	/*
	 * https://discuss.leetcode.com/topic/39745/java-o-k-solution-with-explanation
	 * 
	 * Ref : 
	 * https://discuss.leetcode.com/topic/17805/another-accepted-java-solution
	 * https://discuss.leetcode.com/topic/4722/my-accepted-java-solution-any-better-code
	 */
	public List<Integer> getRow(int rowIndex) {
        List<Integer> res = new ArrayList<>();
        for(int i = 0; i <= rowIndex; i++) {
            res.add(1);
            for(int j = i-1; j > 0; j--) {
                res.set(j, res.get(j-1) + res.get(j));
            }
        }
        return res;
    }
	
	// Ref : https://discuss.leetcode.com/topic/33175/my-clean-o-k-java-solution
	public List<Integer> getRow_Binomial(int rowIndex) {
		List<Integer> result = new ArrayList<Integer>(rowIndex + 1);
		result.add(1);
		if (rowIndex == 0)
			return result;
		for (int i = 0; i < rowIndex; i++) {
			result.add(1);
		}
		long an = 1;
		for (int i = 1; i < (rowIndex + 1) / 2; i++) {
			an = 1;
			for (int j = i + 1; j <= rowIndex; j++) {
				an = an * j / (j - i);
			}
			result.set(i, (int) an);
			result.set(rowIndex - i, (int) an);
		}
		an = 1;
		if (rowIndex % 2 == 0) {
			for (int i = rowIndex / 2 + 1; i <= rowIndex; i++) {
				an = an * i / (i - rowIndex / 2);
			}
			result.set(rowIndex / 2, (int) an);
		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pascal_Triangle_II triangle2 = new Pascal_Triangle_II();
		int n = 30;
		List<Integer> number = triangle2.getRow(n);
		for (int element : number) {
			System.out.print(element + " ");
		}

	}

}
