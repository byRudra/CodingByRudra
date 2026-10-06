# 494. Target Sum

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/target-sum/)

`Array` · `Dynamic Programming` · `Backtracking` · `Knapsack Problem` · `0-1 Knapsack`

## Intuition  
If we split the numbers into a “positive” group **P** and a “negative” group **N**, the expression value is `sum(P) – sum(N)`. Let `total = sum(nums)`. Because `sum(P) + sum(N) = total`, we can rewrite the target equation as `2·sum(P) = target + total`. Thus a feasible assignment exists only when `target + total` is non‑negative and even, and the problem reduces to counting subsets whose sum equals `(target + total) / 2`. This eliminates the exponential back‑tracking over signs and replaces it with a classic 0‑1 knapsack count.

## Approach  
1. **Compute total sum** – iterate `for (int num : nums) total += num`.  
2. **Validate feasibility** – if `target + total < 0` or `(target + total) % 2 != 0` return `0`.  
3. **Derive real target** – `realTarget = (target + total) / 2`. This is the exact sum we need from the “positive” subset.  
4. **Initialize DP table** – `dp[i][j]` stores the number of ways to reach sum `j` using the first `i` numbers. Set `dp[i][0] = 1` for all `i` because the empty subset always yields sum 0.  
5. **Fill DP** – for each `i` from `1` to `n` and each `j` from `0` to `realTarget`:  
   - **Invariant**: before processing `dp[i][j]`, `dp[i‑1][*]` already contains correct counts for the first `i‑1` elements.  
   - Start with `dp[i][j] = dp[i‑1][j]` (exclude `nums[i‑1]`).  
   - If `nums[i‑1] ≤ j`, add the ways that include the current number: `dp[i][j] += dp[i‑1][j‑nums[i‑1]]`.  
   This respects the 0‑1 constraint because each element is considered exactly once.  
6. **Return answer** – `dp[n][realTarget]` holds the total number of valid sign assignments.

**Edge handling**:  
- Empty or single‑element arrays are covered by the DP initialization (`dp[i][0] = 1`).  
- When `realTarget` exceeds possible sum, the inner loop simply never adds the inclusion term, yielding zero ways.  
- The feasibility check uses `<=` for the negativity test because a negative `target + total` would make `realTarget` negative, which cannot be represented in the DP table.

## Dry Run  
Input: `nums = [1,1,1,1,1]`, `target = 3`  

| i (processed) | j (current sum) | dp[i‑1][j] | include? (`nums[i‑1] ≤ j`) | dp[i][j] after update | note |
|---|---|---|---|---|---|
| 0 | 0 | – | – | 1 | base case, empty subset |
| 1 | 0 | 1 | no | 1 | cannot include 1 |
| 1 | 1 | 0 | yes | 1 | take first 1 |
| 2 | 0 | 1 | no | 1 | still only empty |
| 2 | 1 | 1 | yes | 2 | subsets: {first}, {second} |
| 2 | 2 | 0 | yes | 1 | {first,second} |
| 3 | 0 | 1 | no | 1 | … |
| 3 | 1 | 2 | yes | 3 | three ways to make 1 with three 1’s |
| 3 | 2 | 1 | yes | 3 | three ways to make 2 |
| 3 | 3 | 0 | yes | 1 | {first,second,third} |
| … | … | … | … | … | continue similarly |
| 5 | 5 | 0 | yes | 1 | all five 1’s |
| 5 | 4 | 1 | yes | 5 | five subsets sum to 4 |
| 5 | 3 | 2 | yes | **5** | final answer = 5 |

The DP finishes with `dp[5][4] = 5`, which equals the number of expressions evaluating to 3.

## Complexity  
- **Time:** `O(n * realTarget)` – the double loop runs `n` times, and each iteration processes up to `realTarget` columns; `realTarget` is at most `total ≤ 1000`.  
- **Space:** `O(n * realTarget)` – the 2‑D table stores `n+1` rows and `realTarget+1` columns; this excludes the input array.

## Solution (Java)

```java
class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        int total = 0;

        for(int num : nums){
            total += num;
        }

        // Impossible case 
        if(target + total < 0 || (target  + total) % 2 != 0){
            return 0;
        }

        int realTarget = (target + total) / 2;
        int dp[][] = new int[n + 1][realTarget + 1];
        for(int i = 0; i <= n; i++){
            dp[i][0] = 1;
        }
        for(int i = 1; i <= n; i++){
            for(int j = 0; j <= realTarget; j++){
                dp[i][j] = dp[i - 1][j];

                if(nums[i - 1] <= j){
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[n][realTarget];
    }
}
```

---

**Runtime** 6 ms (beats 73.8%) · **Memory** 45.3 MB (beats 37.7%)

<sub>Synced by AILeetHub on 2026-10-06.</sub>
