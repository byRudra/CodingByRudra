# 322. Coin Change

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/coin-change/)

`Array` · `Dynamic Programming` · `Breadth-First Search` · `Knapsack Problem` · `Complete Knapsack`

## Intuition  
The key observation is that the optimal solution for any amount *i* can be built from an optimal solution of a smaller amount *i − coin*. If we already know the fewest coins needed to reach every value below *i*, we can obtain the answer for *i* by trying each coin and adding one to the best sub‑solution that fits. A naïve approach would try all subsets or recurse with memoization, which either explodes combinatorially or needs an extra pass to backtrack. By storing the best count for every prefix amount once and reusing it, we eliminate the exponential blow‑up and need only a single forward scan—this is the classic unbounded knapsack / DP pattern.

## Approach  
1. **Initialisation**  
   * Create `dp` of length `amount + 1`.  
   * Fill every entry with `amount + 1`, a sentinel larger than any possible answer (since the worst case uses `amount` coins of value 1).  
   * Set `dp[0] = 0` because zero coins are needed to make amount 0.  

2. **Outer loop (`i` from 1 to `amount`)**  
   * **Invariant:** before processing `i`, `dp[0..i‑1]` already contain the minimum coin counts for those amounts.  
   * **Exit condition:** loop stops when `i` exceeds `amount`.  

3. **Inner loop (iterate `coin` in `coins`)**  
   * Guard `if (i >= coin)` ensures we only consider coins that do not overshoot the current target.  
   * Update rule: `dp[i] = Math.min(dp[i - coin] + 1, dp[i])`.  
   * **Invariant:** after each iteration of the inner loop, `dp[i]` holds the smallest count achievable using the subset of coins examined so far.  

4. **Result extraction**  
   * If `dp[amount]` stayed at the sentinel (`amount + 1`), no combination was possible → return `-1`.  
   * Otherwise return `dp[amount]`.  

**Edge‑case handling**  
* When `amount` is 0 the outer loop never runs and `dp[0]` (0) is returned.  
* A single‑element `coins` array works because the inner loop still iterates once; the `>=` guard prevents negative indexing.  
* The sentinel `amount + 1` is safe from overflow because `amount ≤ 10⁴`.  

## Dry Run  
Input: `coins = [1,3,4]`, `amount = 6`

| i (target) | dp before inner loop | coin examined | dp after update | note |
|------------|----------------------|---------------|----------------|------|
| 1 | [0,∞] | 1 | dp[1]=1 | 1 = 1×1 |
|   |          | 3,4 skipped (i<coin) |   | |
| 2 | [0,1,∞] | 1 | dp[2]=2 | 2 = 1+1 |
|   |          | 3,4 skipped |   | |
| 3 | [0,1,2,∞] | 1 | dp[3]=3 (temp) | 1+1+1 |
|   |          | 3 | dp[3]=1 (min) | 3 alone |
|   |          | 4 skipped |   | |
| 4 | [0,1,2,1,∞] | 1 | dp[4]=2 (temp) | 1+3 |
|   |          | 3 | dp[4]=2 (no change) | 3+1 |
|   |          | 4 | dp[4]=1 (min) | 4 alone |
| 5 | [0,1,2,1,1,∞] | 1 | dp[5]=2 (temp) | 1+4 |
|   |          | 3 | dp[5]=2 (no change) | 3+1+1 |
|   |          | 4 | dp[5]=2 (no change) | 4+1 |
| 6 | [0,1,2,1,1,2,∞] | 1 | dp[6]=3 (temp) | 1+5 |
|   |          | 3 | dp[6]=2 (min) | 3+3 |
|   |          | 4 | dp[6]=2 (no change) | 4+1+1 |

Final `dp[6] = 2`, meaning the amount 6 can be formed with two coins (3 + 3).  

## Complexity  
- **Time:** `O(amount × n)` where `n = coins.length`; the outer loop runs `amount` times and the inner loop scans all `n` coins each iteration.  
- **Space:** `O(amount)` for the `dp` array; no additional structures beyond a few scalars are allocated.

## Solution (Java)

```java
class Solution {
    public int coinChange(int[] coins, int amount) {
        int dp[] = new int[amount  + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for(int i = 1; i <= amount; i++){
            for(int coin : coins){
                if(i >= coin){
                    dp[i] = Math.min(dp[i - coin] + 1 , dp[i]);
                }
            }
        }
        return dp[amount] == amount + 1 ? -1 : dp[amount]; 
    }
}
```

---

**Runtime** 15 ms (beats 83.7%) · **Memory** 46.4 MB (beats 51.8%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
