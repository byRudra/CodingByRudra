class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        int total = 0;

        for(int num : nums){
            total += num;
        }

        // Impossible case 
        if(target + total < 0 || (target  + total) % 2 != 0){
            return 0;
        }

        int realTarget = (target + total) / 2;
        int dp[][] = new int[n + 1][realTarget + 1];
        for(int i = 0; i <= n; i++){
            dp[i][0] = 1;
        }
        for(int i = 1; i <= n; i++){
            for(int j = 0; j <= realTarget; j++){
                dp[i][j] = dp[i - 1][j];

                if(nums[i - 1] <= j){
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[n][realTarget];
    }
}