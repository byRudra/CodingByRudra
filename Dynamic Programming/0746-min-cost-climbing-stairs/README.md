# 746. Min Cost Climbing Stairs

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/min-cost-climbing-stairs/)

`Array` · `Dynamic Programming`

## Intuition  
The key observation is that the cheapest way to finish from any step *i* depends only on the cheapest ways from the next two reachable steps: `cost[i] + min(costFrom(i+1), costFrom(i+2))`. This creates a simple optimal‑substructure that lets us compute the answer by looking backwards, eliminating the need for a second pass, a hash map, or recursion. The pattern exploited here is classic **bottom‑up dynamic programming**.

## Approach  
1. **Allocate storage** – create `dp` of length `cost.length + 2`. The extra two cells (`dp[n]` and `dp[n+1]`) are initialized to 0, representing that no cost is required once we are past the last step.  
2. **Iterate backwards** – for `i` from `cost.length‑1` down to `0`:  
   - *Invariant*: before each iteration, `dp[i+1]` and `dp[i+2]` already hold the minimum cost to reach the top from those positions.  
   - Compute `dp[i] = Math.min(dp[i+1], dp[i+2]) + cost[i]`. This follows directly from the observation in step 1.  
3. **Choose the start** – after the loop, `dp[0]` is the cheapest cost when starting at step 0 and `dp[1]` when starting at step 1. Return `Math.min(dp[0], dp[1])`.  
   - The check uses `min` rather than a direct index because the problem permits starting on either of the first two steps; picking the smaller of the two guarantees optimality.  

**Edge handling** – The constraints guarantee `cost.length ≥ 2`, so the loop always runs at least once. The extra two slots in `dp` avoid any out‑of‑bounds checks when `i` is `n‑1` or `n‑2`.  

## Dry Run  

Input: `cost = [10, 15, 20]`  

| i | dp[i+2] | dp[i+1] | dp[i] (computed) | note |
|---|---------|---------|------------------|------|
| 2 | dp[4]=0 | dp[3]=0 | `min(0,0)+20 = 20` | last step costs 20 |
| 1 | dp[3]=0 | dp[2]=20| `min(0,20)+15 = 15`| cheaper to jump two steps |
| 0 | dp[2]=20| dp[1]=15| `min(20,15)+10 = 15`| best path starts at step 1 |

After the loop `dp = [15,15,20,0,0]`. The final answer is `min(dp[0], dp[1]) = 15`, which matches the optimal strategy of starting at index 1 and jumping directly to the top.

## Complexity  
- **Time:** `O(n)` – the backward loop visits each of the `n` elements exactly once, and each iteration performs only constant‑time work.  
- **Space:** `O(n)` – the `dp` array stores `n+2` integers; the output itself is not counted toward extra space. (The algorithm could be reduced to `O(1)` by keeping only the last two values, but the submitted code uses the full array.)

## Solution (Java)

```java
class Solution {
    public int minCostClimbingStairs(int[] cost) {
        int dp[] = new int[cost.length + 2];
        for(int i = cost.length - 1; i >= 0; i--){
            dp[i] = Math.min(dp[i + 1], dp[i + 2]) + cost[i];
        }
        return Math.min(dp[0], dp[1]);
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 45 MB (beats 22.7%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
