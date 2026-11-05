# 3505. Minimum Operations to Make Elements Within K Subarrays Equal

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-operations-to-make-elements-within-k-subarrays-equal/)

`Array` · `Hash Table` · `Math` · `Dynamic Programming` · `Sliding Window` · `Heap (Priority Queue)`

## Intuition  
The cost to make all numbers in a fixed‑size window equal is minimized by turning every element into the window’s median; the total operations equal the sum of distances to that median. Computing the median and the sum of distances for each sliding window naïvely would require sorting every window, leading to O(n·x log x). The key observation is that, while the window slides, we can maintain the multiset of its values in a Fenwick tree (binary indexed tree) that supports O(log m) updates and prefix‑sum queries, where m is the number of distinct values after coordinate compression. With a binary‑lifting query we retrieve the target‑th smallest element (the lower median) in O(log m). After we can compute the cost of each window in O(log m), the problem reduces to picking k non‑overlapping windows of length x with minimum total cost – a classic 1‑D DP over intervals.

## Approach  
1. **Coordinate compression** –  
   *`sorted`* holds a copy of `nums` sorted.  
   *`vals`* stores the distinct values; *`m`* is their count.  
   *`id[i]`* is the 1‑indexed rank of `nums[i]` in `vals` (found by binary search).  
   This lets us index Fenwick trees by value rank instead of raw magnitude.

2. **Fenwick trees for counts and sums** –  
   Arrays *`cnt`* and *`sum`* of size `m+1` store, respectively, the number of occurrences and the total of original values for each prefix of ranks.  
   *`top`* is the highest power of two ≤ `m` (used for binary lifting).  
   *`target = (x+1)/2`* is the position of the lower median inside a window of size `x`.

3. **Slide the window** –  
   For each index `i` from 0 to `n‑1`:
   - Insert `nums[i]` into both Fenwick trees (`cnt` ++, `sum` += `nums[i]`).  
   - Add `nums[i]` to `windowSum`.  
   - If the window exceeds length `x`, remove the element at `old = i‑x` from the trees and subtract it from `windowSum`.  
   - When the window first reaches size `x` (`i >= x‑1`), compute the median:
     *Binary lifting* walks down the tree using `pw = top, pw>>=1` to find the smallest rank `pos` whose cumulative count ≥ `target`.  
     `med = vals[pos]`.  
     `below` accumulates the sum of all values strictly smaller than the median; `rem` is how many median copies are needed to reach the exact `target`.  
     `leftSum = below + rem * med` (sum of the `target` smallest numbers).  
     `rightSum = windowSum - leftSum`.  
     Finally, `cost[i‑x+1] = med*target - leftSum + rightSum - med*(x‑target)`, which is the minimal operations for this window.

4. **Dynamic programming over windows** –  
   `dp[j][i]` = minimum operations to place `j` non‑overlapping windows within the first `i` elements.  
   Initialise `dp[0][*] = 0` and fill the rest with `INF`.  
   Transition for each `j` (1 .. k) and `i` (1 .. n):
   - Skip position `i`: `dp[j][i] = dp[j][i‑1]`.  
   - Take a window ending at `i‑1` (i.e., starting at `i‑x`): if `i >= x` and `dp[j‑1][i‑x]` is feasible, update  
     `dp[j][i] = min(dp[j][i], dp[j‑1][i‑x] + cost[i‑x])`.  
   The answer is `dp[k][n]`.

## Dry Run  
Input: `nums = [5, -2, 1, 3]`, `x = 2`, `k = 1`.

| i | window (i‑x+1 … i) | median (`med`) | `windowSum` | `cost[i‑x+1]` | note |
|---|--------------------|----------------|-------------|---------------|------|
| 0 | –                  | –              | 5           | –             | insert 5 |
| 1 | [5, -2]            | -2 (target=1)  | 3           | 7             | median -2, ops = (5‑(-2)) = 7 |
| 2 | [-2, 1]            | -2 (target=1)  | -1          | 3             | slide: remove 5, add 1 |
| 3 | [1, 3]             | 1 (target=1)   | 4           | 2             | slide: remove -2, add 3 |

After processing, `cost = [7,3,2]`. DP chooses the cheapest single window, `dp[1][4] = 2`, which corresponds to making subarray `[1,3]` equal to `1` with 2 operations.

## Complexity  
- **Time:** O(n log n) – sorting for compression O(n log n), each of the n iterations performs a constant number of Fenwick updates and a binary‑lifting query, both O(log m) ≤ O(log n); DP runs O(k·n) with k ≤ 15, dominated by the previous step.  
- **Space:** O(n) – the compressed value array, two Fenwick trees of size m ≤ n, the `cost` array of length n‑x+1, and a DP table of size (k+1)·(n+1); output storage is excluded.

## Solution (Java)

```java
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
```

---

**Runtime** 261 ms (beats 100.0%) · **Memory** 135.7 MB (beats 86.5%)

<sub>Synced by AILeetHub on 2026-11-04.</sub>
