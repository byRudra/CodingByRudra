class Solution {
    public int[] maximumWeight(List<List<Integer>> intervals) {
        int n = intervals.size();

        // [end, start, weight, originalIndex]
        int[][] arr = new int[n][4];

        for (int i = 0; i < n; i++) {
            arr[i][0] = intervals.get(i).get(1); // end
            arr[i][1] = intervals.get(i).get(0); // start
            arr[i][2] = intervals.get(i).get(2); // weight
            arr[i][3] = i; // original index
        }

        // Sort by end
        Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));

        long[][] dp = new long[n + 1][5];

        // Store selected original indices
        List<Integer>[][] ans = new ArrayList[n + 1][5];

        for (int i = 0; i <= n; i++) {
            for (int k = 0; k <= 4; k++) {
                ans[i][k] = new ArrayList<>();
            }
        }

        for (int i = 1; i <= n; i++) {

            int start = arr[i - 1][1];
            int weight = arr[i - 1][2];

            // Find last interval with end < current start
            int lo = 0;
            int hi = i - 2;
            int prev = -1;

            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;

                if (arr[mid][0] < start) {
                    prev = mid;
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }

            for (int k = 1; k <= 4; k++) {

                // Option 1: Skip
                dp[i][k] = dp[i - 1][k];
                ans[i][k] = new ArrayList<>(ans[i - 1][k]);

                // Option 2: Take
                long take = weight + dp[prev + 1][k - 1];

                List<Integer> candidate = new ArrayList<>(ans[prev + 1][k - 1]);

                candidate.add(arr[i - 1][3]);
                Collections.sort(candidate);

                if (take > dp[i][k] ||
                        (take == dp[i][k] &&
                                isSmaller(candidate, ans[i][k]))) {

                    dp[i][k] = take;
                    ans[i][k] = candidate;
                }
            }
        }

        return ans[n][4]
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private boolean isSmaller(List<Integer> a, List<Integer> b) {

        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {

            if (!a.get(i).equals(b.get(i))) {
                return a.get(i) < b.get(i);
            }
        }

        return a.size() < b.size();

    }
}