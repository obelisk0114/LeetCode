package OJ0101_0110;

/*
 * https://discuss.leetcode.com/topic/14412/java-iterative-solution
 */

import definition.TreeNode;

public class Convert_Sorted_Array_to_Binary_Search_Tree {
	// recursive Java solution
	public TreeNode sortedArrayToBST(int[] nums) {
		if (nums.length == 0) {
	        return null;
	    }
	    TreeNode head = combine(nums, 0, nums.length - 1);
	    return head;
    }
    
    private TreeNode combine(int[] nums, int low, int high) {
    	if (low > high) { // Done
            return null;
        }
        int mid = (low + high) / 2;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = combine(nums, low, mid - 1);
        node.right = combine(nums, mid + 1, high);
        return node;
	}

	/*
    public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
		int[] arr2 = {1, 2, 3, 4, 5, 6, 7};
		

	}
	*/

}
