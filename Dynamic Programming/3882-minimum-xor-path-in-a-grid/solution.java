class Solution {
    public int minCost(int[][] grid) {
         int m = grid.length, n = grid[0].length;

        // dp[i][j] = all XOR values possible at cell (i, j)
        Set<Integer>[][] dp = new HashSet[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                dp[i][j] = new HashSet<>();

        dp[0][0].add(grid[0][0]);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) continue;

                // from above
                if (i > 0)
                    for (int x : dp[i - 1][j])
                        dp[i][j].add(x ^ grid[i][j]);

                // from left
                if (j > 0)
                    for (int x : dp[i][j - 1])
                        dp[i][j].add(x ^ grid[i][j]);
            }
        }

        return Collections.min(dp[m - 1][n - 1]);
    }
}