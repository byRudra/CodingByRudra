# 3505. Minimum Operations to Make Elements Within K Subarrays Equal

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-operations-to-make-elements-within-k-subarrays-equal/)

`Array` · `Hash Table` · `Math` · `Dynamic Programming` · `Sliding Window` · `Heap (Priority Queue)`

## Intuition  
The costly part of the problem is measuring, for every length‑`x` window, the minimum number of ±1 operations needed to make all its elements equal. That cost equals the sum of absolute deviations from the window’s median, and the median can be obtained without sorting the window if we maintain a multiset of its values. By compressing the values and using two Fenwick trees (one for frequencies, one for prefix sums) we can update the multiset in O(log m) per slide and retrieve the `target`‑th smallest element (the lower median) with binary lifting in O(log m). Once each window’s cost is known, the remaining task is to pick `k` non‑overlapping windows of size `x` with minimum total cost, which is a classic 1‑D DP over intervals. The naive approach would recompute a window’s median by sorting each time (O(x log x) per window) and then try all subsets (exponential), but the two‑pointer‑like Fenwick trick removes the extra O(x) factor, and the DP eliminates the exponential search.

## Approach  
1. **Coordinate compression** – copy `nums` to `sorted`, sort it, and build `vals` containing the distinct values. Each original element gets an index `id[i]` (1‑based) via binary search.  
2. **Fenwick initialization** – allocate `cnt` (int) and `sum` (long) of size `m+1`, where `m` is the number of distinct values. `top` stores the highest power‑of‑two ≤ `m` for binary lifting. `target = (x+1)/2` is the position of the lower median inside a window.  
3. **Slide the window** – for each index `i` (0‑based):  
   - Add `nums[i]` to the Fenwick trees: `cnt[p]++`, `sum[p] += nums[i]` for all ancestors `p`.  
   - Add `nums[i]` to `windowSum`.  
   - If the window exceeded length `x`, remove the element at `old = i‑x` by decrementing the same Fenwick entries and subtracting from `windowSum`.  
   The loop invariant: after processing `i`, the Fenwick trees represent exactly the multiset of the current window `nums[max(0,i‑x+1)…i]`, and `windowSum` equals its total sum.  
4. **Extract the median** – when the window is full (`i ≥ x‑1`):  
   - Perform binary lifting on `cnt` to locate the smallest index `pos` such that the cumulative count reaches `target`. `below` accumulates the sum of all values strictly smaller than the median.  
   - `med = vals[pos]`.  
   - `leftSum = below + rem * med` gives the sum of the `target` smallest elements, where `rem` is the remaining count needed at `pos`.  
   - `rightSum = windowSum - leftSum`.  
5. **Compute window cost** – using the median formula:  
   `cost[start] = med*target - leftSum + rightSum - med*(x‑target)`.  
   This stores the minimal operations for the window beginning at `start = i‑x+1`.  
6. **Dynamic programming over windows** – `dp[j][i]` = minimal operations to select `j` windows within the prefix `nums[0…i‑1]`. Transition:  
   - Skip position `i`: `dp[j][i] = dp[j][i‑1]`.  
   - Take a window ending at `i‑1` (requires `i ≥ x`): `dp[j][i] = min(dp[j][i], dp[j‑1][i‑x] + cost[i‑x])`.  
   The DP respects the non‑overlap constraint because a taken window consumes exactly `x` positions.  

## Dry Run  
**Input:** `nums = [1, 2, 3, 4]`, `x = 2`, `k = 1`  

| i | added value | window (size 2) | median `med` | `cost[i‑x+1]` | note |
|---|-------------|----------------|--------------|--------------|------|
|0|+1|[1]| – | – | only one element, window not full |
|1|+2|[1,2]|1 (target=1) | `1*1‑1 + (3‑1)‑1*1 = 1` | first full window, cost = 1 |
|2|+3, remove 1|[2,3]|2| `2*1‑2 + (5‑2)‑2*1 = 1`| second window |
|3|+4, remove 2|[3,4]|3| `3*1‑3 + (7‑3)‑3*1 = 1`| third window |

The DP picks the cheapest single window, which is `1`. Hence the answer is `1`.

## Complexity  
- **Time:** O(n log m) for the sliding‑window updates and median extraction (each update touches O(log m) Fenwick nodes, binary lifting is O(log m)), plus O(k·n) for the DP, so overall **O(n log n + k·n)** because `m ≤ n`.  
- **Space:** O(n + m + k·n) for the `cost` array (`n‑x+1`), the two Fenwick trees (`m+1`), and the DP table (`(k+1)·(n+1)`). The output itself is a single `long`, so the extra space is **O(k·n)**.

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

**Runtime** 261 ms (beats 100.0%) · **Memory** 135.7 MB (beats 86.8%)

<sub>Synced by AILeetHub on 2026-11-04.</sub>
