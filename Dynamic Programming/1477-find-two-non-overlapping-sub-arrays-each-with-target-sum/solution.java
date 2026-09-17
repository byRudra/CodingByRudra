class Solution {
    public int minSumOfLengths(int[] arr, int target) {
        int n = arr.length;

        // main window: finds windows summing to target, scanning forward
        int left = 0;
        long sum = 0;
        int ans = Integer.MAX_VALUE;

        // shadow window: independently scans [0, left-1], tracking the
        // best (shortest) valid subarray length found so far in that range
        int l2 = 0, r2 = -1;
        long sum2 = 0;
        int bestBefore = Integer.MAX_VALUE;

        for (int right = 0; right < n; right++) {
            sum += arr[right];
            while (sum > target) {
                sum -= arr[left];
                left++;
            }

            // catch the shadow window up to cover indices [0, left-1]
            while (r2 < left - 1) {
                r2++;
                sum2 += arr[r2];
                while (sum2 > target) {
                    sum2 -= arr[l2];
                    l2++;
                }
                if (sum2 == target) {
                    bestBefore = Math.min(bestBefore, r2 - l2 + 1);
                }
            }

            if (sum == target) {
                int length = right - left + 1;
                if (bestBefore != Integer.MAX_VALUE) {
                    ans = Math.min(ans, bestBefore + length);
                }
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}