# 517. Super Washing Machines

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/super-washing-machines/)

`Array` · `Greedy`

## Intuition  
The key observation is that after we decide the final uniform count `each = totalClothes / n`, every machine can be described by two numbers:  

1. `diff = cloth - each` – how many dresses it must give away (positive) or receive (negative).  
2. The running prefix sum `balance` – the net excess that has to be pushed across the boundary between the current machine and its left neighbor.  

During any sequence of moves, the number of simultaneous transfers needed at a position is bounded by the larger of the absolute prefix imbalance (`|balance|`) and the local surplus/deficit (`diff`). The answer is therefore the maximum of these values over all positions. This eliminates the naïve approach of simulating moves or using extra passes to track individual transfers.

The pattern used is a **greedy prefix‑balance scan**.

## Approach  
1. **Compute total dresses**  
   ```java
   totalClothes += cloth;
   ```  
   If `totalClothes % n != 0` the target `each` is not integral → return `-1`.  

2. **Determine target per machine**  
   ```java
   each = totalClothes / n;
   ```  

3. **Iterate once over `machines`** keeping three mutable variables:  
   - `diff = cloth - each` – local surplus/deficit.  
   - `balance += diff` – cumulative excess that must cross the current left‑right cut.  
   - `answer = max(answer, max(|balance|, diff))` – update the global worst‑case moves.  

   *Loop invariant*: after processing the first `i` machines, `balance` equals the net number of dresses that must be moved from the left segment `[0..i]` to the right segment `[i+1..n‑1]`.  

   Edge cases handled:  
   - **Empty or single‑element array** – the loop runs zero or one iteration; `answer` stays `0`.  
   - **Even vs. odd length** – irrelevant because the invariant is position‑based, not length‑based.  
   - **All `diff` zero** – `balance` never deviates, `answer` remains `0`.  
   - The `Math.max(Math.abs(balance), diff)` uses `abs` because the direction of flow does not affect the count of simultaneous moves, and `diff` is kept separate because a single machine may need to send out many dresses even if the prefix imbalance is small.  

4. **Return `answer`** – the minimal number of parallel moves required.

## Dry Run  
Input: `machines = [1, 0, 5]`  

| i | cloth | diff = cloth‑each | balance (prefix) | answer = max(prev, max(|balance|, diff)) | note |
|---|-------|-------------------|------------------|-------------------------------------------|------|
| 0 | 1     | 1‑2 = **-1**      | -1               | max(0, max(1, -1)) = **1**                | first deficit, need 1 dress from right |
| 1 | 0     | 0‑2 = **-2**      | -3               | max(1, max(3, -2)) = **3**                | cumulative deficit grows to 3 |
| 2 | 5     | 5‑2 = **+3**      | 0                | max(3, max(0, 3)) = **3**                 | surplus balances prefix, no larger need |

Final `answer = 3`, matching the optimal three moves described in the statement.

## Complexity  
- **Time:** O(n) – the algorithm makes a single pass to compute `totalClothes` and a second pass that updates `balance` and `answer`; each loop runs at most `n` iterations.  
- **Space:** O(1) – only a handful of integer variables (`totalClothes`, `each`, `balance`, `diff`, `answer`) are used, independent of input size. The output integer does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public int findMinMoves(int[] machines) {
        int totalClothes = 0;
        int n = machines.length;
        for (int cloth : machines) {
            totalClothes += cloth;
        }

        // A case where totalClothes is not a fully divisible by n means we cannot divide the clothes equally
        if (totalClothes % n != 0)
            return -1;

        int each = totalClothes / n;
        int answer = 0;
        int balance = 0;
        for (int cloth : machines) {
            int diff = cloth - each;
            balance += diff;

            answer = Math.max(answer, Math.max(Math.abs(balance), diff));
            // we do abs(balance) because direction dosent matter and we do max because if diff is +ve and balance was first -ve then we take max diff in account because it increases the moves
        }
        return answer;
    }
}
```

---

**Runtime** 1 ms (beats 99.8%) · **Memory** 46.7 MB (beats 24.5%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
