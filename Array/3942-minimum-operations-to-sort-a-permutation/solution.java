class Solution {
    public int minOperations(int[] nums) {
        int n = nums.length;
        boolean inc = true, dec = true;
        for (int i = 0; i < n; i++) {
            int next = nums[(i + 1) % n];
            if (next != (nums[i] + 1) % n) inc = false;
            if (next != (nums[i] - 1 + n) % n) dec = false;
        }

        int best = Integer.MAX_VALUE;
        if (inc) {
            int k = nums[0];
            best = (k == 0) ? 0 : Math.min(n - k, k + 2);
        }
        if (dec) {
            int k = (nums[0] + 1) % n;
            best = Math.min(best, 1 + Math.min(k, (n - k) % n));
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }
}