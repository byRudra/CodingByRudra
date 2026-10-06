# 1049. Last Stone Weight II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/last-stone-weight-ii/)

`Array` · `Dynamic Programming` · `Knapsack Problem` · `0-1 Knapsack`

## Intuition  
If we view each smash as a way of partitioning the stones into two groups whose total weights differ by the final stone, the game’s outcome is exactly the absolute difference between the sums of the two groups. The smallest possible leftover weight is therefore achieved when the two groups are as balanced as possible, i.e., when one group’s sum is as close as possible to `totalSum / 2`. The naïve way would be to enumerate every subset ( 2ⁿ ) or to use a hash‑set of reachable sums, both of which are exponential or require extra passes. The key insight is that the classic 0‑1 knapsack DP can compute the largest achievable subset sum that does not exceed `target = totalSum / 2` in a single double loop, eliminating the need for extra data structures or multiple traversals. This is the **0‑1 knapsack (subset‑sum) pattern**.

## Approach  
1. **Compute total weight** – iterate `for (int num : nums) totalSum += num;`.  
2. **Define capacity** – `target = totalSum / 2;` is the maximum sum we are allowed to pack into the first group.  
3. **Create DP table** – `dp[i][j]` stores the maximum sum ≤ j that can be formed using the first `i` stones. The table size is `(n+1) × (target+1)`.  
4. **Initialize borders** – for any capacity `j>0`, `dp[0][j] = 0` (no stones ⇒ sum 0); for any `i`, `dp[i][0] = 0` (capacity 0 ⇒ sum 0).  
5. **Fill the table** –  
   - Outer loop `for (int i = 1; i <= n; i++)` processes stones one by one; invariant: rows `0..i‑1` already contain optimal sums for the first `i‑1` stones.  
   - Inner loop `for (int j = 1; j <= target; j++)` scans capacities; invariant: `dp[i‑1][j]` is the best sum without the current stone.  
   - **Skip case** – `dp[i][j] = dp[i‑1][j];` keeps the previous optimum when we do not take `nums[i‑1]`.  
   - **Take case** – if `nums[i‑1] <= j`, we consider adding the stone: `dp[i][j] = Math.max(dp[i‑1][j], nums[i‑1] + dp[i‑1][j - nums[i-1]]);`. This respects the 0‑1 rule (each stone used at most once).  
6. **Derive answer** – after the loops, `dp[n][target]` is the largest achievable sum ≤ target. The minimal leftover weight is `totalSum - 2 * dp[n][target]`, because the second group automatically receives the remaining weight.

## Dry Run  
Input: `stones = [2,7,4,1,8,1]` → `totalSum = 23`, `target = 11`.

| i (processed) | j (capacity) | dp[i][j] after update | note |
|---|---|---|---|
| 1 (stone 2) | 1 | 0 | 2 > 1, cannot take |
| 1 | 2 | 2 | take 2 (2 ≤ 2) |
| 2 (stone 7) | 7 | 7 | take 7, better than previous 2 |
| 2 | 9 | 9 | 7 + 2 fits, max becomes 9 |
| 3 (stone 4) | 11 | 11 | 7 + 4 fits, reaches capacity |
| 4 (stone 1) | 11 | 11 | no improvement, already optimal |
| 5 (stone 8) | 11 | 11 | 8 + 2 + 1 =11 also optimal |
| 6 (stone 1) | 11 | 11 | final table entry unchanged |

After processing all six stones, `dp[6][11] = 11`. The answer is `23 - 2*11 = 1`, which matches the optimal leftover weight.

## Complexity  
- **Time:** `O(n * target)` – the double loop runs `n` times, and each iteration advances `j` up to `target`, which is `totalSum/2`.  
- **Space:** `O(n * target)` – the DP matrix `dp` of size `(n+1) × (target+1)` stores integer sums; the output itself is not counted.

## Solution (Java)

```java
class Solution {
    public int lastStoneWeightII(int[] nums) {
        int totalSum = 0;
        for (int num : nums)
            totalSum += num;

        int target = totalSum / 2;
        int n = nums.length;
        int dp[][] = new int[n + 1][target + 1];
        // initializing dp
        for (int i = 1; i <= target; i++) {
            dp[0][i] = 0;
        }
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // Don't take
                dp[i][j] = dp[i - 1][j];

                // check if taking will help or not
                if (nums[i - 1] <= j) {
                    dp[i][j] = Math.max(dp[i - 1][j], nums[i - 1] + dp[i - 1][j - nums[i - 1]]);
                }
            }
        }
        return totalSum - 2 * dp[n][target];
    }
}
```

---

**Runtime** 6 ms (beats 18.3%) · **Memory** 43.7 MB (beats 32.0%)

<sub>Synced by AILeetHub on 2026-10-06.</sub>
