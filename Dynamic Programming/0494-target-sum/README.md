# 494. Target Sum

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/target-sum/)

`Array` · `Dynamic Programming` · `Backtracking` · `Knapsack Problem` · `0-1 Knapsack`

## Intuition  
The expression “+ nums[i] or ‑ nums[i]” splits the original array into two disjoint groups: numbers taken with a plus sign (call their sum **P**) and numbers taken with a minus sign (sum **N**). The final value is `P ‑ N = target`. Because every element belongs to exactly one group, we also have `P + N = total`, where **total** is the sum of all array elements. Adding the two equations eliminates **N** and yields `2·P = target + total`. Therefore a feasible assignment exists **iff** `target + total` is non‑negative and even, and the problem reduces to counting subsets whose sum equals `real = (target + total) / 2`. This observation removes the need for exponential backtracking or a second pass to balance signs; we can solve it with a classic 0‑1 knapsack DP that counts ways to reach a given sum.

## Approach  
1. **Compute total** – iterate `for (int num : nums) total += num;`.  
2. **Validate feasibility** – if `target + total` is negative or odd, return 0.  
3. **Derive real target** – `int real = (target + total) / 2;`. This is the exact sum we must achieve with a subset of `nums`.  
4. **Allocate DP table** – `int[][] dp = new int[n + 1][real + 1];` where `dp[i][j]` stores the number of ways to reach sum `j` using the first `i` numbers.  
5. **Initialize base case** – for every `i` (`0 ≤ i ≤ n`) set `dp[i][0] = 1` because the empty subset always yields sum 0.  
6. **Fill the table** –  
   ```java
   for (int i = 1; i <= n; i++) {
       for (int j = 0; j <= real; j++) {
           dp[i][j] = dp[i-1][j];                     // skip nums[i‑1]
           if (nums[i-1] <= j) {
               dp[i][j] += dp[i-1][j-nums[i-1]];      // take nums[i‑1]
           }
       }
   }
   ```  
   *Loop invariant*: before processing `i`, `dp[i‑1][*]` correctly counts ways using the first `i‑1` elements. After the inner loop finishes, `dp[i][j]` holds the correct count for the first `i` elements.  
   Edge handling: when `nums[i‑1] > j` the inner `if` is skipped, preventing out‑of‑bounds access. The `<=` in the outer loop (`j <= real`) ensures we also compute the exact target column.  
7. **Return answer** – `dp[n][real]` is the number of subsets that sum to `real`, which equals the number of valid `+ / -` assignments.

## Dry Run  
Input: `nums = [1,1,1,1,1]`, `target = 3`  

- `total = 5`, `real = (5+3)/2 = 4`.  
- DP table dimensions: `6 × 5` (rows 0‑5, cols 0‑4).  

| i (processed) | j (sum) | dp[i][j] | change |
|---------------|---------|----------|--------|
| 0 | 0 | 1 | base case |
| 0 | 1‑4 | 0 | no elements yet |
| 1 | 0 | 1 | inherit dp[0][0] |
| 1 | 1 | 1 | dp[0][1] + dp[0][0] (take first 1) |
| 1 | 2‑4 | 0 | cannot reach larger sums |
| 2 | 0 | 1 | always 1 |
| 2 | 1 | 2 | skip + take second 1 |
| 2 | 2 | 1 | take both 1s |
| 2 | 3‑4 | 0 | — |
| 3 | 0 | 1 |
| 3 | 1 | 3 |
| 3 | 2 | 3 |
| 3 | 3 | 1 |
| 3 | 4 | 0 |
| 4 | 0 | 1 |
| 4 | 1 | 4 |
| 4 | 2 | 6 |
| 4 | 3 | 4 |
| 4 | 4 | 1 |
| 5 | 0 | 1 |
| 5 | 1 | 5 |
| 5 | 2 | 10 |
| 5 | 3 | 10 |
| 5 | 4 | **5** |

After processing all five numbers, `dp[5][4] = 5`, which matches the five valid sign assignments.

## Complexity  
- **Time:** `O(n * real)` – the double loop runs `n` times, and each inner iteration advances `j` from 0 to `real`.  
- **Space:** `O(n * real)` – the DP matrix stores a count for every `(i, j)` pair; the output integer itself is not counted.

## Solution (Java)

```java
class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        int total = 0;
        for(int num : nums){
            total+=num;
        }
        if(((total + target) < 0) || (target + total) % 2 != 0 ) return 0;
        int real = (target + total) / 2;
        int dp[][] = new int[n + 1][real + 1];
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= real; j++) {
                dp[i][j] = dp[i - 1][j];
                if (nums[i - 1] <= j) {
                    dp[i][j] += dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[n][real] ;
    }
}
```

---

**Runtime** 11 ms (beats 50.3%) · **Memory** 45.3 MB (beats 37.7%)

<sub>Synced by AILeetHub on 2026-10-06.</sub>
