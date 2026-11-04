class Solution {
    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;

        // coordinate compression
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int[] vals = new int[n];
        int m = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) vals[m++] = sorted[i];
        }
        int[] id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = Arrays.binarySearch(vals, 0, m, nums[i]) + 1; // 1-indexed
        }

        // Fenwick trees: counts and sums
        int[] cnt = new int[m + 1];
        long[] sum = new long[m + 1];
        int top = Integer.highestOneBit(m);
        int target = (x + 1) / 2; // lower median position

        // cost[i] = min ops to equalize window nums[i..i+x-1]
        long[] cost = new long[n - x + 1];
        long windowSum = 0;

        for (int i = 0; i < n; i++) {
            for (int p = id[i]; p <= m; p += p & -p) { cnt[p]++; sum[p] += nums[i]; }
            windowSum += nums[i];

            if (i >= x) {
                int old = i - x;
                for (int p = id[old]; p <= m; p += p & -p) { cnt[p]--; sum[p] -= nums[old]; }
                windowSum -= nums[old];
            }

            if (i >= x - 1) {
                // find the target-th smallest element via binary lifting
                int pos = 0, rem = target;
                long below = 0;
                for (int pw = top; pw > 0; pw >>= 1) {
                    int nxt = pos + pw;
                    if (nxt <= m && cnt[nxt] < rem) {
                        pos = nxt;
                        rem -= cnt[nxt];
                        below += sum[nxt];
                    }
                }
                long med = vals[pos];
                long leftSum = below + (long) rem * med;  // sum of the `target` smallest
                long rightSum = windowSum - leftSum;

                cost[i - x + 1] = med * target - leftSum
                                + rightSum - med * (x - target);
            }
        }

        // DP over non-overlapping windows
        long INF = Long.MAX_VALUE / 4;
        long[][] dp = new long[k + 1][n + 1];
        for (int j = 1; j <= k; j++) Arrays.fill(dp[j], INF);

        for (int j = 1; j <= k; j++) {
            for (int i = 1; i <= n; i++) {
                dp[j][i] = dp[j][i - 1];
                if (i >= x && dp[j - 1][i - x] < INF) {
                    dp[j][i] = Math.min(dp[j][i], dp[j - 1][i - x] + cost[i - x]);
                }
            }
        }

        return dp[k][n];
    }
}