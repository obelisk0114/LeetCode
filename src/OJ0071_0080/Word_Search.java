package OJ0071_0080;


public class Word_Search {
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-search/discuss/27811/my-java-solution
	 */
	public boolean exist_self(char[][] board, String word) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (dfs_self(board, i, j, word, 0, visited)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean dfs_self(char[][] board, int i, int j, String word, int start, 
    		boolean[][] visited) {
    	
        if (start == word.length())
            return true;
        
        if (i < 0 || i == board.length || j < 0 || j == board[0].length 
        		|| visited[i][j])
            return false;
        
        if (word.charAt(start) != board[i][j])
            return false;
        
        visited[i][j] = true;
        
        boolean down = dfs_self(board, i + 1, j, word, start + 1, visited);
        boolean up = dfs_self(board, i - 1, j, word, start + 1, visited);
        boolean left = dfs_self(board, i, j - 1, word, start + 1, visited);
        boolean right = dfs_self(board, i, j + 1, word, start + 1, visited);
        if (down || up || left || right) {
            return true;
        }
        
        // Recover for next start point
        visited[i][j] = false;
        return false;
    }
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/21142/my-java-solution
	 */
	static boolean[][] visited;
	public boolean exist2(char[][] board, String word) {
		visited = new boolean[board.length][board[0].length];
		
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if((word.charAt(0) == board[i][j]) && search2(board, word, i, j, 0)){
					return true;
				}
			}
		}
		
		return false;
	}
	private boolean search2(char[][]board, String word, int i, int j, int index){
		if(index == word.length()){
			return true;
		}
		
		if(i >= board.length || i < 0 || j >= board[i].length || j < 0 || 
				board[i][j] != word.charAt(index) || visited[i][j]){
			return false;
		}
		
		visited[i][j] = true;
		if(search2(board, word, i-1, j, index+1) || 
				search2(board, word, i+1, j, index+1) ||
				search2(board, word, i, j-1, index+1) || 
				search2(board, word, i, j+1, index+1)){
			return true;
		}
		
		visited[i][j] = false;
		return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/25591/simple-solution
	 */
	public boolean exist3(char[][] board, String word) {
	    for (int i = 0; i < board.length; i++) {
	        for (int j = 0; j < board[i].length; j++) {
	            if(exist3(board, i, j, word, 0)) return true;
	        }
	    }
	    return false;
	}
	private boolean exist3(char[][] board, int x, int y, String word, int start) {
	    if(start == word.length()) return true;
	    if(x < 0 || x == board.length || y < 0 || y == board[x].length) return false;
	    if (board[x][y] == word.charAt(start++)) {
	        char c = board[x][y];
	        board[x][y] = '#';
	        boolean res = exist3(board, x + 1, y, word, start) || exist3(board, x - 1, y, word, start) ||
	        exist3(board, x, y + 1, word, start) || exist3(board, x, y - 1, word, start);
	        board[x][y] = c;
	        return res;
	    }
	    return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/7907/accepted-very-short-java-solution-no-additional-space
	 * 
	 * It is like setting board[x][y] = '#'. 
	 * The range of char is between 0 - 255. 
	 * By doing xor with 256, board[x][y] becomes a number >= 256 
	 * and thus is different from any character.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/45252/java-dfs-solution-beats-97-64/3
	 */
	public boolean exist_bit(char[][] board, String word) {
		char[] w = word.toCharArray();
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[y].length; x++) {
				if (exist_bit(board, y, x, w, 0))
					return true;
			}
		}
		return false;
	}
	private boolean exist_bit(char[][] board, int y, int x, char[] word, int i) {
		if (i == word.length)
			return true;
		if (y < 0 || x < 0 || y == board.length || x == board[y].length)
			return false;
		if (board[y][x] != word[i])
			return false;
		board[y][x] ^= 256;
		boolean exist = exist_bit(board, y, x + 1, word, i + 1) || 
				        exist_bit(board, y, x - 1, word, i + 1) || 
				        exist_bit(board, y + 1, x, word, i + 1) || 
				        exist_bit(board, y - 1, x, word, i + 1);
		board[y][x] ^= 256;     // (x ^ y) ^ y = x
		return exist;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Word_Search search = new Word_Search();
		char[][] board = {{'A', 'B', 'Q'}, {'X', 'C'}, {'T', 'A'}, {'G'}};
		String s = "ATCG";
//		char[][] board = {"ab", "cd"};
//		String s = "abcd";
		System.out.println(search.exist_bit(board, s));
	}

}
