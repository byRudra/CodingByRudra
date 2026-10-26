class Solution {
    public int minNumber(int[] nums1, int[] nums2) {
        int min1 = 10, min2 = 10;
        int common = 10;

        for (int a : nums1) {
            min1 = Math.min(min1, a);

            for (int b : nums2) {
                if (a == b) {
                    common = Math.min(common, a);
                }
            }
        }

        if (common != 10) {
            return common;
        }

        for (int b : nums2) {
            min2 = Math.min(min2, b);
        }

        return Math.min(min1, min2) * 10 + Math.max(min1, min2);
    
    }
}