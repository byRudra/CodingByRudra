class Solution {
    public int uniquePaths(int[][] grid) {
        final int MOD = 1_000_000_007;
        int m = grid.length, n = grid[0].length;

        // R[i][j]: ways to enter (i,j) moving right
        // D[i][j]: ways to enter (i,j) moving down
        long[][] R = new long[m][n];
        long[][] D = new long[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                long rOut, dOut;

                if (i == 0 && j == 0) {
                    rOut = 1;                       // start: free to go either way
                    dOut = 1;
                } else if (grid[i][j] == 0) {
                    long t = (R[i][j] + D[i][j]) % MOD;
                    rOut = t;
                    dOut = t;
                } else {                            // mirror
                    rOut = D[i][j];                 // entered down  -> turn right
                    dOut = R[i][j];                 // entered right -> turn down
                }

                if (j + 1 < n) R[i][j + 1] = (R[i][j + 1] + rOut) % MOD;
                if (i + 1 < m) D[i + 1][j] = (D[i + 1][j] + dOut) % MOD;
            }
        }

        return (int) ((R[m - 1][n - 1] + D[m - 1][n - 1]) % MOD);
    }
}