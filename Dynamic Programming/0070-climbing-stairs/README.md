# 70. Climbing Stairs

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/climbing-stairs/)

`Math` · `Dynamic Programming` · `Memoization`

## Intuition  
The number of ways to reach step *i* equals the sum of ways to reach the two previous steps because the last move can be either a single‑step or a double‑step. Formally, `ways[i] = ways[i‑1] + ways[i‑2]`. A naïve recursive solution would explore all combinations and run in exponential time; recognizing this linear recurrence lets us compute each value once and reuse it, eliminating the extra passes or a hash‑map. This is the classic **dynamic programming** pattern for Fibonacci‑like sequences.

## Approach  
1. **Handle trivial inputs.**  
   - If `n == 1` return 1.  
   - If `n == 2` return 2.  
   These guard clauses avoid out‑of‑bounds access and satisfy the base cases of the recurrence.  

2. **Allocate storage.**  
   - Create `int[] dp = new int[n + 1];`.  
   The extra slot at index 0 is unused but keeps indexing natural (1‑based).  

3. **Initialize the first two entries.**  
   - `dp[1] = 1;` – one way to stand on the first step.  
   - `dp[2] = 2;` – either two single steps or one double step.  

4. **Iterate from 3 to n inclusive.**  
   - Loop condition: `i <= n`.  
   - **Invariant:** before each iteration, `dp[i‑1]` and `dp[i‑2]` already hold the correct counts for their respective step numbers.  
   - Update: `dp[i] = dp[i‑1] + dp[i‑2];`. This directly applies the recurrence.  

5. **Return the result.**  
   - After the loop finishes, `dp[n]` contains the total ways for `n` steps, so `return dp[n];`.  

**Edge‑case decisions:**  
- The early returns for `n == 1` and `n == 2` avoid accessing `dp[0]` or uninitialized entries.  
- The array size `n+1` guarantees that `dp[n]` is a valid index even when `n` equals the maximum constraint (45).  
- The loop uses `<= n` rather than `< n+1` to keep the bound explicit and readable.

## Dry Run  

**Input:** `n = 4`

| i (iteration) | dp[i‑2] | dp[i‑1] | dp[i] (computed) | Note |
|---------------|--------|--------|------------------|------|
| init          | –      | –      | dp[1]=1, dp[2]=2 | base cases set |
| 3             | 1      | 2      | 3 = 2+1          | dp[3] = ways to step 3 |
| 4             | 2      | 3      | 5 = 3+2          | dp[4] = ways to step 4 |

After the loop, `dp[4] = 5`, which is the number of distinct ways to climb four steps.

## Complexity  
- **Time:** O(n) – the `for` loop runs once for each integer from 3 up to n, performing a constant‑time addition each iteration.  
- **Space:** O(n) – the `dp` array stores a value for every step from 0 to n; the output itself is not counted toward extra space. (A constant‑space variant is possible by keeping only the last two values, but the submitted code uses the full array.)

## Solution (Java)

```java
class Solution {
    public int climbStairs(int n) {
        int [] dp = new int [n+1];
        if (n == 1){return 1;}
        if (n == 2){return 2;}
        dp[1] = 1;
        dp[2] = 2;
        for(int i = 3; i <= n; i++){
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 41 MB (beats 100.0%)

<sub>Synced by AILeetHub on 2025-04-02.</sub>
