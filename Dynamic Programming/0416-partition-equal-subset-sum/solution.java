class Solution {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums)
            totalSum += num;

        int target = totalSum / 2;
        if (totalSum % 2 != 0) {
            return false;
        }
        int n = nums.length;
        boolean dp[][] = new boolean[n + 1][target + 1];
        // initializing dp
        for (int i = 1; i <= target; i++) {
            dp[0][i] = false;
        }
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // Don't take
                dp[i][j] = dp[i - 1][j];

                // check if taking will help or not
                if (nums[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[n][target];
    }
}