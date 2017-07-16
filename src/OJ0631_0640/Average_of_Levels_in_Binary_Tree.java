package OJ0631_0640;

import definition.TreeNode;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Average_of_Levels_in_Binary_Tree {
	// https://discuss.leetcode.com/topic/95214/java-bfs-solution
	public List<Double> averageOfLevels(TreeNode root) {
	    List<Double> result = new ArrayList<>();
	    Queue<TreeNode> q = new LinkedList<>();
	    
	    if(root == null) return result;
	    q.add(root);
	    while(!q.isEmpty()) {
	        int n = q.size();
	        double sum = 0.0;
	        for(int i = 0; i < n; i++) {
	            TreeNode node = q.poll();
	            sum += node.val;
	            if(node.left != null) q.offer(node.left);
	            if(node.right != null) q.offer(node.right);
	        }
	        result.add(sum / n);
	    }
	    return result;
	}
	
	// Self
	public List<Double> averageOfLevels_self(TreeNode root) {
		List<Double> out = new ArrayList<>();
        LinkedList<TreeNode> store = new LinkedList<>();
        store.add(root);
        double sum = 0;
        int counter = 1;
        int length = 1;
        while (!store.isEmpty()) {
            TreeNode tmp = store.removeFirst();
            sum += tmp.val;
            if (tmp.left != null) {
                store.add(tmp.left);
            }
            if (tmp.right != null) {
                store.add(tmp.right);
            }
            counter--;
            if (counter == 0) {
                counter = store.size();
                double element = sum / length;
                out.add(element);
                sum = 0;
                length = store.size();
            }
        }
        return out;
	}
	
	/*
	 * BFS
	 * https://leetcode.com/articles/average-of-levels/
	 */
	public List < Double > averageOfLevels_BFS(TreeNode root) {
        List < Double > res = new ArrayList < > ();
        Queue < TreeNode > queue = new LinkedList < > ();
        queue.add(root);
        while (!queue.isEmpty()) {
            long sum = 0, count = 0;
            Queue < TreeNode > temp = new LinkedList < > ();
            while (!queue.isEmpty()) {
                TreeNode n = queue.remove();
                sum += n.val;
                count++;
                if (n.left != null)
                    temp.add(n.left);
                if (n.right != null)
                    temp.add(n.right);
            }
            queue = temp;
            res.add(sum * 1.0 / count);
        }
        return res;
    }
	
	/*
	 * DFS
	 * The following 2 functions are from this link.
	 * https://leetcode.com/articles/average-of-levels/
	 */
	public List<Double> averageOfLevels_DFS(TreeNode root) {
		List<Integer> count = new ArrayList<>();
		List<Double> res = new ArrayList<>();
		average(root, 0, res, count);
		for (int i = 0; i < res.size(); i++)
			res.set(i, res.get(i) / count.get(i));
		return res;
	}
	public void average(TreeNode t, int i, List<Double> sum, List<Integer> count) {
		if (t == null)
			return;
		if (i < sum.size()) {
			sum.set(i, sum.get(i) + t.val);
			count.set(i, count.get(i) + 1);
		} else {
			sum.add(1.0 * t.val);
			count.add(1);
		}
		average(t.left, i + 1, sum, count);
		average(t.right, i + 1, sum, count);
	}

}
