class Solution {
    public long[] resultArray(int[] nums, int k) {
        long[] result = new long[k];

        // dp[r] = number of subarrays ending at the previous index
        // whose product % k == r
        long[] dp = new long[k];

        for (int num : nums) {
            long[] next = new long[k];

            int rem = num % k;

            // Start a new subarray with nums[i]
            next[rem]++;

            // Extend every previous subarray
            for (int r = 0; r < k; r++) {
                if (dp[r] != 0) {
                    int newRem = (int)((long) r * rem % k);
                    next[newRem] += dp[r];
                }
            }

            // Add all subarrays ending at this position
            for (int r = 0; r < k; r++) {
                result[r] += next[r];
            }

            dp = next;
        }

        return result;
    }
}