# 3505. Minimum Operations to Make Elements Within K Subarrays Equal

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-operations-to-make-elements-within-k-subarrays-equal/)

`Array` · `Hash Table` · `Math` · `Dynamic Programming` · `Sliding Window` · `Heap (Priority Queue)`

## Intuition  
The key observation is that the cost to make a fixed‑size window equal is completely determined by its median: the sum of absolute deviations from the median is minimal. If we can obtain the median and the sums of the lower and upper halves of each sliding window in O(log M) time (where M is the number of distinct values), we avoid the naïve O(x) scan per window. This eliminates the extra O(n·x) pass that a direct computation would require. The solution therefore combines two classic patterns: **coordinate compression + Fenwick trees** to maintain order‑statistics in a sliding window, and a **DP over non‑overlapping intervals** to pick the cheapest k windows.

## Approach  
1. **Compress values** –  
   *Sort a copy of `nums` and store the distinct numbers in `vals`.  
   *For each original element compute `id[i] = binarySearch(vals, nums[i]) + 1` (1‑indexed).  
   This maps the possibly huge range `[-10⁶,10⁶]` to `[1…m]` so Fenwick indices stay small.  

2. **Fenwick structures** –  
   *`cnt[p]` stores the count of numbers whose compressed index ≤ p.  
   *`sum[p]` stores the sum of those numbers.  
   Both are updated with the usual `for (int p = id[i]; p <= m; p += p & -p)` loops.  

3. **Slide the window** –  
   *Iterate `i` from 0 to n‑1, inserting `nums[i]` into the trees and adding to `windowSum`.  
   *When `i ≥ x` remove the element at `old = i‑x` using the same Fenwick update pattern (decrement).  
   *The loop invariant: after processing index i, the trees contain exactly the multiset of the current window `nums[i‑x+1 … i]`.  

4. **Extract the median** –  
   *`target = (x+1)/2` is the position of the lower median.  
   *Binary‑lifting on the Fenwick count tree finds the smallest `pos` such that the prefix count ≥ target.  
   *During the lift we accumulate `below`, the sum of all elements strictly smaller than the median, and keep `rem`, the number of median copies still needed.  

5. **Compute window cost** –  
   *`leftSum = below + rem * med` is the sum of the `target` smallest elements.  
   *`rightSum = windowSum - leftSum` is the sum of the remaining `x‑target` elements.  
   *Cost formula: `med*target - leftSum + rightSum - med*(x‑target)`.  
   Store it in `cost[i‑x+1]`.  

6. **DP for k non‑overlapping windows** –  
   *`dp[j][i]` = minimal operations to pick j windows inside the prefix `nums[0…i‑1]`.  
   *Transition: either skip position i (`dp[j][i‑1]`) or end a window at `i‑1` (`dp[j‑1][i‑x] + cost[i‑x]`).  
   *Initialize `dp[0][*]=0` and fill the rest with `INF`.  

7. **Answer** – `dp[k][n]` holds the minimum total operations.

## Dry Run  
Input: `nums = [5, -2, 1, 3]`, `x = 2`, `k = 1`

| i | inserted | removed | window (i‑x+1…i) | median `med` | `cost[i‑x+1]` | note |
|---|----------|---------|------------------|--------------|---------------|------|
|0|5| – |[5] (size<2) | – | – | building first window |
|1|-2| – |[5, -2] | -2 (lower median) | `|-2-5| = 7` | first full window |
|2|1|5|[-2, 1] | -2 | `|-2+2| = 2` | slide right |
|3|3|-2|[1, 3] | 1 | `|1-3| = 2` | slide right |

After processing, `cost = [7,2,2]`. DP picks the cheapest single window (`2`), so the answer is 2.

## Complexity  
- **Time:** O(n log m + k·n) – each element is inserted and possibly removed once (Fenwick updates O(log m)), median extraction is another O(log m); the DP runs `k` passes over `n`.  
- **Space:** O(m + n + k·n) ≈ O(n + m) – Fenwick arrays of size m, the `cost` array of size n, and the DP table of size (k+1)·(n+1); output and recursion stacks are excluded.

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
