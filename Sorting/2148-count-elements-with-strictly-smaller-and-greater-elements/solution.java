class Solution {
    public int countElements(int[] nums) {
        int lo = Integer.MAX_VALUE, hi = Integer.MIN_VALUE;
        for (int x : nums) {
            lo = Math.min(lo, x);
            hi = Math.max(hi, x);
        }

        int count = 0;
        for (int x : nums) {
            if (x > lo && x < hi) count++;
        }
        return count;
    }
}