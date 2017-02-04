package OJ081_090;

public class Merge_Sorted_Array {
	// Use extra O(m) to store
	void merge(int[] nums1, int m, int[] nums2, int n) {
		int[] tmp = new int[m];
		for (int i1 = 0; i1 < m; i1++) {
			tmp[i1] = nums1[i1];
		}
		int i = 0;
		int j = 0;
		int k = 0;
		while (i < m && j < n) {
			if (tmp[i] < nums2[j]) {
				nums1[k++] = tmp[i++];
			}
			else {
				nums1[k++] = nums2[j++];
			}
		}
		while (i < m) {
			nums1[k++] = tmp[i++];
		}
		while (j < n) {
			nums1[k++] = nums2[j++];
		}
	}

	// Merge to nums1 from the left end
	void merge2(int[] nums1, int m, int[] nums2, int n) {
		int i = m - 1;
		int j = n - 1;
		int k = m + n - 1;
		while (i >= 0 && j >= 0) {
			if (nums1[i] > nums2[j]) {
				nums1[k--] = nums1[i--];
			}
			else {
				nums1[k--] = nums2[j--];
			}
		}
		while (i >= 0) {
			nums1[k--] = nums1[i--];
		}
		while (j >= 0) {
			nums1[k--] = nums2[j--];
		}
	}

}
