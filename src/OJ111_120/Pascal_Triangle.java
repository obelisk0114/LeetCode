package OJ111_120;

import java.util.List;
import java.util.ArrayList;

public class Pascal_Triangle {
	public List<List<Integer>> generate(int numRows) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if (numRows <= 0)
			return result;
		for (int i = 1; i <= numRows; i++) {
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.add(1);
			for (int j = 2; j <= i; j++) {
				int an = result.get(i - 2).get(j - 2);
				if (j != i) {
					an += result.get(i - 2).get(j - 1);
				}
				tmp.add(an);
			}
			result.add(tmp);
		}
		return result;
	}
	
	// https://discuss.leetcode.com/topic/32464/1ms-java-solution-simple
	public List<List<Integer>> generate2(int numRows) {
        List<List<Integer>> sol = new ArrayList<>();
        if (numRows == 0) return sol;
        
        List<Integer> row = new ArrayList<>();
        row.add(1);
        sol.add(row);
        
        for (int i = 1; i < numRows; i++) {
            List<Integer> r = new ArrayList<>();
            r.add(1);
            List<Integer> p = sol.get(i-1);
            for (int j = 0; j < p.size()-1; j++) {
                r.add(p.get(j) + p.get(j+1));
            }
            r.add(1);
            sol.add(r);
        }        
        return sol;
    }
	
	// https://discuss.leetcode.com/topic/6805/my-concise-solution-in-java
	public List<List<Integer>> generate3(int numRows) {
		List<List<Integer>> allrows = new ArrayList<List<Integer>>();
		ArrayList<Integer> row = new ArrayList<Integer>();
		for(int i=0;i<numRows;i++)
		{
			row.add(0, 1);
			for(int j=1;j<row.size()-1;j++)
				row.set(j, row.get(j)+row.get(j+1));
			allrows.add(new ArrayList<Integer>(row));
		}
		return allrows;
		
	}
	
	// https://discuss.leetcode.com/topic/22369/200ms-java-simple-loop-solution
	
	public static void main(String[] args) {
		Pascal_Triangle triangle = new Pascal_Triangle();
		int n = 6;
		List<List<Integer>> out = triangle.generate(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < out.get(i).size(); j++) {
				System.out.print(out.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}

}
