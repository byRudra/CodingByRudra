# 416. Partition Equal Subset Sum

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/partition-equal-subset-sum/)

`Array` · `Dynamic Programming` · `Knapsack Problem` · `0-1 Knapsack`

## Intuition  
The key observation is that a partition into two equal‑sum subsets exists **iff** the total sum is even and some subset of the numbers adds up to exactly half of that sum. This turns the problem into a classic 0‑1 knapsack: can we pick a subset whose weight equals `target = totalSum/2`? A naïve approach would try every subset (exponential) or use a hash set to store all reachable sums (extra linear pass). The insight that the reachable‑sum relation is monotone lets us fill a DP table once, eliminating the need for recursion or additional passes. The pattern used is the 0‑1 knapsack dynamic programming table.

## Approach  
1. **Compute total sum** – iterate `for (int num : nums) totalSum += num;`.  
2. **Early reject** – if `totalSum` is odd, return `false` because `target` would be non‑integer.  
3. **Initialize dimensions** – `target = totalSum/2`; `n = nums.length`; allocate `boolean dp[n+1][target+1]`.  
4. **Base cases** –  
   * For any `i`, `dp[i][0] = true` because a sum of 0 is achievable with the empty subset.  
   * For `i = 0` and `j > 0`, `dp[0][j] = false` because no elements means no positive sum.  
5. **Fill the table** – outer loop `for (int i = 1; i <= n; i++)` (consider first `i` numbers).  
   * **Invariant:** before processing row `i`, `dp[i‑1][j]` correctly tells whether a sum `j` is reachable using the first `i‑1` elements.  
   * Inner loop `for (int j = 1; j <= target; j++)` (target sum we try to achieve).  
   * **Don't take**: set `dp[i][j] = dp[i‑1][j]`.  
   * **Take if possible**: if `nums[i‑1] <= j`, update `dp[i][j] = dp[i‑1][j] || dp[i‑1][j‑nums[i‑1]]`. This captures the choice of including the current element.  
   * **Exit condition:** loops finish when `i == n` and `j == target`.  
6. **Result** – return `dp[n][target]`, which reflects whether the full array can achieve the half‑sum.

Edge‑case handling:  
* Empty or single‑element arrays are covered by the early odd‑sum check and the base row `dp[i][0] = true`.  
* The code uses `<=` for the inner loop bounds because `target` itself is a valid sum to test.  
* The condition `nums[i‑1] <= j` prevents out‑of‑range indexing when the current number exceeds the remaining capacity.

## Dry Run  
Input: `nums = [1, 5, 11, 5]` → `totalSum = 22`, `target = 11`.

| i (first i elems) | j (current sum) | dp[i][j] after update | note |
|-------------------|-----------------|----------------------|------|
| 1 | 1 | true | take `1` (dp[0][0]) |
| 1 | 2‑10 | false | `1` too small, cannot reach |
| 1 | 11 | false | cannot reach 11 with only `1` |
| 2 | 5 | true | take `5` (dp[1][0]) |
| 2 | 6 | true | `1+5` (dp[1][1]) |
| 2 | 11 | false | need 6 more, not possible yet |
| 3 | 11 | true | take `11` (dp[2][0]) |
| 4 | 11 | true | already true from previous rows |

After processing all rows, `dp[4][11]` is `true`, so the array can be split into `[1,5,5]` and `[11]`.

## Complexity  
- **Time:** O(n × target) – the double loop runs `n` times, and each iteration advances `j` up to `target`, which is `totalSum/2`.  
- **Space:** O(n × target) – the boolean table stores a value for every pair `(i, j)`. (If we ignore the output boolean, no additional structures are allocated.)

## Solution (Java)

```java
class Solution {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums)
            totalSum += num;

        int target = totalSum / 2;
        if (totalSum % 2 != 0) {
            return false;
        }
        int n = nums.length;
        boolean dp[][] = new boolean[n + 1][target + 1];
        // initializing dp
        for (int i = 1; i <= target; i++) {
            dp[0][i] = false;
        }
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // Don't take
                dp[i][j] = dp[i - 1][j];

                // check if taking will help or not
                if (nums[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[n][target];
    }
}
```

---

**Runtime** 64 ms (beats 39.3%) · **Memory** 47.6 MB (beats 55.7%)

<sub>Synced by AILeetHub on 2026-10-06.</sub>
