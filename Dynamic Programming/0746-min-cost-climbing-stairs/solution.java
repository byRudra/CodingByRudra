class Solution {
    public int minCostClimbingStairs(int[] cost) {
        int dp[] = new int[cost.length + 2];
        for(int i = cost.length - 1; i >= 0; i--){
            dp[i] = Math.min(dp[i + 1], dp[i + 2]) + cost[i];
        }
        return Math.min(dp[0], dp[1]);
    }
}